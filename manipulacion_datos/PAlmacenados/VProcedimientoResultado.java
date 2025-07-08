package manipulacion_datos.PAlmacenados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VProcedimientoResultado extends JFrame {

    public VProcedimientoResultado(String nombreFuncion, String parametro, Connection conn) {
        setTitle("Resultado del procedimiento: " + nombreFuncion);
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Resultados de: " + nombreFuncion, SwingConstants.CENTER);
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

        // Construye SQL según si el parámetro existe o no
        String sql = (parametro == null || parametro.trim().isEmpty())
                ? "SELECT * FROM " + nombreFuncion + "()"
                : "SELECT * FROM " + nombreFuncion + "('" + parametro + "')";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnas = meta.getColumnCount();
            DefaultTableModel modelo = new DefaultTableModel();

            // Ocultar los encabezados: columnas vacías
            for (int i = 1; i <= columnas; i++) {
                modelo.addColumn(""); // encabezados ocultos
            }

            while (rs.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 0; i < columnas; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                modelo.addRow(fila);
            }

            tabla.setModel(modelo);
            tabla.getTableHeader().setUI(null); // Ocultar encabezados visuales

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al ejecutar el procedimiento: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
