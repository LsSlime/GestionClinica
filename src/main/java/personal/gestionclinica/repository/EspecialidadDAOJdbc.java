package personal.gestionclinica.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import personal.gestionclinica.model.Especialidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("EspecialidadDAOJdbc")
public class EspecialidadDAOJdbc implements EspecialidadDAO {

    private Connection getConnection() {return Conexion.getInstancia().getConnection();}

    @Override
    public void guardar(Especialidad especialidad) {
        String sql = "INSERT INTO especialidades (nombre) values (?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, especialidad.getNombre());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo guardar la especialidad.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al guardar la especialidad: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Especialidad> listarTodos() {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT id, nombre FROM especialidades";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Especialidad especialidad = new Especialidad();
                especialidad.setId(rs.getInt("id"));
                especialidad.setNombre(rs.getString("nombre"));
                especialidades.add(especialidad);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar las especialidades: " + e.getMessage(), e);
        }
        return especialidades;
    }

    @Override
    public Especialidad obtenerPorId(int id) {
        String sql = "SELECT id, nombre FROM especialidades WHERE id = ?";
        Especialidad especialidad = null;

        try(PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    especialidad = new Especialidad();
                    especialidad.setId(rs.getInt("id"));
                    especialidad.setNombre(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al obtener la especialidad por ID: " + e.getMessage(), e);
        }
        return especialidad;
    }

}
