-- Paciente
insert into Paciente (rut_paciente, nombre, apellido, edad, email, n_telefono, tratamiento, prevision,pagocita)
values ('12.345.678-9', 'Pedro', 'Mujica', 50, 'pmujica@gmail.com', 985761234, true, true,true);
insert into Paciente (rut_paciente, nombre, apellido, edad, email, n_telefono, tratamiento, prevision,pagocita)
values ('12.345.678-8', 'Martina', 'Rojas', 20, 'mrojas@gmail.com', 985761222, true, false,true);
insert into Paciente (rut_paciente, nombre, apellido, edad, email, n_telefono, tratamiento, prevision,pagocita)
values ('18.355.556-7', 'Barbara', 'Contreras', 30, 'bcontreras@gmail.com', 985766666, true, false,true);
insert into Paciente (rut_paciente, nombre, apellido, edad, email, n_telefono, tratamiento, prevision,pagocita)
values ('19.333.355-6', 'Francisca', 'Reyes', 25, 'freyes@gmail.com', 985764444, true, false,true);
insert into Paciente (rut_paciente, nombre, apellido, edad, email, n_telefono, tratamiento, prevision,pagocita)
values ('12.356.565-5', 'Jose', 'Martinez', 50, 'jmartinez@gmail.com', 985777777, true, true,true);
insert into Paciente (rut_paciente, nombre, apellido, edad, email, n_telefono, tratamiento, prevision,pagocita)
values ('12.356.565-4', 'Rut', 'Sandoval', 65, NULL, 954863147, false, true,true);


-- Especialiadad
insert into Especialidad (id_especialidad, nombre_especialidad, clave_especialidad, costo_especialidad, info_especialidad)
values (1, 'Odontologia', 1501, 30000, 'Blanqueamiento dental, Ortodoncia Bracket, Carillas bucales y dentales, Operacion Maxilofacial');
insert into Especialidad (id_especialidad, nombre_especialidad, clave_especialidad, costo_especialidad, info_especialidad)
values (2,'Cardiologia', 1502, 50000, 'Cateterismo Cardiaco, Ecocardiografia (de Esfuerzo y Trasensofagica), IRM Cardiaca');
insert into Especialidad (id_especialidad, nombre_especialidad, clave_especialidad, costo_especialidad, info_especialidad)
values (3,'Oncologia', 1503, 70000, 'Radioterapia, Inmunoterapia, Quimioterapia, Pruebas de biomarcadores');
insert into Especialidad (id_especialidad, nombre_especialidad, clave_especialidad, costo_especialidad, info_especialidad)
values (4,'Reumatologia', 1504, 45000, '');
insert into Especialidad (id_especialidad, nombre_especialidad, clave_especialidad, costo_especialidad, info_especialidad)
values (5,'Pediatria', 1505, 30000, '');
insert into Especialidad (id_especialidad, nombre_especialidad, clave_especialidad, costo_especialidad, info_especialidad)
values (6,'Psiquiatria', 1506, 60000, '');


-- Profesional
insert into Profesional_salud (rut_profesional, nombre, apellido, email, telefono, cargo, id_especialidad) 
values ('9.453.621-8', 'Juan', 'Riquelme', 'jriquelme@gmail.com', 964578213, 'Dentista', 1);
insert into Profesional_salud (rut_profesional, nombre, apellido, email, telefono, cargo, id_especialidad) 
values ('9.453.621-7', 'Julia', 'Sandoval', 'jsandoval@gmail.com', 961457832, 'Psicologa', 6);
insert into Profesional_salud (rut_profesional, nombre, apellido, email, telefono, cargo, id_especialidad) 
values ('9.453.621-6', 'Simon', 'Contreras', 'scontreras@gmail.com', 947856356, 'Pediatra', 5);
insert into Profesional_salud (rut_profesional, nombre, apellido, email, telefono, cargo, id_especialidad) 
values ('9.453.621-5', 'Daniela', 'Ruiz', 'druiz@gmail.com', 978546832, 'Reumatologa', 4);
insert into Profesional_salud (rut_profesional, nombre, apellido, email, telefono, cargo, id_especialidad) 
values ('9.453.621-4', 'Omar', 'Garcia', 'ogarcia@gmail.com', 945832664, 'Oncologo', 3);
insert into Profesional_salud (rut_profesional, nombre, apellido, email, telefono, cargo, id_especialidad) 
values ('9.453.621-3', 'Sebastian', 'Villablanca', 'svillablanca@gmail.com', 987453216,'Cardiologo', 2);


-- Horario_laboral
insert into Horario_laboral (id_horario, cupos_diarios, rut_profesional)
values (1, 6, '9.453.621-8');
insert into Horario_laboral (id_horario, cupos_diarios, rut_profesional)
values (2, 5, '9.453.621-7');
insert into Horario_laboral (id_horario, cupos_diarios, rut_profesional)
values (3, 6, '9.453.621-6');
insert into Horario_laboral (id_horario, cupos_diarios, rut_profesional)
values (4, 4,'9.453.621-5');
insert into Horario_laboral (id_horario, cupos_diarios, rut_profesional)
values (5, 5,'9.453.621-4');
insert into Horario_laboral (id_horario, cupos_diarios, rut_profesional)
values (6, 4,'9.453.621-3');


-- Cita Medica
insert into Cita_medica (id_cita, fecha_atencion, comentario, sala_atencion, costo, rut_paciente, rut_profesional)
VALUES (1, '2022-11-08', 'Tengo dolor en una muela', 'Sala 3', 13789, '12.345.678-9', '9.453.621-8');
insert into Cita_medica (id_cita, fecha_atencion, comentario, sala_atencion, costo, rut_paciente, rut_profesional)
values (2,'2022-11-08', 'Me duelen las rodillas', 'Sala 4', 25643, '12.345.678-8', '9.453.621-5');
insert into Cita_medica (id_cita, fecha_atencion, comentario, sala_atencion, costo, rut_paciente, rut_profesional)
values (3,'2022-11-08', 'No siento las piernas', 'Sala 4', 30500, '18.355.556-7', '9.453.621-5');
insert into Cita_medica (id_cita, fecha_atencion, comentario, sala_atencion, costo, rut_paciente, rut_profesional)
values (4,'2022-11-08', 'Vengo a un control para mi hijo', 'Sala 9', 14750, '19.333.355-6', '9.453.621-6');
insert into Cita_medica (id_cita, fecha_atencion, comentario, sala_atencion, costo, rut_paciente, rut_profesional)
values (5,'2022-11-08', NULL, 'Sala 2', 30000, '12.356.565-5', '9.453.621-7');
insert into Cita_medica (id_cita, fecha_atencion, comentario, sala_atencion, costo, rut_paciente, rut_profesional)
values (6,'2022-11-08', NULL, 'Sala 1', 50000, '12.356.565-5', '9.453.621-4');
insert into Cita_medica (id_cita, fecha_atencion, comentario, sala_atencion, costo, rut_paciente, rut_profesional)
values (7,'2022-11-08', NULL, 'Sala 5', 20000, '12.356.565-4', '9.453.621-3');

-- Corresponde
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (1, 1, 1, '09:30:00', '10:30:00', true);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (2, 1, NULL, '10:30:00', '11:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (3, 1, NULL, '11:30:00', '12:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (4, 1, NULL, '13:30:00', '14:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (5, 1, NULL, '14:30:00', '15:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (6, 1, NULL, '15:30:00', '16:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (7, 2, 5, '09:30:00', '10:30:00', true);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (8, 2, NULL, '10:30:00', '11:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (9, 2, NULL, '11:30:00', '12:30:00', false );
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (10, 2, NULL, '13:30:00', '14:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (11, 2, NULL, '14:30:00', '15:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (12, 3, 4, '09:30:00', '10:30:00', true);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (13, 3, NULL, '10:30:00', '11:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (14, 3, NULL, '11:30:00', '12:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (15, 3, NULL, '13:30:00', '14:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (16, 3, NULL, '14:30:00', '15:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (17, 3, NULL, '15:30:00', '16:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (18, 4, 2, '09:30:00', '10:30:00', true);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (19, 4, 3, '10:30:00', '11:30:00', true);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (20, 4, NULL, '11:30:00', '12:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (21, 4, NULL, '13:30:00', '14:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (22, 5, 6, '09:30:00', '10:30:00', true);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (23, 5, NULL, '10:30:00', '11:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (24, 5, NULL, '11:30:00', '12:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (25, 5, NULL, '13:30:00', '14:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (26, 5, NULL, '14:30:00', '15:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (27, 6, 7, '09:30:00', '10:30:00', true);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (28, 6, NULL, '10:30:00', '11:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (29, 6, NULL, '11:30:00', '12:30:00', false);
insert into corresponde (id, id_horario, id_cita, hora_inicio, hora_termino, ocupado)
values (30, 6, NULL, '13:30:00', '14:30:00', false);