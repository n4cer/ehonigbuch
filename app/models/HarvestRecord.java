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

  public static Finder<Long, HarvestRecord> find = new Finder<>(HarvestRecord.class);
  @Id
  public Long id;
  public Integer number;
  public Date date;
  
  public String description;
  public Float weight;
  public Float waterContent;
  @ManyToOne
  public User user;
  @OneToMany(mappedBy="harvestRecord", cascade=CascadeType.ALL)
  @OrderBy("date asc, number asc")
  public List<BottlingRecord> bottlingRecords;
  @ManyToOne
  @Constraints.Required
  public HarvestType harvestType;
  
  @Override
  public String toString() {
    String year = "";
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    
    if(date == null) {
      year = df.format(new Date());
    } else {
      year = df.format(date);
    }
    
    return harvestType.typeId + "_" + number + "_" + year;
  }
}