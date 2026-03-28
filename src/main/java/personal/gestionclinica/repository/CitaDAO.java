package personal.gestionclinica.repository;

import personal.gestionclinica.model.Citas;

import java.util.List;


public interface CitaDAO {

    void guardarCita(Citas citas);
    void completarCita(Citas cita);
    void cancelarCita(int id);
    // void actualizar(Citas citas);
    // Citas obtenerPorId(int id);
    List<Citas> listarTodasLasCitas();
    List<Citas> obtenerCitasPorPaciente(int idPaciente);
    List<Citas> obtenerCitasPorMedico(int idMedico);
}
