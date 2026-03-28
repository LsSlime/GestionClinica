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

}
