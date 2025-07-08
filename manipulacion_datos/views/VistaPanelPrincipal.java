package manipulacion_datos.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class VistaPanelPrincipal extends JFrame {

    private JTable tablaVistas;
    private JButton btnEjecutar;
    private JButton btnRegresar;

    public VistaPanelPrincipal() {
        setTitle("Reportes generados (vistas)");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("VISTAS DISPONIBLES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"Nombre de Vista", "Descripción"};
        Object[][] datos = {
            {"vista_empresa_residuos", "Empresas con residuos generados"},
            {"vista_info_completa_empresa", "Empresas con ubicación completa"},
            {"vista_total_residuos_por_tipo_y_toxicidad", "Total por tipo y toxicidad"},
            {"vista_historial_traslados_detallado", "Traslados recientes con detalle (Ultimos 30)"}
        };

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVistas = new JTable(modelo);
        tablaVistas.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tablaVistas.setRowHeight(26);
        tablaVistas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVistas.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaVistas);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Lista de Vistas",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 15)
        ));
        scroll.setBackground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        btnEjecutar = new JButton("Ejecutar");
        btnRegresar = new JButton("Regresar");

        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 16);
        btnEjecutar.setFont(fuenteBotones);
        btnRegresar.setFont(fuenteBotones);

        btnEjecutar.setBackground(new Color(0, 123, 255));
        btnEjecutar.setForeground(Color.WHITE);
        btnEjecutar.setOpaque(true);
        btnEjecutar.setBorderPainted(false);
        btnEjecutar.setFocusPainted(false);

        btnRegresar.setBackground(new Color(220, 53, 69));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setOpaque(true);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setFocusPainted(false);

        panelBotones.add(btnEjecutar);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);

        btnRegresar.addActionListener(e -> dispose());

        btnEjecutar.addActionListener(e -> {
            int fila = tablaVistas.getSelectedRow();
            if (fila != -1) {
                String vistaSeleccionada = (String) tablaVistas.getValueAt(fila, 0);
                try {
                    Connection conn = src.DBConnection.getConnection();
                    if (conn != null) {
                        if (vistaSeleccionada.equals("vista_diagnostico_empresa")) {
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("SELECT \"EmpNif\", \"EmpNom\" FROM \"R1M_EMPRESA\"");
                            java.util.List<String> opciones = new java.util.ArrayList<>();
                            while (rs.next()) {
                                String nif = rs.getString("EmpNif");
                                String nombre = rs.getString("EmpNom");
                                opciones.add(nif + " - " + nombre);
                            }
                            rs.close();
                            stmt.close();

                            if (!opciones.isEmpty()) {
                                JComboBox<String> combo = new JComboBox<>(opciones.toArray(new String[0]));
                                int seleccion = JOptionPane.showConfirmDialog(null, combo, "Selecciona una empresa", JOptionPane.OK_CANCEL_OPTION);

                                if (seleccion == JOptionPane.OK_OPTION) {
                                    String seleccionado = (String) combo.getSelectedItem();
                                    String nifSeleccionado = seleccionado.split(" - ")[0];
                                    Statement execStmt = conn.createStatement();
                                    execStmt.execute("CALL pa_diagnostico_empresa('" + nifSeleccionado + "')");
                                    JOptionPane.showMessageDialog(null, "Procedimiento ejecutado para NIF: " + nifSeleccionado);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No hay empresas registradas.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            new VistaResultado(vistaSeleccionada, conn).setVisible(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No se pudo conectar a la base de datos.",
                                "Error de conexión", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar: " + ex.getMessage(),
                            "Error SQL", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Selecciona una vista primero.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}