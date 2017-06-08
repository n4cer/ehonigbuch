package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class BottlingRecord extends Model {

  public static Finder<Long, BottlingRecord> find = new Finder<Long, BottlingRecord>(BottlingRecord.class);
  @Id
  public Long id;
  public Integer number;
  public String amount;
  public Date date;
  public Integer labelNumberFrom;
  public Integer labelNumberTo;
  @ManyToOne
  public HarvestRecord harvestRecord;
  
  @Override
  public String toString() {
    return number.toString();
  }
}