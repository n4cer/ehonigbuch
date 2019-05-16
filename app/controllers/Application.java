package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.Column;

import org.apache.commons.lang3.StringEscapeUtils;

import models.User;
import models.Util;
import net.glxn.qrgen.javase.QRCode;
import play.Logger;
import play.api.Configuration;
import play.api.Play;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Validate;
import play.data.validation.Constraints.Validatable;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

public class Application extends Controller {
  @Validate
  public static class Login implements Validatable<String> {
    @Constraints.Required
    public String username;
    @Constraints.Required
    public String password;
    
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
    public String username;
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
      
      User user = User.find.query().where().eq("name", username).findUnique();
      if(user != null) {
        return "Der Benutzer existiert bereits.";
      }
      
      return null;
    }
  }
  
  @Inject FormFactory formFactory;
  @Inject Configuration configuration;

  public Result authenticate() {
    Form<Login> login_form = formFactory.form(Login.class).bindFromRequest();

    if (login_form.hasErrors()) {
      return badRequest(login.render(login_form));
    } else {
      String user_name = login_form.get().username.toLowerCase();
      session("username", user_name);
      Logger.info("Login: " + user_name);
      return redirect(routes.Application.index());
    }
  }

  public Result index() {
    return ok(index.render());
  }
  
  public Result signup() {
    if(Util.isAuthenticated()) {
      return badRequest("Bereits angemeldet.");
    }

    if(!configuration.underlying().getBoolean("signup.activated")) {
      return badRequest("Die Registrierung ist momentan deaktiviert.");
    }

    Form<Signup> signup_form = formFactory.form(Signup.class);
    
    return ok(signup.render(signup_form));
  }
  
  public Result createUser() {
    if(!configuration.underlying().getBoolean("signup.activated")) {
      return badRequest("Die Registrierung ist momentan deaktiviert.");
    }

    Form<Signup> signup_form = formFactory.form(Signup.class).bindFromRequest();

    if (signup_form.hasErrors()) {
      return badRequest(signup.render(signup_form));
    } else {
      String userName = signup_form.get().username.toLowerCase();
      String password = signup_form.get().password;
      
      User user = User.create(userName, password);
      session("username", userName);
      
      return redirect(routes.Application.index());
    }
  }

  public Result login() {
    Form<Login> login_form = formFactory.form(Login.class);

    return ok(login.render(login_form));
  }

  public Result logout() {
    session().clear();
    return redirect(routes.Application.index());
  }
  
  @Security.Authenticated(Secured.class)
  public Result user() {
    Form<User> user_form = formFactory.form(User.class);

    return ok(user_profile.render(user_form.fill(Util.getUser())));
  }
  
  @Security.Authenticated(Secured.class)
  public Result password() {
    Form<ChangePassword> change_password_form = formFactory.form(ChangePassword.class);

    return ok(change_password.render(change_password_form));
  }
  
  @Security.Authenticated(Secured.class)
  public Result changePassword() {
    Form<ChangePassword> form = formFactory.form(ChangePassword.class).bindFromRequest();
    
    if (form.hasErrors()) {
      flash("success", "Fehler beim Ändern des Passwortes.");
      return badRequest(change_password.render(form));
    } else {
      ChangePassword changePassword = form.get();
      User user = Util.getUser();
      user.changePassword(changePassword.password);
      
      flash("success", "Das Passwort wurde geändert.");
      
      return ok(change_password.render(form));
    }
  }
  
  public Result userEdit() {
    Form<User> form = formFactory.form(User.class).bindFromRequest();

    if (form.hasErrors()) {
      flash("success", "Fehler");
      return badRequest(user_profile.render(form));
    } else {
      User user = form.get();
      User oldUser = Util.getUser();
      //upper case usernames are not allowed
      user.name = user.name.toLowerCase();
      user.id = oldUser.id;
      user.update();
      
      //logout if user has changed
      if(!oldUser.name.equals(user.name)) {
        flash("success", "Benutzername geändert. Bitte melden Sie sich erneut an.");
        return redirect(routes.Application.logout());
      }
      
      flash("success", "gespeichert");
      
      return ok(user_profile.render(form));
    }
  }
  
  public Result imprint() {
    String name = configuration.underlying().getString("owner.name");
    String street = configuration.underlying().getString("owner.street");
    String city = configuration.underlying().getString("owner.city");
    String email = configuration.underlying().getString("owner.email");
    String email_encoded = "";
    for (int i = 0; i < email.length(); i++) {
      email_encoded += ("&#" + email.codePointAt(i) + ";");
    }
    
    return ok(imprint.render(name, street, city, email_encoded));
  }
  
  public Result privacy() {
    String name = configuration.underlying().getString("owner.name");
    String street = configuration.underlying().getString("owner.street");
    String city = configuration.underlying().getString("owner.city");
    String country = configuration.underlying().getString("privacy.country");
    String email = configuration.underlying().getString("privacy.email");
    String email_encoded = "";
    for (int i = 0; i < email.length(); i++) {
      email_encoded += ("&#" + email.codePointAt(i) + ";");
    }
    
    return ok(privacy.render(name, street, city, country, email_encoded));
  }
}