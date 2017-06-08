package controllers;

import java.util.List;

import javax.inject.Inject;

import models.User;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.users.*;

@Security.Authenticated(Secured.class)
public class Users extends Controller {
  @Inject FormFactory formFactory;
  
  public Result index() {
    List<User> users = User.find.all();
    
    return ok(index.render(users));
  }
}