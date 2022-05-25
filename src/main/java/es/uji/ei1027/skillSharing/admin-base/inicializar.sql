DROP TABLE colaboracion;
DROP TABLE demanda;
DROP TABLE oferta;
DROP TABLE skill;
DROP TABLE usuario;
DROP TABLE estudiante;



CREATE TABLE estudiante(
                           nif		    CHAR(9),
                           nombre		VARCHAR(20) NOT NULL,
                           apellido	    VARCHAR(20) NOT NULL,
                           email		VARCHAR(20) NOT NULL,
                           grado		VARCHAR(50) NOT NULL,
                           edad		    INTEGER NOT NULL,
                           sexo		    VARCHAR(10) NOT NULL,
                           direccion	VARCHAR(50) NOT NULL,
                           horas		INTEGER NOT NULL,
                           rutaimg      VARCHAR(150),
                           CONSTRAINT cp_estudiante PRIMARY KEY (nif),
                           CONSTRAINT calt_estudiante UNIQUE(email),
                           CONSTRAINT ri_estudiante_edad CHECK (edad > 16)
);

CREATE TABLE skill(
                      id_skill      SERIAL PRIMARY KEY,
                      nombre		VARCHAR(20),
                      activo		BOOLEAN NOT NULL,
                      nivel         VARCHAR(20) NOT NULL,
                      descripcion   VARCHAR(200),
                      rutaim        VARCHAR(150)
);

CREATE TABLE oferta(
                       id_oferta	SERIAL PRIMARY KEY,
                       estudiante	CHAR(9) NOT NULL,
                       horas		INTEGER NOT NULL,
                       ini_fecha	DATE NOT NULL,
                       fin_fecha	DATE NOT NULL,
                       activa		BOOLEAN NOT NULL,
                       id_skill	    SERIAL NOT NULL,
                       descripcion	VARCHAR(200),
                       CONSTRAINT ca_oferta_sn FOREIGN KEY (id_skill) REFERENCES skill(id_skill) ON DELETE RESTRICT ON UPDATE CASCADE,
                       CONSTRAINT ca_oferta_estudiante FOREIGN KEY (estudiante) REFERENCES estudiante(nif) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TABLE demanda(
                        id_demanda      SERIAL PRIMARY KEY,
                        estudiante      CHAR(9) NOT NULL,
                        horas           INTEGER NOT NULL,
                        ini_fecha       DATE NOT NULL,
                        fin_fecha       DATE NOT NULL,
                        activa          BOOLEAN NOT NULL,
                        id_skill        SERIAL NOT NULL,
                        descripcion     VARCHAR(200),
                        CONSTRAINT ca_demanda_sn FOREIGN KEY (id_skill) REFERENCES skill(id_skill) ON DELETE RESTRICT ON UPDATE CASCADE,
                        CONSTRAINT ca_demanda_estudiante FOREIGN KEY (estudiante) REFERENCES estudiante(nif) ON DELETE RESTRICT ON UPDATE CASCADE

);


CREATE TABLE colaboracion(
                             id_colaboracion	SERIAL PRIMARY KEY,
                             id_oferta       SERIAL NOT NULL,
                             id_demanda	    SERIAL NOT NULL,
                             ini_fecha       DATE NOT NULL,
                             fin_fecha       DATE  NULL,
                             activa          BOOLEAN NOT NULL,
                             rate		     INTEGER NULL,
                             comentario	VARCHAR(200),
                             horas		INTEGER NULL,
                             CONSTRAINT ca_colaboracion_id_demanda FOREIGN KEY (id_demanda) REFERENCES demanda(id_demanda) ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT ca_colaboracion_id_oferta FOREIGN  KEY (id_oferta) REFERENCES oferta(id_oferta) ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT ri_colaboracion_rate CHECK(rate IS NULL OR rate BETWEEN 1 AND 5 )
);
                             --> Diría que falta poner que si la colaboración pasa de fecha_fin respecto a la actual, se cambie el estado de activa = FALSE
CREATE TABLE usuario(
        username    VARCHAR(20),
        password    VARCHAR(200) NOT NULL,
        skp         BOOLEAN NOT NULL,
        active      BOOLEAN NOT NULL,
        nif         CHAR(9) NOT NULL,
        descripcion VARCHAR(200) NULL,
        CONSTRAINT cp_usuario PRIMARY KEY (username),
        CONSTRAINT ca_usuario_es FOREIGN KEY (nif) REFERENCES estudiante(nif) ON DELETE RESTRICT ON UPDATE CASCADE
);
