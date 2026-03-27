package personal.gestionclinica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import personal.gestionclinica.model.Especialidad;
import personal.gestionclinica.repository.EspecialidadDAO;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadDAO especialidadDAO;

    @Autowired
    public EspecialidadService(@Qualifier("EspecialidadDAOJdbc") EspecialidadDAO especialidadDAO) {
        this.especialidadDAO = especialidadDAO;
    }

    public void guardarEspecialidad(Especialidad especialidad) {
        if (especialidad == null || especialidad.getNombre() == null || especialidad.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la especialidad es obligatorio.");
        }
        especialidad.setNombre(especialidad.getNombre().trim());
        especialidadDAO.guardar(especialidad);
    }

    public List<Especialidad> listarTodos() {
        return especialidadDAO.listarTodos();
    }

    public Especialidad obtenerPorId(int id) {
        return especialidadDAO.obtenerPorId(id);
    }
}


