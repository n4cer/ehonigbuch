package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.mindrot.jbcrypt.BCrypt;

import io.ebean.*;

import play.data.validation.Constraints;
import play.data.validation.Constraints.MinLength;

@Entity
@Table(name = "beekeepers")
public class User extends Model {
  @Id
  private Long id;
  @Constraints.Required
  @Column(unique = true)
  @MinLength(4)
  private String name;
  private String firstName;
  private String lastName;
  private String street;
  private String zipCode;
  private String city;
  @Constraints.Email
  @Column(unique = true)
  private String email;
  private String telephone;
  private String passwordHash;
  @Column(columnDefinition = "boolean default false")
  private Boolean visible;
  @Column(columnDefinition = "boolean default true")
  private Boolean active;
  private String registrationNumber;

  public static final Finder<Long, User> find = new Finder<Long, User>(User.class);

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

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
    User user = User.find.query().where().eq("name", userName).findOne();
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
