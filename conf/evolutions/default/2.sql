# --- !Ups

ALTER TABLE tShelter ADD paypal varchar(128); 

# --- !Downs

ALTER TABLE tShelter DROP paypal;