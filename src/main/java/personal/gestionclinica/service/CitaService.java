package personal.gestionclinica.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import personal.gestionclinica.model.Citas;
import personal.gestionclinica.repository.CitaDAO;

import java.util.List;

@Service
public class CitaService {

    private final CitaDAO citaDAO;

    @Autowired

    public CitaService(@Qualifier("CitaDAOJdbc") CitaDAO citaDAO) {
        this.citaDAO = citaDAO;
    }

    public void guardarCita (Citas cita){
        if (cita == null || cita.getPaciente() == null || cita.getPaciente().getId() <= 0) {
            throw new IllegalArgumentException("Debes seleccionar un paciente.");
        }
        if (cita.getMedico() == null || cita.getMedico().getId() <= 0) {
            throw new IllegalArgumentException("Debes seleccionar un medico.");
        }
        if (cita.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora son obligatorias.");
        }
        citaDAO.guardarCita(cita);
    }


    public void completarCita (Citas cita){
    citaDAO.completarCita(cita);
    }

    public void cancelarCita (int id){
    citaDAO.cancelarCita(id);
    }

    public List<Citas> obtenerCitasPorPaciente(int idPaciente){
    return citaDAO.obtenerCitasPorPaciente(idPaciente);
    }

    public List<Citas> obtenerCitasPorMedico(int idMedico){
        return citaDAO.obtenerCitasPorMedico(idMedico);
    }

    public List<Citas> listarTodasLasCitas() {
        return citaDAO.listarTodasLasCitas();
    }

}
