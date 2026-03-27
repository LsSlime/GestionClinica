package personal.gestionclinica.service;

import personal.gestionclinica.model.Paciente;
import personal.gestionclinica.repository.PacienteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PacienteService {

    private final PacienteDAO pacienteDAO;

    @Autowired
    public PacienteService(@Qualifier("PacienteDAOJdbc") PacienteDAO pacienteDAO) {
        this.pacienteDAO = pacienteDAO;
    }

    public void guardarPaciente(Paciente paciente) {
        validarPacienteNuevo(paciente);
        pacienteDAO.guardar(paciente);
    }

    public void actualizarPaciente(Paciente paciente) {
        if (paciente == null || paciente.getId() <= 0) {
            throw new IllegalArgumentException("El paciente a actualizar no es valido.");
        }

        Paciente existente = pacienteDAO.obtenerPorId(paciente.getId());
        if (existente == null) {
            throw new IllegalArgumentException("No existe el paciente que intentas actualizar.");
        }

        validarCamposEditables(paciente);
        paciente.setDni(existente.getDni());
        paciente.setGenero(existente.getGenero());
        pacienteDAO.actualizar(paciente);
    }

    public void eliminarPaciente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador del paciente no es valido.");
        }
        pacienteDAO.eliminar(id);
    }

    public Paciente buscarPacientePorId(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return pacienteDAO.obtenerPorId(id);
    }

    public List<Paciente> listarTodosLosPacientes() {
        return pacienteDAO.listarTodos();
    }

    private void validarPacienteNuevo(Paciente paciente) {
        validarCamposEditables(paciente);
        validarTexto(paciente.getDni(), "El DNI es obligatorio.");
        validarTexto(paciente.getGenero(), "El genero es obligatorio.");
    }

    private void validarCamposEditables(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("Los datos del paciente no son validos.");
        }

        validarTexto(paciente.getNombre(), "El nombre es obligatorio.");
        validarTexto(paciente.getApellido1(), "El primer apellido es obligatorio.");
        validarFecha(paciente.getFechaNacimiento());
        validarTelefono(paciente.getTelefono());
        validarTexto(paciente.getEmail(), "El email es obligatorio.");
        validarTexto(paciente.getDireccion(), "La direccion es obligatoria.");
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private void validarFecha(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede estar en el futuro.");
        }
    }

    private void validarTelefono(int telefono) {
        if (telefono <= 0) {
            throw new IllegalArgumentException("El telefono es obligatorio.");
        }
    }
}
