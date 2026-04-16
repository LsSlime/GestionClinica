package personal.gestionclinica.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import personal.gestionclinica.model.Especialidad;
import personal.gestionclinica.model.Medico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("MedicoDAOJdbc")
public class MedicoDAOJdbc implements MedicoDAO {

    private Connection getConnection() throws SQLException {
        Connection connection = Conexion.getInstancia().getConnection();
        if (connection == null || connection.isClosed()) {
            throw new SQLException("No se pudo establecer la conexion con la base de datos.");
        }
        return connection;
    }

    @Override
    public void guardar(Medico medico) {
        String sql = "INSERT INTO medicos (dni, nombre, apellido1, apellido2, telefono, email, genero, id_especialidad, numeroColegiado) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, medico.getDni());
            pstmt.setString(2, medico.getNombre());
            pstmt.setString(3, medico.getApellido1());
            pstmt.setString(4, medico.getApellido2());
            pstmt.setInt(5, medico.getTelefono());
            pstmt.setString(6, medico.getEmail());
            pstmt.setString(7, medico.getGenero());
            pstmt.setInt(8, medico.getEspecialidad().getId());
            pstmt.setString(9, medico.getNumeroColegiado());
            pstmt.executeUpdate();
            System.out.println("Medico guardado correctamente.");
        } catch (SQLException e) {
            throw new IllegalStateException("Error al guardar el medico: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Medico medico) {
        String sql = "UPDATE medicos set nombre = ?, apellido1 = ?, apellido2 = ?, telefono = ?, email = ?, id_especialidad = ?, numeroColegiado = ? where id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, medico.getNombre());
            pstmt.setString(2, medico.getApellido1());
            pstmt.setString(3, medico.getApellido2());
            pstmt.setInt(4, medico.getTelefono());
            pstmt.setString(5, medico.getEmail());
            pstmt.setInt(6, medico.getEspecialidad().getId());
            pstmt.setString(7, medico.getNumeroColegiado());
            pstmt.setInt(8, medico.getId());
            pstmt.executeUpdate();
            System.out.println("Medico actualizado correctamente.");
        } catch (SQLException e) {
            throw new IllegalStateException("Error al actualizar el medico: " + e.getMessage(), e);
        }
    }


    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM medicos where id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Medico eliminado correctamente.");
        } catch (SQLException e) {
            throw new IllegalStateException("Error al eliminar el medico: " + e.getMessage(), e);
        }
    }

    @Override
    public Medico obtenerPorId(int id) {
        String sql = "Select m.*, e.id especialidad_id, e.nombre especialidad_nombre from medicos m left join especialidades e on m.id_especialidad = e.id where m.id = ?";
        Medico medico = null;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    medico = mapearMedico(rs);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al obtener el medico por id: " + e.getMessage(), e);
        }
        return medico;
    }


    @Override
    public List<Medico> listarTodos(){
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT m.*, e.id especialidad_id, e.nombre especialidad_nombre FROM medicos m LEFT JOIN especialidades e ON m.id_especialidad = e.id";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                medicos.add(mapearMedico(rs));
            }
            System.out.println("Medicos listados correctamente.");
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar los medicos: " + e.getMessage(), e);
        }
        return medicos;
    }

    @Override
    public List<Medico> listarPorEspecialidad(int especialidadId) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT m.*, e.id especialidad_id, e.nombre especialidad_nombre FROM medicos m LEFT JOIN especialidades e ON m.id_especialidad = e.id WHERE m.id_especialidad = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, especialidadId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapearMedico(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar los medicos por especialidad: " + e.getMessage(), e);
        }
        return medicos;
    }


    @Override
    public Medico ObtenerUsuario (String email, String dni) {
        String sql = "SELECT m.*, e.id AS especialidad_id, e.nombre AS especialidad_nombre " +
                "FROM medicos m " +
                "LEFT JOIN especialidades e ON m.id_especialidad = e.id " +
                "WHERE m.email = ? AND m.dni = ?";
        Medico medico = null;

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, dni);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    medico = mapearMedico(rs);
                }
            }
        } catch (SQLException e){
            throw new IllegalStateException("Error al obtener el medico: " + e.getMessage(), e);
        }
        return medico;
    }


    @Override
    public List<Medico> buscar(String texto){
        List<Medico> medicos = new ArrayList<>();
        String sql = """
                SELECT m.*, e.id AS especialidad_id, e.nombre AS especialidad_nombre
                FROM medicos m
                LEFT JOIN especialidades e ON m.id_especialidad = e.id
                WHERE m.nombre LIKE ? OR m.apellido1 LIKE ? OR m.apellido2 LIKE ? OR m.dni LIKE ? OR m.email LIKE ?
                ORDER BY m.nombre, m.apellido1
                """;

        String filtro = "%" + texto.trim().toLowerCase() + "%";

        try(PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            for (int i = 1; i <= 7; i++) {
                pstmt.setString(i, filtro);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapearMedico(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar medicos: " + e.getMessage(), e);
        }
        return medicos;
    }



    private Medico mapearMedico(ResultSet rs) throws SQLException {

        Especialidad especialidad = new Especialidad();
        especialidad.setId(rs.getInt("especialidad_id"));
        especialidad.setNombre(rs.getString("especialidad_nombre"));

        Medico medico = new Medico();
        medico.setEspecialidad(especialidad);
        medico.setId(rs.getInt("id"));
        medico.setDni(rs.getString("dni"));
        medico.setNombre(rs.getString("nombre"));
        medico.setApellido1(rs.getString("apellido1"));
        medico.setApellido2(rs.getString("apellido2"));
        medico.setTelefono(rs.getInt("telefono"));
        medico.setEmail(rs.getString("email"));
        medico.setGenero(rs.getString("genero"));
        medico.setNumeroColegiado(rs.getString("numeroColegiado"));
        return medico;
    }


}
