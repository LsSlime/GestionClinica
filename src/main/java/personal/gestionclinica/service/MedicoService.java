package personal.gestionclinica.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import personal.gestionclinica.model.Medico;
import personal.gestionclinica.repository.MedicoDAO;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoDAO medicoDAO;

    @Autowired
    public MedicoService(@Qualifier("MedicoDAOJdbc")  MedicoDAO medicoDAO) {
        this.medicoDAO = medicoDAO;
    }

    public void guardarMedico(Medico medico) {
        medicoDAO.guardar(medico);
    }

    public void actualizarMedico(Medico medico) {
        medicoDAO.actualizar(medico);
    }

    public void eliminarMedico(int id) {
        medicoDAO.eliminar(id);
    }

    public Medico obtenerMedicoPorId(int id) {
        return medicoDAO.obtenerPorId(id);
    }

    public List<Medico> listarTodosLosMedicos() {
        return medicoDAO.listarTodos();
    }

}
