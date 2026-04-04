package personal.gestionclinica.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import personal.gestionclinica.model.Administrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Qualifier("AdministradorDAOJdbc")
public class AdministradorDAOJdbc implements AdministradorDAO{

    private Connection getConnection() throws SQLException {
        Connection connection = Conexion.getInstancia().getConnection();
        if (connection == null || connection.isClosed()) {
            throw new SQLException("No se pudo establecer la conexion con la base de datos.");
        }
        return connection;
    }

    @Override
    public Administrador ObtenerUsuario (String email, String dni) {
        String sql = "SELECT * FROM administradores WHERE email = ? AND dni = ?";
        Administrador administrador = null;

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, dni);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    administrador = mapearAdministrador(rs);
                }
            }
            } catch (SQLException e){
                throw new IllegalStateException("Error al obtener el administrador: " + e.getMessage(), e);
            }
        return administrador;
    }

    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {
        Administrador administrador = new Administrador();
        administrador.setId(rs.getInt("id"));
        administrador.setEmail(rs.getString("email"));
        administrador.setDni(rs.getString("dni"));
        administrador.setNombre(rs.getString("nombre"));
        return administrador;
    }

}
