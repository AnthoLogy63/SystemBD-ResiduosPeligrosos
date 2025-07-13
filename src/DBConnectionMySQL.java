package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionMySQL {
    // Cambia el nombre de la base si es diferente en tu servidor MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/ResToxicos?serverTimezone=UTC";
    private static final String USUARIO = "root"; // o tu usuario
    private static final String PASSWORD = "123456"; // o tu contraseña

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL no encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error cerrando la conexión: " + e.getMessage());
            }
        }
    }
}
