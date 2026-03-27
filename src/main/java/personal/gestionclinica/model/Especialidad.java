package personal.gestionclinica.model;

public class Especialidad {

    private Integer id;
    private String nombre;

    public Especialidad() {
    }

    public Especialidad(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
