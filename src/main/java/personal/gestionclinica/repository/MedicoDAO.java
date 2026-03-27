package personal.gestionclinica.repository;

import personal.gestionclinica.model.Medico;

import java.util.List;

public interface MedicoDAO {

    void guardar(Medico medico);
    void actualizar(Medico medico);
    void eliminar (int id);
    Medico obtenerPorId(int id);
    List<Medico> listarTodos();


}
