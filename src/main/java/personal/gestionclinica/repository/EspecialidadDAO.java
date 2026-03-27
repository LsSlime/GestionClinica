package personal.gestionclinica.repository;

import personal.gestionclinica.model.Especialidad;

import java.util.List;

public interface EspecialidadDAO {

    void guardar(Especialidad especialidad);
    List<Especialidad> listarTodos();
    Especialidad obtenerPorId(int id);

}
