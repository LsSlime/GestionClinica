package personal.gestionclinica.model;

public class Administrador {

    private int id;
    private String email;
    private String dni;
    private String nombre;

    public Administrador() {
    }

    public Administrador(int id, String email, String dni, String nombre) {
        this.id = id;
        this.email = email;
        this.dni = dni;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
