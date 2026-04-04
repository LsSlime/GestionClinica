package personal.gestionclinica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.gestionclinica.model.Administrador;
import personal.gestionclinica.model.Medico;
import personal.gestionclinica.model.Paciente;
import personal.gestionclinica.model.Usuario;

@Service
public class LoginService {

    private final AdministradorService administradorService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    @Autowired
    public LoginService(AdministradorService administradorService, PacienteService pacienteService, MedicoService medicoService) {
        this.administradorService = administradorService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    public Usuario autenticar(String email, String dni) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingresa el correo electrónico");
        }
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingresa la contraseña");
        }

        email = email.trim();
        dni = dni.trim();

        Administrador administrador = administradorService.obtenerUsuario(email, dni);
        if (administrador != null) {
            return new Usuario(administrador.getId(), administrador.getNombre(), administrador.getEmail(), "ADMIN");
        }

        Medico medico = medicoService.obtenerUsuario(email, dni);
        if (medico != null) {
            return new Usuario(medico.getId(), medico.getNombre(), medico.getEmail(), "MEDICO");
        }

        Paciente paciente = pacienteService.obtenerUsuario(email, dni);
        if (paciente != null) {
            return new Usuario(paciente.getId(), paciente.getNombre(), paciente.getEmail(), "PACIENTE");
        }
        return null;
    }
}
