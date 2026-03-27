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
        String sql = "INSERT INTO medicos (dni, nombre, apellido1, apellido2, telefono, email, id_especialidad, numeroColegiado) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, medico.getDni());
            pstmt.setString(2, medico.getNombre());
            pstmt.setString(3, medico.getApellido1());
            pstmt.setString(4, medico.getApellido2());
            pstmt.setInt(5, medico.getTelefono());
            pstmt.setString(6, medico.getEmail());
            pstmt.setInt(7, medico.getEspecialidad().getId());
            pstmt.setString(8, medico.getNumeroColegiado());
            pstmt.executeUpdate();
            System.out.println("Medico guardado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al querer guardar al medico.");
            e.printStackTrace();
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
            System.out.println("Error al querer actualizar al medico.");
            e.printStackTrace();
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
            System.out.println("Error al querer eliminar al medico.");
            e.printStackTrace();
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
            System.out.println("Error al querer obtener el medico por id.");
            e.printStackTrace();
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
            System.out.println("Error al querer listar los medicos.");
            e.printStackTrace();
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
        medico.setNumeroColegiado(rs.getString("numeroColegiado"));
        return medico;
    }


}
