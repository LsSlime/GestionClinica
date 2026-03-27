package personal.gestionclinica.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/gestordesalud";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Conexion instancia;
    private Connection connection;


    private Conexion() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión a la base de datos establecida.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }


    public static synchronized Conexion getInstancia() {
        if (instancia == null || instancia.connection == null) {
            instancia = new Conexion();
        }
        return instancia;
    }


    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión.");
            e.printStackTrace();
        }
    }

}
