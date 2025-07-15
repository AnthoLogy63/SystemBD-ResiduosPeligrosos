package manipulacion_datos.PAlmacenados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VProcedimientoResultado extends JFrame {

    public VProcedimientoResultado(String nombreProcedimiento, String parametro, Connection conn) {
        setTitle("Resultado del procedimiento: " + nombreProcedimiento);
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Resultados de: " + nombreProcedimiento, SwingConstants.CENTER);
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

        try {
            CallableStatement stmt;

            if (parametro == null || parametro.trim().isEmpty()) {
                stmt = conn.prepareCall("CALL " + nombreProcedimiento + "()");
            } else {
                stmt = conn.prepareCall("CALL " + nombreProcedimiento + "(?)");
                stmt.setString(1, parametro.trim());
            }

            boolean tieneResultados = stmt.execute();

            if (tieneResultados) {
                ResultSet rs = stmt.getResultSet();
                ResultSetMetaData meta = rs.getMetaData();
                int columnas = meta.getColumnCount();
                DefaultTableModel modelo = new DefaultTableModel();

                // Agregar nombres de columnas
                for (int i = 1; i <= columnas; i++) {
                    modelo.addColumn(meta.getColumnLabel(i));
                }

                // Agregar filas
                int rowCount = 0;
                while (rs.next()) {
                    Object[] fila = new Object[columnas];
                    for (int i = 0; i < columnas; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    modelo.addRow(fila);
                    rowCount++;
                }

                if (rowCount == 0) {
                    JOptionPane.showMessageDialog(this, "El procedimiento se ejecutó pero no retornó filas.");
                }

                tabla.setModel(modelo);
                rs.close();
            } else {
                JOptionPane.showMessageDialog(this, "El procedimiento no retornó resultados.");
            }

            stmt.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al ejecutar el procedimiento: " + ex.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Para depurar en consola
        }
    }
}
