package manipulacion_datos.Triggers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import src.DBConnectionMySQL; // Asegúrate de usar la conexión MySQL

public class VistaHistorialAdvertencias extends JFrame {

    public VistaHistorialAdvertencias() {
        setTitle("Historial de Advertencias");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTable tabla = new JTable();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{
            "LogID", "Origen", "Detalle", "Tabla Afectada", "Clave Primaria", "Fecha y Hora"
        });
        tabla.setModel(modelo);

        try (Connection conn = DBConnectionMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM log_advertencias ORDER BY fechahora DESC")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("logid"),
                    rs.getString("origen"),
                    rs.getString("detalle"),
                    rs.getString("tablafectada"),
                    rs.getString("claveprimaria"),
                    rs.getTimestamp("fechahora")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el historial: " + e.getMessage());
        }

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}
