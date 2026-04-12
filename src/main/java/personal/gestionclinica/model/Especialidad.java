package personal.gestionclinica.model;

public class Especialidad {

    private Integer id;
    private String nombre;
    private String imagenUrl;

    public Especialidad() {
    }

    public Especialidad(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Especialidad(Integer id, String nombre, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
