# --- !Ups
ALTER TABLE harvest_record ADD COLUMN water_content float;

# --- !Downs
ALTER TABLE harvest_record DROP COLUMN water_content;
