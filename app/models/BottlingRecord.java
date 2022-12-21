package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.ebean.*;

@Entity
public class BottlingRecord extends Model {
  @Id
  private Long id;
  private Integer number;
  private String amount;
  private Date date;
  private Integer labelNumberFrom;
  private Integer labelNumberTo;
  @ManyToOne
  private HarvestRecord harvestRecord;

  public static final Finder<Long, BottlingRecord> find = new Finder<Long, BottlingRecord>(BottlingRecord.class);

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

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Integer getLabelNumberFrom() {
    return labelNumberFrom;
  }

  public void setLabelNumberFrom(Integer labelNumberFrom) {
    this.labelNumberFrom = labelNumberFrom;
  }

  public Integer getLabelNumberTo() {
    return labelNumberTo;
  }

  public void setLabelNumberTo(Integer labelNumberTo) {
    this.labelNumberTo = labelNumberTo;
  }

  public HarvestRecord getHarvestRecord() {
    return harvestRecord;
  }

  public void setHarvestRecord(HarvestRecord harvestRecord) {
    this.harvestRecord = harvestRecord;
  }

  @Override
  public String toString() {
    return number.toString();
  }
}