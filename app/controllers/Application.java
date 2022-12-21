package controllers;

import javax.inject.Inject;

import com.typesafe.config.Config;

import models.User;
import models.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Validate;
import play.data.validation.Constraints.Validatable;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

public class Application extends Controller {
  @Validate
  public static class Login implements Validatable<String> {
    @Constraints.Required
    private String username;
    @Constraints.Required
    private String password;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    @Override
    public String validate() {
      if (User.authenticate(username, password) == null) {
        return "Benutzer oder Passwort ungültig!";
      }
      return null;
    }
  }
  
  @Validate
  public static class ChangePassword implements Validatable<String> {
    @Constraints.Required
    @MinLength(6)
    public String password;
    @MinLength(6)
    public String confirmPassword;
    
    @Override
    public String validate() {
      if(!password.equals(confirmPassword)) {
        return "Passwörter stimmen nicht überein";
      }
      
      return null;
    }
  }
  
  @Validate
  public static class Signup implements Validatable<String> {
    @Constraints.Required
    private String username;
    @Constraints.Required
    @MinLength(6)
    private String password;
    @MinLength(6)
    private String confirmPassword;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getConfirmPassword() {
      return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
      this.confirmPassword = confirmPassword;
    }

    @Override
    public String validate() {
      if(!password.equals(confirmPassword)) {
        return "Passwörter stimmen nicht überein";
      }
      
      User user = User.find.query().where().eq("name", username).findOne();
      if(user != null) {
        return "Der Benutzer existiert bereits.";
      }
      
      return null;
    }
  }
  
  private final FormFactory formFactory;
  private final Config config;
  private final MessagesApi messagesApi;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  public Application(FormFactory formFactory, Config config, MessagesApi messagesApi) {
    this.formFactory = formFactory;
    this.config = config;
    this.messagesApi = messagesApi;
  }


  public Result authenticate(Http.Request request) {
    Form<Login> login_form = formFactory.form(Login.class).bindFromRequest(request);

    if (login_form.hasErrors()) {
      return badRequest(login.render(login_form, request, messagesApi.preferred(request)));
    } else {
      String user_name = login_form.get().username.toLowerCase();
      logger.info("Login: {}", user_name);
      return redirect(routes.Application.index()).addingToSession(request, "username", user_name);
    }
  }

  public Result index(Http.Request request) {
    return ok(index.render(request, messagesApi.preferred(request)));
  }
  
  public Result signup(Http.Request request) {
    if(Util.isAuthenticated(request)) {
      return badRequest("Bereits angemeldet.");
    }

    if(!config.getBoolean("signup.activated")) {
      return badRequest("Die Registrierung ist momentan deaktiviert.");
    }

    Form<Signup> signup_form = formFactory.form(Signup.class);
    
    return ok(signup.render(signup_form, request, messagesApi.preferred(request)));
  }
  
  public Result createUser(Http.Request request) {
    if(!config.getBoolean("signup.activated")) {
      return badRequest("Die Registrierung ist momentan deaktiviert.");
    }

    Form<Signup> signup_form = formFactory.form(Signup.class).bindFromRequest(request);

    if (signup_form.hasErrors()) {
      return badRequest(signup.render(signup_form, request, messagesApi.preferred(request)));
    } else {
      String userName = signup_form.get().username.toLowerCase();
      String password = signup_form.get().password;
      
      User user = User.create(userName, password);

      return redirect(routes.Application.index()).addingToSession(request, "username", userName);
    }
  }

  public Result login(Http.Request request) {
    Form<Login> login_form = formFactory.form(Login.class);

    return ok(login.render(login_form, request, messagesApi.preferred(request)));
  }

  public Result logout() {
    return redirect(routes.Application.index()).withNewSession();
  }
  
  @Security.Authenticated(Secured.class)
  public Result user(Http.Request request) {
    Form<User> user_form = formFactory.form(User.class);

    return ok(user_profile.render(user_form.fill(Util.getUser(request)), request, messagesApi.preferred(request)));
  }
  
  @Security.Authenticated(Secured.class)
  public Result password(Http.Request request) {
    Form<ChangePassword> change_password_form = formFactory.form(ChangePassword.class);

    return ok(change_password.render(change_password_form, request, messagesApi.preferred(request)));
  }
  
  @Security.Authenticated(Secured.class)
  public Result changePassword(Http.Request request) {
    Form<ChangePassword> form = formFactory.form(ChangePassword.class).bindFromRequest(request);
    
    if (form.hasErrors()) {
      return redirect(routes.Application.changePassword())
          .flashing("success", "Fehler beim Ändern des Passwortes.");
    } else {
      ChangePassword changePassword = form.get();
      User user = Util.getUser(request);
      user.changePassword(changePassword.password);
      
      return redirect(routes.Application.changePassword())
          .flashing("success", "Das Passwort wurde geändert.");
    }
  }
  
  public Result userEdit(Http.Request request) {
    Form<User> form = formFactory.form(User.class).bindFromRequest(request);

    if (form.hasErrors()) {
      return redirect(routes.Application.user())
          .flashing("success", "Fehler");
    } else {
      User user = form.get();
      User oldUser = Util.getUser(request);
      //upper case usernames are not allowed
      user.setName(user.getName().toLowerCase());
      user.setId(oldUser.getId());
      user.update();
      
      //logout if user has changed
      if(!oldUser.getName().equals(user.getName())) {
        return redirect(routes.Application.logout())
            .flashing("success", "Benutzername geändert. Bitte melden Sie sich erneut an.");
      }
      
      return redirect(routes.Application.user())
          .flashing("success", "gespeichert");
    }
  }
  
  public Result imprint(Http.Request request) {
    String name = config.getString("owner.name");
    String street = config.getString("owner.street");
    String city = config.getString("owner.city");
    String email = config.getString("owner.email");
    String email_encoded = "";
    for (int i = 0; i < email.length(); i++) {
      email_encoded += ("&#" + email.codePointAt(i) + ";");
    }
    
    return ok(imprint.render(name, street, city, email_encoded, request));
  }
  
  public Result privacy(Http.Request request) {
    String name = config.getString("owner.name");
    String street = config.getString("owner.street");
    String city = config.getString("owner.city");
    String country = config.getString("privacy.country");
    String email = config.getString("privacy.email");
    String email_encoded = "";
    for (int i = 0; i < email.length(); i++) {
      email_encoded += ("&#" + email.codePointAt(i) + ";");
    }
    
    return ok(privacy.render(name, street, city, country, email_encoded, request));
  }
}