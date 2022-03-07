DROP TABLE colaboracion;
DROP TABLE demanda;
DROP TABLE oferta;
DROP TABLE niveles;
DROP TABLE skill;
DROP TABLE estudiante;


CREATE TABLE estudiante(
	nif		CHAR(9),
	nombre		VARCHAR(20) NOT NULL,
	apellido	VARCHAR(20) NOT NULL,
	email		VARCHAR(20) NOT NULL,
	skp		    BOOLEAN NOT NULL,
	grado		VARCHAR(50) NOT NULL,
	edad		INTEGER NOT NULL,
	sexo		VARCHAR(10) NOT NULL,
	direccion	VARCHAR(50) NOT NULL,
	horas		INTEGER NOT NULL,
	CONSTRAINT cp_estudiante PRIMARY KEY (nif),
	CONSTRAINT ri_estudiante_edad CHECK (edad > 16)
);

CREATE TABLE skill(
	nombre		VARCHAR(20),
	activo		BOOLEAN NOT NULL,
	CONSTRAINT cp_skill PRIMARY KEY (nombre)

);

CREATE TABLE oferta(
	id_oferta	NUMERIC(7, 0),
	estudiante	CHAR(9) NOT NULL,
	horas		INTEGER NOT NULL,
	ini_fecha	DATE NOT NULL,
	fin_fecha	DATE NOT NULL,
	activa		BOOLEAN NOT NULL,
	skill		VARCHAR(20) NOT NULL,
	nivel		VARCHAR(20) NOT NULL,
	descripcion	VARCHAR(200),
	CONSTRAINT cp_oferta PRIMARY KEY (id_oferta),
	CONSTRAINT ca_oferta_sn FOREIGN KEY (skill) REFERENCES skill(nombre) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT ca_oferta_estudiante FOREIGN KEY (estudiante) REFERENCES estudiante(nif) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TABLE demanda(
        id_demanda      NUMERIC(7, 0),
        estudiante      CHAR(9) NOT NULL,
        horas           INTEGER NOT NULL,
        ini_fecha       DATE NOT NULL,
        fin_fecha       DATE NOT NULL,
        activa          BOOLEAN NOT NULL,
        skill           VARCHAR(20) NOT NULL,
        nivel           VARCHAR(20) NOT NULL,
        descripcion     VARCHAR(200),
        CONSTRAINT cp_demanda PRIMARY KEY (id_demanda),
        CONSTRAINT ca_demanda_sn FOREIGN KEY (skill) REFERENCES skill(nombre) ON DELETE RESTRICT ON UPDATE CASCADE,
        CONSTRAINT ca_demanda_estudiante FOREIGN KEY (estudiante) REFERENCES estudiante(nif) ON DELETE RESTRICT ON UPDATE CASCADE

);


CREATE TABLE colaboracion(
	id_oferta	NUMERIC(7,0),
	id_demanda	NUMERIC(7,0) NOT NULL,
    ini_fecha       DATE NOT NULL,
    fin_fecha       DATE  NULL,
    activa          BOOLEAN NOT NULL,
	rate		INTEGER NULL,
	comentario	VARCHAR(200),
	horas		INTEGER NULL,
	CONSTRAINT cp_colaboracion PRIMARY KEY (id_oferta, id_demanda),
	CONSTRAINT ca_colaboracion_id_demanda FOREIGN KEY (id_demanda) REFERENCES demanda(id_demanda) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT ca_colaboracion_id_oferta FOREIGN  KEY (id_oferta) REFERENCES oferta(id_oferta) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT ri_colaboracion_rate CHECK(rate IS NULL OR rate BETWEEN 1 AND 5 )

);
