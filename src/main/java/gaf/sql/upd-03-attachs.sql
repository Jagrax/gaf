CREATE TABLE ATTACHMENTS (
  ID INT NOT NULL AUTO_INCREMENT,
  PATH VARCHAR(255) NOT NULL,
  FILE_NAME VARCHAR(100) NOT NULL,
  CORTE_ID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (CORTE_ID) REFERENCES CORTES(ID)
);

ALTER TABLE TALLES ADD COLUMN finalization_date DATE NULL;