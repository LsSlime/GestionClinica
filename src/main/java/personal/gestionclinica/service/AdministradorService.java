package personal.gestionclinica.service;

import org.springframework.stereotype.Service;
import personal.gestionclinica.model.Administrador;
import personal.gestionclinica.repository.AdministradorDAO;

@Service
public class AdministradorService {

    private final AdministradorDAO administradorDAO;

    public AdministradorService(AdministradorDAO administradorDAO) {
        this.administradorDAO = administradorDAO;
    }

    public Administrador obtenerUsuario(String email, String dni) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingresa el correo electrónico");
        }
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingresa la contraseña");
        }
        return administradorDAO.ObtenerUsuario(email, dni);
    }

}
