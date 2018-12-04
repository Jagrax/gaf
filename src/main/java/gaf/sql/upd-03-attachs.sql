CREATE TABLE ATTACHMENTS (
  ID INT NOT NULL AUTO_INCREMENT,
  PATH VARCHAR(255) NOT NULL,
  FILE_NAME VARCHAR(100) NOT NULL,
  CORTE_ID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (CORTE_ID) REFERENCES CORTES(ID)
);

ALTER TABLE TALLES ADD COLUMN finalization_date DATE NULL;

CREATE TABLE ROLES (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(25) NOT NULL,
  descripcion VARCHAR(125) NOT NULL
);

INSERT INTO ROLES (name, descripcion) VALUES ('SU', 'Super User. Tiene acceso a todo');
INSERT INTO ROLES (name, descripcion) VALUES ('ADMIN', 'Visibilidad total de la aplicacion');
INSERT INTO ROLES (name, descripcion) VALUES ('OPERARIO', 'Visibilidad de operario');

CREATE TABLE ACCESS (
  id INT NOT NULL AUTO_INCREMENT,
  operador_id INT NOT NULL,
  rol_id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (operador_id) REFERENCES OPERADORES(ID),
  FOREIGN KEY (rol_id) REFERENCES ROLES(ID)
);

/*
INSERT INTO RIGHT_INSTANCES (type, tag, description) VALUES ('ACCESO', 'GAF.CORTES', 'Acceso a la pantalla de cortes');
INSERT INTO RIGHT_INSTANCES (type, tag, description) VALUES ('ACCESO', 'GAF.TALLERES', 'Acceso a la pantalla de talleres');
INSERT INTO RIGHT_INSTANCES (type, tag, description) VALUES ('ACCESO', 'GAF.REPOSICIONES', 'Acceso a la pantalla de reposiciones');
INSERT INTO RIGHT_INSTANCES (type, tag, description) VALUES ('ACCESO', 'GAF.DETALLES_EMPRESA', 'Acceso a la pantalla de detalles de la empresa');
INSERT INTO RIGHT_INSTANCES (type, tag, description) VALUES ('PERFIL', 'ADMIN', 'Perfil con rol de administrador');
INSERT INTO RIGHT_INSTANCES (type, tag, description) VALUES ('PERFIL', 'OPERARIO', 'Perfil con rol de operario');

INSERT INTO RIGHT_PROFILES (profile_id, type, tag, value) VALUES ('ADMIN'   , 'ACCESO', 'GAF.CORTES'            , 'TRUE');
INSERT INTO RIGHT_PROFILES (profile_id, type, tag, value) VALUES ('ADMIN'   , 'ACCESO', 'GAF.TALLERES'          , 'TRUE');
INSERT INTO RIGHT_PROFILES (profile_id, type, tag, value) VALUES ('ADMIN'   , 'ACCESO', 'GAF.REPOSICIONES'      , 'TRUE');
INSERT INTO RIGHT_PROFILES (profile_id, type, tag, value) VALUES ('ADMIN'   , 'ACCESO', 'GAF.DETALLES_EMPRESA'  , 'TRUE');
INSERT INTO RIGHT_PROFILES (profile_id, type, tag, value) VALUES ('OPERARIO', 'ACCESO', 'GAF.CORTES'            , 'TRUE');
INSERT INTO RIGHT_PROFILES (profile_id, type, tag, value) VALUES ('OPERARIO', 'ACCESO', 'GAF.TALLERES'          , 'TRUE');*/