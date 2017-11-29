package models;

import java.security.SecureRandom;

import play.Configuration;
import play.api.Play;
import play.mvc.Http.Context;

public class Util {
  public static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  public static SecureRandom rnd = new SecureRandom();
  
  public static User getUser() {
    String user_name = Context.current().session().get("username");
    
    return User.find.query().where().eq("name", user_name).findUnique();
  }
  
  public static Boolean isAuthenticated() {
    if (Util.getUser() == null) {
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
  
  public static String getConfigValueByKey(String key) {
    Configuration config = Play.current().injector().instanceOf(Configuration.class);
    
    return config.getString(key);
  }
}