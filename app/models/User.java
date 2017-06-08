package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.Model;

import play.data.validation.Constraints;
import play.data.validation.Constraints.MinLength;

@Entity
@Table(name = "beekeepers")
public class User extends Model {

  public static Finder<Long, User> find = new Finder<Long, User>(User.class);
  @Id
  public Long id;
  @Constraints.Required
  @Column(unique = true)
  @MinLength(4)
  public String name;
  public String firstName;
  public String lastName;
  public String street;
  public String zipCode;
  public String city;
  @Constraints.Email
  @Column(unique = true)
  public String email;
  public String telephone;
  public String passwordHash;
  @Column(columnDefinition = "boolean default false")
  public Boolean visible;
  @Column(columnDefinition = "boolean default true")
  public Boolean active;
  public String registrationNumber;

  @Override
  public String toString() {
    return name;
  }

  /**
   * Checks username and password
   * 
   * @param userName
   * @param password
   * @return User
   */
  public static User authenticate(String userName, String password) {
    User user = User.find.where().eq("name", userName).findUnique();
    if (user != null && BCrypt.checkpw(password, user.passwordHash)) {
      return user;
    } else {
      return null;
    }
  }

  /**
   * Create new user with password hash
   * 
   * @param userName
   * @param password
   * @return User
   */
  public static User create(String userName, String password) {
    User user = new User();
    user.name = userName;
    user.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    user.visible = false;
    user.active = true;
    user.save();
    
    return user;
  }
  
  public void changePassword(String password) {
    this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    this.save();
  }
}
