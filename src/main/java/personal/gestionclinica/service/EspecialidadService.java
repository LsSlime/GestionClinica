package personal.gestionclinica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import personal.gestionclinica.model.Especialidad;
import personal.gestionclinica.repository.EspecialidadDAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class EspecialidadService {

    private final EspecialidadDAO especialidadDAO;
    private final String UPLOAD_DIR = "src/main/resources/static/images/especialidades/";

    @Autowired
    public EspecialidadService(@Qualifier("EspecialidadDAOJdbc") EspecialidadDAO especialidadDAO) {
        this.especialidadDAO = especialidadDAO;
        // Crear directorio si no existe
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            System.err.println("Error al crear directorio de especialidades: " + e.getMessage());
        }
    }

    public void guardarEspecialidad(Especialidad especialidad) {
        if (especialidad == null || especialidad.getNombre() == null || especialidad.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la especialidad es obligatorio.");
        }
        especialidad.setNombre(especialidad.getNombre().trim());
        especialidadDAO.guardar(especialidad);
    }

    public void guardarEspecialidadConImagen(Especialidad especialidad, MultipartFile archivo) throws IOException {
        if (especialidad == null || especialidad.getNombre() == null || especialidad.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la especialidad es obligatorio.");
        }
        especialidad.setNombre(especialidad.getNombre().trim());

        // Procesar la imagen si existe
        if (archivo != null && !archivo.isEmpty()) {
            String nombreArchivo = generarNombreArchivo(archivo.getOriginalFilename());
            Path rutaCompleta = Paths.get(UPLOAD_DIR + nombreArchivo);
            
            // Guardar el archivo
            Files.write(rutaCompleta, archivo.getBytes());
            
            // Guardar la ruta relativa en la BD
            especialidad.setImagenUrl("/images/especialidades/" + nombreArchivo);
        } else {
            // Imagen por defecto si no se proporciona
            especialidad.setImagenUrl("/images/especialidades/default.png");
        }

        especialidadDAO.guardar(especialidad);
    }

    private String generarNombreArchivo(String nombreOriginal) {
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    public List<Especialidad> listarTodos() {
        return especialidadDAO.listarTodos();
    }

    public Especialidad obtenerPorId(int id) {
        return especialidadDAO.obtenerPorId(id);
    }
}


