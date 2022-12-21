package models;

import java.security.SecureRandom;

import play.api.Play;
import play.mvc.Http;

public class Util {
  public static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  public static SecureRandom rnd = new SecureRandom();
  
  public static User getUser(Http.Request request) {
    if (request.session().get("username").isPresent()) {
      String user_name = request.session().get("username").get();

      return User.find.query().where().eq("name", user_name).findOne();
    }

    return null;
  }
  
  public static Boolean isAuthenticated(Http.Request request) {
    if (Util.getUser(request) == null) {
      return false;
    }
    
    return true;
  }
  
  public static String rndUrl(int len) {
   StringBuilder sb = new StringBuilder( len );
   
   for( int i = 0; i < len; i++ ) 
      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
   
   return sb.toString();
  }
  
  public static String BooleanToGlyphIcon(Boolean b) {
    if(b) {
      return "<span class=\"glyphicon glyphicon-ok icon-success\"></span>";
    }
    
    return "<span class=\"glyphicon glyphicon-remove icon-error\"></span>";
  }
}