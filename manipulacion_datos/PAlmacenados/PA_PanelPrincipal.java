package manipulacion_datos.PAlmacenados;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.*;
import manipulacion_datos.PAlmacenados.VProcedimientoResultado;

public class PA_PanelPrincipal extends JFrame {

    private JList<String> listaProcedimientos;
    private JButton btnEjecutar;
    private JButton btnRegresar;

    public PA_PanelPrincipal() {
        setTitle("Ejecución de Procedimientos Almacenados");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("PROCEDIMIENTOS DISPONIBLES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(lblTitulo, BorderLayout.NORTH);

        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        modeloLista.addElement("pa_diagnostico_empresa_completo");
        modeloLista.addElement("pa_diagnostico_empresa_completo_extended");
        modeloLista.addElement("pa_alerta_saturacion_destinos");
        modeloLista.addElement("pa_alerta_saturacion_destinos_extended");

        listaProcedimientos = new JList<>(modeloLista);
        listaProcedimientos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        listaProcedimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(listaProcedimientos);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Procedimientos Almacenados",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 15)));
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
            String seleccionado = listaProcedimientos.getSelectedValue();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un procedimiento primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Connection conn = src.DBConnection.getConnection();
                if (conn == null) throw new SQLException("No se pudo obtener conexión a la BD");

                if (seleccionado.endsWith("_extended")) {
                    // Funciones que retornan resultados
                    if (seleccionado.contains("diagnostico")) {
                        String nif = JOptionPane.showInputDialog(this, "Ingresa el NIF de la empresa:");
                        if (nif == null || nif.trim().isEmpty()) return;
                        new VProcedimientoResultado(seleccionado, nif.trim(), conn).setVisible(true);
                    } else {
                        // Llamar funciones sin parámetros
                        new VProcedimientoResultado(seleccionado, "", conn).setVisible(true);
                    }
                } else {
                    // Procedimientos normales tipo CALL
                    CallableStatement stmt;
                    if (seleccionado.contains("diagnostico")) {
                        String nif = JOptionPane.showInputDialog(this, "Ingresa el NIF de la empresa:");
                        if (nif == null || nif.trim().isEmpty()) return;
                        stmt = conn.prepareCall("CALL " + seleccionado + "(?)");
                        stmt.setString(1, nif.trim());
                    } else {
                        stmt = conn.prepareCall("CALL " + seleccionado + "()");
                    }

                    stmt.execute();
                    stmt.close();
                    JOptionPane.showMessageDialog(this, "Procedimiento ejecutado correctamente.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al ejecutar el procedimiento: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
