package manipulacion_datos.Triggers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import src.DBConnection;

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

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM \"LOG_ADVERTENCIAS\" ORDER BY \"FechaHora\" DESC")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("LogID"),
                    rs.getString("Origen"),
                    rs.getString("Detalle"),
                    rs.getString("TablaAfectada"),
                    rs.getString("ClavePrimaria"),
                    rs.getTimestamp("FechaHora")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el historial: " + e.getMessage());
        }

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}
