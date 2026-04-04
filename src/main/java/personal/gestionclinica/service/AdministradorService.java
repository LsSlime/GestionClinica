package personal.gestionclinica.service;

import personal.gestionclinica.model.Administrador;
import personal.gestionclinica.repository.AdministradorDAO;

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
        return administradorDAO.obtenerUsuario(email, dni);
    }

}
