CREATE DATABASE gafdb;

CREATE TABLE ESTADOS (
	id INT NOT NULL,
	name VARCHAR(20) NOT NULL,
	color VARCHAR(20) NOT NULL,
  entity VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
);

INSERT INTO ESTADOS (id, name, color, entity) VALUES (1, 'Arreglando', 'yellow', 'TALLER');
INSERT INTO ESTADOS (id, name, color, entity) VALUES (2, 'Disponible', 'green', 'TALLER');
INSERT INTO ESTADOS (id, name, color, entity) VALUES (3, 'En deuda', 'blue', 'TALLER');
INSERT INTO ESTADOS (id, name, color, entity) VALUES (4, 'En produccion', 'orange', 'TALLER');
INSERT INTO ESTADOS (id, name, color, entity) VALUES (5, 'No disponible', 'red', 'TALLER');
INSERT INTO ESTADOS (id, name, color, entity) VALUES (6, 'Sin asignar', '', 'CORTE');
INSERT INTO ESTADOS (id, name, color, entity) VALUES (7, 'En produccion', '', 'CORTE');
INSERT INTO ESTADOS (id, name, color, entity) VALUES (8, 'Cerrado con deudas', '', 'CORTE');
INSERT INTO ESTADOS (id, name, color, entity) VALUES (9, 'Finalizado', '', 'CORTE');

CREATE TABLE TALLERES (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	address VARCHAR(255) NOT NULL,
  estado INT NOT NULL,
	primary_phone VARCHAR(20) NOT NULL,
	secondary_phone VARCHAR(20),
	employees_quantity INT,
  score DOUBLE,
	PRIMARY KEY (id),
	FOREIGN KEY (estado) REFERENCES Estados(id)
);

CREATE TABLE CORTES (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  price DOUBLE,
  clothes_quantity INT NOT NULL,
  creation_date DATE NOT NULL,
  due_date DATE NOT NULL,
  from_size DOUBLE,
  to_size DOUBLE,
  hoja_de_corte VARCHAR(255),
  comments VARCHAR(255),
  estado INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (estado) REFERENCES Estados(id)
);

CREATE TABLE TALLES (
  id INT NOT NULL AUTO_INCREMENT,
  size VARCHAR(20),
  quantity INT NOT NULL,
  clothes_delivered INT NOT NULL,
  first_due_date DATE NOT NULL,
  second_due_date DATE NOT NULL,
  comments VARCHAR(500),
  estado INT NOT NULL,
  taller INT,
  corte INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (estado) REFERENCES ESTADOS(id),
  FOREIGN KEY (taller) REFERENCES TALLERES(id),
  FOREIGN KEY (corte) REFERENCES CORTES(id)
);

create table operadores (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(25) NOT NULL,
  password VARCHAR(250) NULL,
  salt VARCHAR(250) NULL,
  descripcion VARCHAR(125) NULL
);