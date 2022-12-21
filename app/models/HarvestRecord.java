package models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import io.ebean.*;
import play.data.validation.Constraints;

@Entity
public class HarvestRecord extends Model {
  @Id
  private Long id;
  private Integer number;
  private Date date;

  private String description;
  private Float weight;
  private Float waterContent;
  @ManyToOne
  private User user;
  @OneToMany(mappedBy="harvestRecord", cascade=CascadeType.ALL)
  @OrderBy("date asc, number asc")
  private List<BottlingRecord> bottlingRecords;
  @ManyToOne
  @Constraints.Required
  private HarvestType harvestType;

  public static final Finder<Long, HarvestRecord> find = new Finder<>(HarvestRecord.class);

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Float getWeight() {
    return weight;
  }

  public void setWeight(Float weight) {
    this.weight = weight;
  }

  public Float getWaterContent() {
    return waterContent;
  }

  public void setWaterContent(Float waterContent) {
    this.waterContent = waterContent;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<BottlingRecord> getBottlingRecords() {
    return bottlingRecords;
  }

  public void setBottlingRecords(List<BottlingRecord> bottlingRecords) {
    this.bottlingRecords = bottlingRecords;
  }

  public HarvestType getHarvestType() {
    return harvestType;
  }

  public void setHarvestType(HarvestType harvestType) {
    this.harvestType = harvestType;
  }

  @Override
  public String toString() {
    String year = "";
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    
    if(date == null) {
      year = df.format(new Date());
    } else {
      year = df.format(date);
    }
    
    return harvestType.getTypeId() + "_" + number + "_" + year;
  }
}