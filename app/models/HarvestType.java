package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import io.ebean.*;
import play.data.validation.Constraints;

@Entity
public class HarvestType extends Model {

  public static Finder<Long, HarvestType> find = new Finder<>(HarvestType.class);
  @Id
  public Long id;
  @Constraints.Required
  public String typeId;
  @Constraints.Required
  public String name;
  
  @Override
  public String toString() {
    return name.toString();
  }
}