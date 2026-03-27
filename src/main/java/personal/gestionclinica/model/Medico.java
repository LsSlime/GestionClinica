package personal.gestionclinica.model;


public class Medico {

    private int id, telefono;
    private String dni, nombre, apellido1, apellido2, email, genero;
    private String numeroColegiado;
    private Especialidad especialidad;


    public Medico() {
    }


    public Medico(Especialidad especialidad, String numeroColegiado, String genero, String email, String apellido2, String apellido1, String nombre, String dni, int telefono, int id) {
        this.especialidad = especialidad;
        this.numeroColegiado = numeroColegiado;
        this.genero = genero;
        this.email = email;
        this.apellido2 = apellido2;
        this.apellido1 = apellido1;
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.id = id;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
