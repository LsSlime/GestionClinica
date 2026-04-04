package personal.gestionclinica.repository;

import personal.gestionclinica.model.Administrador;

public interface AdministradorDAO {
    Administrador ObtenerUsuario(String email, String dni);
}
