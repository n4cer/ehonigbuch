package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import io.ebean.*;
import play.data.validation.Constraints;

@Entity
public class HarvestType extends Model {
  @Id
  private Long id;
  @Constraints.Required
  private String typeId;
  @Constraints.Required
  private String name;

  public static final Finder<Long, HarvestType> find = new Finder<>(HarvestType.class);

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTypeId() {
    return typeId;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name.toString();
  }
}