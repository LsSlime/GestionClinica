package personal.gestionclinica.repository;

import personal.gestionclinica.model.Administrador;

public interface AdministradorDAO {
    Administrador obtenerUsuario(String email, String dni);
}
