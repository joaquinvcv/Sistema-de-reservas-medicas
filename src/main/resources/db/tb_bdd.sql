CREATE TABLE IF NOT EXISTS paciente(
    rut_paciente VARCHAR(20) NOT NULL, 
    nombre VARCHAR(50) NOT NULL, 
    apellido VARCHAR(50) NOT NULL, 
    edad INT NOT NULL, 
    email VARCHAR(50), 
    n_telefono INT NOT NULL, 
    tratamiento BOOLEAN NOT NULL,
    prevision BOOLEAN NOT NULL,
    pagocita BOOLEAN NOT NULL,
    PRIMARY KEY (rut_paciente)
);

CREATE TABLE IF NOT EXISTS Especialidad(
	id_especialidad INT NOT NULL,
    nombre_especialidad VARCHAR(30) NOT NULL,
    clave_especialidad INT NOT NULL,
    costo_especialidad INT NOT NULL,
    info_especialidad VARCHAR(2000) NOT NULL,
    PRIMARY KEY (id_especialidad)
);

CREATE TABLE IF NOT EXISTS Profesional_salud(
    rut_profesional VARCHAR(20) NOT NULL,
 	nombre VARCHAR(30) NOT NULL,
    apellido VARCHAR(30) NOT NULL,
    email VARCHAR(30),
    telefono INT NOT NULL,
    cargo VARCHAR(30) NOT NULL,
    id_especialidad INT NOT NULL,
    PRIMARY KEY (rut_profesional)
);

CREATE TABLE IF NOT EXISTS Horario_laboral(
    id_horario INT NOT NULL,
    cupos_diarios INT NOT NULL,
    rut_profesional VARCHAR(20) NOT NULL,
    PRIMARY KEY(id_horario)
);
 
CREATE TABLE IF NOT EXISTS Corresponde(
    id int NOT NULL,
    id_horario INT NOT NULL,
    id_cita INT,
    hora_inicio TIME NOT NULL,
    hora_termino TIME NOT NULL,
    ocupado BOOLEAN,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Cita_medica(
    id_cita INT NOT NULL,
    fecha_atencion DATE NOT NULL,
    comentario VARCHAR(130),
    sala_atencion VARCHAR(30) NOT NULL,
    costo FLOAT,
    rut_paciente VARCHAR(20) NOT NULL,
    rut_profesional VARCHAR(20) NOT NULL,
    PRIMARY KEY(id_cita)
);

ALTER TABLE cita_medica ADD FOREIGN KEY (rut_paciente) REFERENCES paciente(rut_paciente); 
ALTER TABLE cita_medica ADD FOREIGN KEY (rut_profesional) REFERENCES profesional_salud(rut_profesional);

ALTER TABLE corresponde ADD FOREIGN KEY (id_horario) REFERENCES horario_laboral(id_horario);
ALTER TABLE corresponde ADD FOREIGN KEY (id_cita) REFERENCES cita_medica(id_cita);

ALTER TABLE horario_laboral ADD FOREIGN KEY (rut_profesional) REFERENCES profesional_salud(rut_profesional);

ALTER TABLE profesional_salud ADD FOREIGN KEY (id_especialidad) REFERENCES especialidad(id_especialidad);




