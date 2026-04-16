create database if not exists gestordesalud;
use gestordesalud;


create table if not exists administradores (
    id int not null auto_increment,
    email varchar(30) not null,
    dni varchar(9) not null,
    nombre varchar(30) not null,
    constraint pk_administradores primary key (id),
    constraint unq_administradores_dni unique (dni),
    constraint unq_administradores_email unique (email)
);

create table especialidades (
    id int not null auto_increment,
    nombre varchar(50) not null,
    imagen_url varchar(255) default null,
    constraint pk_especialidades primary key (id),
    constraint unq_especialidades_nombre unique (nombre)
);


create table if not exists pacientes (
    id int not null auto_increment,
    dni varchar(9) not null,
    nombre varchar(30) not null,
    apellido1 varchar(30) not null,
    apellido2 varchar(30),
    fecha_nacimiento date not null,
    genero enum('Masculino', 'Femenino') not null,
    telefono varchar(20) default null,
    email varchar(50),
    direccion varchar(100),
    constraint pk_pacientes primary key (id),
    constraint unq_pacientes_dni unique (dni),
    constraint unq_pacientes_email unique (email)
);


create table if not exists medicos (
    id int auto_increment,
    dni varchar(9) not null,
    nombre varchar(30) not null,
    apellido1 varchar(30) not null,
    apellido2 varchar(30),
    telefono varchar(20),
    email varchar(60),
    genero enum('Masculino', 'Femenino') not null,
    id_especialidad int(11) not null,
    numeroColegiado varchar(30) not null unique,
    constraint pk_medicos primary key (id),
    constraint unq_medicos_dni unique (dni),
    constraint unq_medicos_email unique (email),
    constraint fk_medicos_especialidades foreign key (id_especialidad) references especialidades(id)
    on update cascade on delete restrict
);


create table if not exists citas (
    id int not null auto_increment,
    id_paciente int not null,
    id_medico int not null,
    fecha_cita datetime not null,
    motivo varchar(255) not null,
    descripcion text default null,
    estado enum('programada', 'completada', 'cancelada') not null default 'programada',
    diagnostico text default null,
    tratamiento text default null,
    observaciones text default null,
    constraint pk_citas primary key (id),
    constraint fk_citas_pacientes
        foreign key (id_paciente) references pacientes(id)
            on update cascade on delete restrict,
    constraint fk_citas_medicos
        foreign key (id_medico) references medicos(id)
            on update cascade on delete restrict

);

