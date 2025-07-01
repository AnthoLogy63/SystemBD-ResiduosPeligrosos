package empresa;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentListener;
import java.util.regex.Pattern;
import java.awt.*;

public class RegistroPanelEmpresa extends JPanel {
    private JTextField tfNif;
    private JTextField tfNombre;
    private JComboBox<String> cbCiudad;
    private JTextField tfActividad;
    private JTextField tfObservacion;
    private JTextField tfEstado;

    public RegistroPanelEmpresa() {
        setBackground(Color.decode("#FFFFFF"));
        setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Registro de Empresa"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 60);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("NIF:"), gbc);
        gbc.gridx = 1;
        tfNif = new JTextField(9);
        tfNif.setPreferredSize(new Dimension(120, tfNif.getPreferredSize().height));

        // Validación y autocompletado al perder el foco
        tfNif.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String texto = tfNif.getText().trim();
                if (!texto.isEmpty()) {
                    if (texto.matches("\\d{1,9}")) {
                        int numero = Integer.parseInt(texto);
                        tfNif.setText(String.format("EMP%09d", numero));
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese solo números válidos (máx. 9 dígitos).", "Error", JOptionPane.WARNING_MESSAGE);
                        tfNif.setText("");
                    }
                }
            }

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                String texto = tfNif.getText().trim();
                if (texto.startsWith("EMP") && texto.length() == 12) {
                    tfNif.setText(texto.substring(3)); // Solo muestra los dígitos al editar
                }
            }
        });
        add(tfNif, gbc);


        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        tfNombre = new JTextField();
        tfNombre.setPreferredSize(new Dimension(250, tfNombre.getPreferredSize().height));
        add(tfNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Ciudad:"), gbc);
        gbc.gridx = 1;
        cbCiudad = new JComboBox<>();
        cbCiudad.setPreferredSize(new Dimension(200, cbCiudad.getPreferredSize().height));
        add(cbCiudad, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Actividad:"), gbc);
        gbc.gridx = 1;
        tfActividad = new JTextField();
        tfActividad.setPreferredSize(new Dimension(250, tfActividad.getPreferredSize().height));
        add(tfActividad, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Observación:"), gbc);
        gbc.gridx = 1;
        tfObservacion = new JTextField();
        tfObservacion.setPreferredSize(new Dimension(250, tfObservacion.getPreferredSize().height));
        add(tfObservacion, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        tfEstado = new JTextField(2);
        tfEstado.setHorizontalAlignment(SwingConstants.CENTER);
        tfEstado.setEditable(false);
        tfEstado.setFocusable(false);
        tfEstado.setBackground(Color.decode("#FFFFFF"));
        tfEstado.setPreferredSize(new Dimension(60, tfEstado.getPreferredSize().height));
        add(tfEstado, gbc);

        setEditableRegistro(false);
    }

    public JTextField getTfNif() { return tfNif; }
    public JTextField getTfNombre() { return tfNombre; }
    public JComboBox<String> getCbCiudad() { return cbCiudad; }
    public JTextField getTfActividad() { return tfActividad; }
    public JTextField getTfObservacion() { return tfObservacion; }
    public JTextField getTfEstado() { return tfEstado; }

    public void setEditableRegistro(boolean on) {
        tfNif.setEditable(on);
        tfNombre.setEditable(on);
        cbCiudad.setEnabled(on);
        tfActividad.setEditable(on);
        tfObservacion.setEditable(on);
    }

    public void cargarDesdeGrilla(JTable table, int fila) {
        tfNif.setText(table.getValueAt(fila, 0).toString());
        tfNombre.setText(table.getValueAt(fila, 1).toString());
        cbCiudad.setSelectedItem(table.getValueAt(fila, 2).toString());
        tfActividad.setText(table.getValueAt(fila, 3).toString());
        tfObservacion.setText(table.getValueAt(fila, 4).toString());
        tfEstado.setText(table.getValueAt(fila, 5).toString());
    }

    public void limpiar() {
        tfNif.setText("");
        tfNombre.setText("");
        cbCiudad.setSelectedIndex(-1);
        tfActividad.setText("");
        tfObservacion.setText("");
        tfEstado.setText("");
        setEditableRegistro(false);
    }
}
