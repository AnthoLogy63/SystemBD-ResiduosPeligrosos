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
        Object[][] datos;

        try (Connection conn = src.DBConnectionMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW FULL TABLES IN restoxicos WHERE Table_type = 'VIEW'")) {

            java.util.List<Object[]> vistas = new ArrayList<>();

            while (rs.next()) {
                String nombreVista = rs.getString(1);
                String descripcion = switch (nombreVista) {
                    case "vista_empresa_residuos" -> "Empresas con residuos generados";
                    case "vista_historial_traslados_detallado" -> "Historial detallado de traslados";
                    case "vista_residuos_componentes" -> "Residuos y sus componentes";
                    case "vista_residuos_detallado" -> "Listado detallado de residuos";
                    case "vista_total_residuos_por_empresa" -> "Totales por empresa";
                    case "vista_toxicidades_activas" -> "Toxicidades activas";
                    default -> "Vista: " + nombreVista;
                };
                vistas.add(new Object[]{nombreVista, descripcion});
            }

            datos = vistas.toArray(new Object[0][]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando vistas: " + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            datos = new Object[0][0];
        }

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
                    Connection conn = src.DBConnectionMySQL.getConnection();
                    if (conn != null) {
                        new VistaResultado(vistaSeleccionada, conn).setVisible(true);
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
