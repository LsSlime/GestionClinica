package personal.gestionclinica.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import personal.gestionclinica.model.Citas;
import personal.gestionclinica.model.Especialidad;
import personal.gestionclinica.model.Medico;
import personal.gestionclinica.model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("CitaDAOJdbc")
public class CitaDAOJdbc implements CitaDAO {

    private Connection getConnection() {return Conexion.getInstancia().getConnection();}

        //PRUEBA PARA FILTRAR ESPECIALIDADES Y MEDICO - IA


    // Cargar especialidades para el primer combo
   // public List<Especialidad> obtenerEspecialidades() {
     //   String sql = "SELECT id, nombre FROM especialidades ORDER BY nombre";
        // ...
    // }

    // Cargar médicos según especialidad elegida
    // public List<Medico> obtenerMedicosPorEspecialidad(int idEspecialidad) {
    //    String sql = "SELECT id, nombre, apellido1 FROM medicos WHERE id_especialidad = ?";
        // ...
    //}


    //SOLO PACIENTE O ADMIN
    @Override
    public void guardarCita(Citas citas) {
        String sql = "INSERT INTO citas (id_paciente, id_medico, fecha_cita, motivo, descripcion, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement pstmt = getConnection().prepareStatement(sql)){
            pstmt.setInt(1, citas.getPaciente().getId());
            pstmt.setInt(2, citas.getMedico().getId());
            pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(citas.getFechaHora()));
            pstmt.setString(4, citas.getMotivo());
            pstmt.setString(5, citas.getDescripcion());
            pstmt.setString(6, "pendiente");
            pstmt.executeUpdate();
            System.out.println("Cita agendada con exito.");
        } catch (SQLException e) {
            throw new IllegalStateException("Error al guardar la cita: " + e.getMessage(), e);
        }
    }



    //SOLO MEDICO
    @Override
    public void completarCita(Citas citas) {
        String sql = "UPDATE citas SET diagnostico = ?, tratamiento = ?, observaciones = ?, estado = 'completada' WHERE id = ?";
        try(PreparedStatement pstmt = getConnection().prepareStatement(sql)){
            pstmt.setString(1, citas.getDiagnostico());
            pstmt.setString(2, citas.getTratamiento());
            pstmt.setString(3, citas.getObservaciones());
            pstmt.setInt(4, citas.getId());
            pstmt.executeUpdate();
            System.out.println("Cita completada con exito.");
        } catch (SQLException e) {
            throw new IllegalStateException("Error al completar la cita: " + e.getMessage(), e);
        }

    }

    //PACIENTE O ADMIN
    @Override
    public void cancelarCita(int id){
        String sql = "UPDATE citas SET estado = 'cancelada' WHERE id = ?";

        try(PreparedStatement pstmt = getConnection().prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Cita cancelada con exito.");
        } catch (SQLException e) {
            throw new IllegalStateException("Error al cancelar la cita: " + e.getMessage(), e);
        }

    }


    public List<Citas> obtenerCitasPorPaciente(int id_Paciente) {
        String sql = """
                SELECT 
                    c.id, c.fecha_cita, c.motivo, c.descripcion, c.estado, c.diagnostico, c.tratamiento, c.observaciones, p.id AS id_paciente, p.nombre AS nombre_paciente, p.apellido1 AS apellido1_paciente,
                    m.id AS id_medico, m.nombre AS nombre_medico, m.apellido1 AS apellido1_medico, 
                    e.nombre AS nombre_especialidad
                FROM citas c 
                JOIN pacientes p ON c.id_paciente = p.id
                JOIN medicos m ON c.id_medico = m.id 
                JOIN especialidades e ON m.id_especialidad = e.id 
                WHERE c.id_paciente = ? 
                ORDER BY c.fecha_cita DESC
                """;
        List<Citas> lista = new ArrayList<>();
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id_Paciente);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearCitas(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al obtener la cita: " + e.getMessage(), e);
        }
        return lista;
    }


    public List<Citas> obtenerCitasPorMedico(int id_Medico){
        String sql = """
                SELECT
                c.id, c.fecha_cita, c.motivo, c.descripcion, c.estado, c.diagnostico, c.tratamiento, c.observaciones, p.id AS id_paciente, p.nombre AS nombre_paciente, p.apellido1 AS apellido1_paciente,
                m.id AS id_medico, m.nombre AS nombre_medico, m.apellido1 AS apellido1_medico, 
                e.nombre AS nombre_especialidad
                FROM citas c 
                JOIN pacientes p ON c.id_paciente = p.id
                JOIN medicos m ON c.id_medico = m.id 
                JOIN especialidades e ON m.id_especialidad = e.id 
                WHERE c.id_medico = ? AND c.estado NOT IN ('cancelada')
                ORDER BY c.fecha_cita DESC
                
                """;

        List<Citas> lista = new ArrayList<>();
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id_Medico);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearCitas(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al obtener la cita: " + e.getMessage(), e);
        }
        return lista;
    }


    public List<Citas> listarTodasLasCitas(){
        List<Citas> citas = new ArrayList<>();
        String sql = "select c.* from citas c";
        try(PreparedStatement pstmt = getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
            while (rs.next()) {
                citas.add(mapearCitas(rs));
            }
            System.out.println("Listado de citas recuperado con exito.");
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar la citas: " + e.getMessage(), e);
    }
    return citas;
}



    private Citas mapearCitas(ResultSet rs) throws SQLException {

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(rs.getString("nombre_especialidad"));

        Paciente paciente = new  Paciente();
        paciente.setId(rs.getInt("id_paciente"));
        paciente.setNombre(rs.getString("nombre_paciente"));
        paciente.setApellido1(rs.getString("apellido1_paciente"));


        Medico medico = new Medico();
        medico.setId(rs.getInt("id_medico"));
        medico.setNombre(rs.getString("nombre_medico"));
        medico.setApellido1(rs.getString("apellido1_medico"));
        medico.setEspecialidad(especialidad);

        Citas citas = new Citas();
        citas.setId(rs.getInt("id"));
        citas.setPaciente(paciente);
        citas.setMedico(medico);
        citas.setFechaHora(rs.getTimestamp("fecha_cita").toLocalDateTime());
        citas.setMotivo(rs.getString("motivo"));
        citas.setDescripcion(rs.getString("descripcion"));

        citas.setEstado(rs.getString("estado"));
        // cita.setEstado(EstadoCita.valueOf(rs.getString("estado").toUpperCase()));

        citas.setDiagnostico(rs.getString("diagnostico"));
        citas.setTratamiento(rs.getString("tratamiento"));
        citas.setObservaciones(rs.getString("observaciones"));

        return citas;
    }


}
