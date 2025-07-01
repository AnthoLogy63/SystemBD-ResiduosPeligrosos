package constituyente;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class RegistroPanelConstituyente extends JPanel {

    private JTextField tfCodigo;
    private JTextField tfNombre;
    private JTextField tfObservacion;
    private JTextField tfEstado;
    private char estadoActual = 'A';

    public RegistroPanelConstituyente() {
        setBackground(Color.decode("#FFFFFF"));
        setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Registro de Constituyente"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 60);
        gbc.anchor = GridBagConstraints.WEST;

        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        tfCodigo = new JTextField(3);
        tfCodigo.setPreferredSize(new Dimension(80, tfCodigo.getPreferredSize().height));

        // Autocompletar al perder el foco
        tfCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String texto = tfCodigo.getText().trim();
                if (!texto.isEmpty()) {
                    if (texto.matches("\\d{1,3}")) {
                        int numero = Integer.parseInt(texto);
                        tfCodigo.setText(String.format("%03d", numero));
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese un número válido (máx. 3 dígitos).", "Error", JOptionPane.WARNING_MESSAGE);
                        tfCodigo.setText("");
                    }
                }
            }

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                String texto = tfCodigo.getText().trim();
                if (texto.matches("\\d{3}")) {
                    tfCodigo.setText(String.valueOf(Integer.parseInt(texto)));
                }
            }
        });

        add(tfCodigo, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        tfNombre = new JTextField();
        tfNombre.setPreferredSize(new Dimension(250, tfNombre.getPreferredSize().height));
        add(tfNombre, gbc);

        // Observaciones
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        tfObservacion = new JTextField();
        tfObservacion.setPreferredSize(new Dimension(250, tfObservacion.getPreferredSize().height));
        add(tfObservacion, gbc);

        // Estado
        gbc.gridx = 0; gbc.gridy = 3;
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

    // Getters
    public JTextField getTfCodigo() { return tfCodigo; }
    public JTextField getTfNombre() { return tfNombre; }
    public JTextField getTfObservacion() { return tfObservacion; }
    public JTextField getTfEstado() { return tfEstado; }

    public char getEstadoActual() { return estadoActual; }

    public void setEstadoActual(char estado) {
        this.estadoActual = estado;
        tfEstado.setText(switch (estado) {
            case 'I' -> "I";
            case '*' -> "*";
            default -> "A";
        });
    }

    public void cargarDesdeGrilla(JTable table, int fila) {
        tfCodigo.setText(table.getValueAt(fila, 0).toString());
        tfNombre.setText(table.getValueAt(fila, 1).toString());
        tfObservacion.setText(table.getValueAt(fila, 2).toString());

        String estado = table.getValueAt(fila, 3).toString();
        switch (estado) {
            case "I", "Inactivo" -> setEstadoActual('I');
            case "*", "Eliminado" -> setEstadoActual('*');
            default -> setEstadoActual('A');
        }
    }

    public void limpiar() {
        tfCodigo.setText("");
        tfNombre.setText("");
        tfObservacion.setText("");
        tfEstado.setText("");
        estadoActual = '\0';
        setEditableRegistro(false);
    }

    public void setEditableRegistro(boolean on) {
        tfCodigo.setEditable(on);
        tfNombre.setEditable(on);
        tfObservacion.setEditable(on);
    }

    // Método adicional sugerido (como en Residuo)
    public int obtenerCodigoSeguro() {
        try {
            return Integer.parseInt(tfCodigo.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
}
