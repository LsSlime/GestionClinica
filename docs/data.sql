insert into administradores (email, dni, nombre) values
    ('admin@example.com', '12345678A', 'Admin Principal');

INSERT INTO `especialidades` (`id`, `nombre`, `imagen_url`) VALUES
(1, 'Medico General', '/images/especialidades/701a2867-cf84-4887-bf83-b9b177447393.jpg'),
(2, 'Oftamologo', '/images/especialidades/b242aae9-9d5d-4d42-9cdc-6aae98eec797.jpg'),
(3, 'Pediatra', '/images/especialidades/d6023806-4abb-477c-b617-eba38718dc2c.jpg'),
(4, 'Enfermeria', '/images/especialidades/e442af71-a63d-46f1-9423-0b8d74e61f80.jpg'),
(5, 'Odontologo', '/images/especialidades/a583fc65-9fe1-4ff2-8c17-4b85ba9718eb.jpg');


INSERT INTO medicos (`id`, `dni`, `nombre`, `apellido1`, `apellido2`, `telefono`, `email`, `genero`, `id_especialidad`, `numeroColegiado`) VALUES
(1, '12345A', 'Medico General', 'Medicado', '', '2174124', 'medico1@gmail.com', 'Masculino', 1, 'PE12091241');


INSERT INTO pacientes (`id`, `dni`, `nombre`, `apellido1`, `apellido2`, `fecha_nacimiento`, `genero`, `telefono`, `email`, `direccion`) VALUES
(1, '1115A', 'Paciente General', 'Pacientado', '', '1945-10-13', 'Masculino', '122151251', 'paciente1@example.com', 'Calle de los pacientados');

INSERT INTO citas (`id`, `id_paciente`, `id_medico`, `fecha_cita`, `motivo`, `descripcion`, `estado`, `diagnostico`, `tratamiento`, `observaciones`) VALUES
(1, 1, 1, '2026-04-17 17:09:00', 'Me duele la cabeza', 'presento dolor de cabeza severo en la parte posterior', 'completada', 'Mucha pc', 'ir al psicologo', 'paracetamol');
