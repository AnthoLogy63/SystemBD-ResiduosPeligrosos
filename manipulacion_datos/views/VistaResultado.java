package manipulacion_datos.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VistaResultado extends JFrame {

    public VistaResultado(String nombreVista, Connection conn) {
        setTitle("Resultado: " + nombreVista);
        setSize(850, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Datos de: " + nombreVista, SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JTable tabla = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla.setRowSelectionAllowed(false);
        tabla.setColumnSelectionAllowed(false);
        tabla.setCellSelectionEnabled(false);
        tabla.setFocusable(false);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + nombreVista)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnas = meta.getColumnCount();

            DefaultTableModel modelo = new DefaultTableModel();
            for (int i = 1; i <= columnas; i++) {
                modelo.addColumn(meta.getColumnLabel(i));
            }

            Object[] anterior = new Object[columnas];

            while (rs.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 0; i < columnas; i++) {
                    Object actual = rs.getObject(i + 1);

                    String nombreCol = meta.getColumnLabel(i + 1).toLowerCase();
                    if ((nombreCol.contains("nif") || nombreCol.contains("nombre"))
                            && actual != null && actual.equals(anterior[i])) {
                        fila[i] = "";
                    } else {
                        fila[i] = actual;
                        anterior[i] = actual;
                    }
                }
                modelo.addRow(fila);
            }

            tabla.setModel(modelo);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al ejecutar vista: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
