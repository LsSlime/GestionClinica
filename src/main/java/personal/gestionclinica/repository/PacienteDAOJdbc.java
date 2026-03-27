package personal.gestionclinica.repository;

import personal.gestionclinica.model.Paciente;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("PacienteDAOJdbc")
public class PacienteDAOJdbc implements PacienteDAO {

    private Connection getConnection() throws SQLException {
        Connection connection = Conexion.getInstancia().getConnection();
        if (connection == null || connection.isClosed()) {
            throw new SQLException("No se pudo establecer la conexion con la base de datos.");
        }
        return connection;
    }

    @Override
    public void guardar(Paciente paciente) {
        String sql = "INSERT INTO pacientes (dni, nombre, apellido1, apellido2, fecha_nacimiento, genero, telefono, email, direccion) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, paciente.getDni());
            pstmt.setString(2, paciente.getNombre());
            pstmt.setString(3, paciente.getApellido1());
            pstmt.setString(4, paciente.getApellido2());
            pstmt.setDate(5, java.sql.Date.valueOf(paciente.getFechaNacimiento()));
            pstmt.setString(6, paciente.getGenero());
            pstmt.setInt(7, paciente.getTelefono());
            pstmt.setString(8, paciente.getEmail());
            pstmt.setString(9, paciente.getDireccion());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo guardar el paciente.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al guardar el paciente: " + obtenerDetalleSql(e), e);
        }
    }

    @Override
    public void actualizar(Paciente paciente) {
        String sql = "UPDATE pacientes set nombre = ?, apellido1 = ?, apellido2 = ?, fecha_nacimiento = ?, telefono = ?, email = ?, direccion = ? where id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, paciente.getNombre());
            pstmt.setString(2, paciente.getApellido1());
            pstmt.setString(3, paciente.getApellido2());
            pstmt.setDate(4, java.sql.Date.valueOf(paciente.getFechaNacimiento()));
            pstmt.setInt(5, paciente.getTelefono());
            pstmt.setString(6, paciente.getEmail());
            pstmt.setString(7, paciente.getDireccion());
            pstmt.setInt(8, paciente.getId());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No existe el paciente a actualizar.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al actualizar el paciente: " + obtenerDetalleSql(e), e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No existe el paciente a eliminar.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al eliminar el paciente: " + obtenerDetalleSql(e), e);
        }
    }

    @Override
    public Paciente obtenerPorId(int id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        Paciente paciente = null;

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    paciente = mapearPaciente(rs);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al obtener el paciente por ID: " + obtenerDetalleSql(e), e);
        }
        return paciente;
    }

    @Override
    public List<Paciente> listarTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                pacientes.add(mapearPaciente(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar los pacientes: " + obtenerDetalleSql(e), e);
        }
        return pacientes;
    }

    private String obtenerDetalleSql(SQLException e) {
        SQLException actual = e;
        while (actual.getNextException() != null) {
            actual = actual.getNextException();
        }

        String mensaje = actual.getMessage();
        if (mensaje == null || mensaje.isBlank()) {
            mensaje = e.getMessage();
        }
        return mensaje == null || mensaje.isBlank() ? "detalle no disponible." : mensaje;
    }

    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getInt("id"));
        paciente.setDni(rs.getString("dni"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellido1(rs.getString("apellido1"));
        paciente.setApellido2(rs.getString("apellido2"));
        paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        paciente.setGenero(rs.getString("genero"));
        paciente.setTelefono(rs.getInt("telefono"));
        paciente.setEmail(rs.getString("email"));
        paciente.setDireccion(rs.getString("direccion"));
        return paciente;
    }
}
