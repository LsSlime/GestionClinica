package personal.gestionclinica.repository;


import personal.gestionclinica.model.Paciente;

import java.util.List;

/**
 *
 * @author sanch
 */

public interface PacienteDAO {

    void guardar(Paciente paciente);
    void actualizar(Paciente paciente);
    void eliminar (int id);
    Paciente obtenerPorId(int id);
    List<Paciente> listarTodos();

}
