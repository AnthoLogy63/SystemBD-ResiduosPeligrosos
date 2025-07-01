package residuo;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.util.List;
import residuo.modelo.ResiduoModel;

public class RegistroPanelResiduo extends JPanel {

    private JTextField tfCodigo;
    private JComboBox<String> cbEmpresa;
    private JComboBox<Integer> cbToxicidad;
    private JComboBox<String> cbTipoResiduo;
    private JComboBox<String> cbCodigoNormativo;
    private JTextField tfCantidad;
    private JTextField tfObservacion;
    private JTextField tfEstado; // ✅ CAMBIO: ahora es JTextField en vez de JComboBox
    private char resEst = 'A';

    public char getResEst() { return resEst; }

    public void setResEst(char resEst) {
        this.resEst = resEst;
        if (tfEstado != null) {
            tfEstado.setText(switch (resEst) {
                case 'I' -> "I";
                case '*' -> "*";
                default -> "A";
            });
        }
    }

    public RegistroPanelResiduo() {
        setBackground(Color.WHITE);
        setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder("Registro de Residuo"),
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
        tfCodigo = new JTextField(6);
        tfCodigo.setPreferredSize(new Dimension(120, tfCodigo.getPreferredSize().height));

        // Autocompletar al perder el foco
        tfCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String texto = tfCodigo.getText().trim();
                if (!texto.isEmpty()) {
                    if (texto.matches("\\d{1,6}")) {
                        int numero = Integer.parseInt(texto);
                        tfCodigo.setText(String.format("%06d", numero)); // 000001, etc.
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese un número válido (máx. 6 dígitos).", "Error", JOptionPane.WARNING_MESSAGE);
                        tfCodigo.setText("");
                    }
                }
            }

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                String texto = tfCodigo.getText().trim();
                if (texto.matches("\\d{6}")) {
                    tfCodigo.setText(String.valueOf(Integer.parseInt(texto))); // muestra 5 en vez de 000005
                }
            }
        });

        add(tfCodigo, gbc);

        // Empresa
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Empresa (NIF):"), gbc);
        gbc.gridx = 1;
        cbEmpresa = new JComboBox<>();
        cbEmpresa.setPreferredSize(new Dimension(200, cbEmpresa.getPreferredSize().height));
        add(cbEmpresa, gbc);

        // Toxicidad
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Toxicidad:"), gbc);
        gbc.gridx = 1;
        cbToxicidad = new JComboBox<>();
        cbToxicidad.setPreferredSize(new Dimension(200, cbToxicidad.getPreferredSize().height));
        add(cbToxicidad, gbc);

        // Tipo Residuo
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Tipo de Residuo:"), gbc);
        gbc.gridx = 1;
        cbTipoResiduo = new JComboBox<>();
        cbTipoResiduo.setPreferredSize(new Dimension(200, cbTipoResiduo.getPreferredSize().height));
        add(cbTipoResiduo, gbc);

        // Código Normativo
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Código Normativo:"), gbc);
        gbc.gridx = 1;
        cbCodigoNormativo = new JComboBox<>();
        cbCodigoNormativo.setPreferredSize(new Dimension(200, cbCodigoNormativo.getPreferredSize().height));
        add(cbCodigoNormativo, gbc);

        // Cantidad
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Cantidad (kg):"), gbc);
        gbc.gridx = 1;
        tfCantidad = new JTextField();
        tfCantidad.setPreferredSize(new Dimension(120, tfCantidad.getPreferredSize().height));
        add(tfCantidad, gbc);

        // Observación
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Observación:"), gbc);
        gbc.gridx = 1;
        tfObservacion = new JTextField();
        tfObservacion.setPreferredSize(new Dimension(250, tfObservacion.getPreferredSize().height));
        add(tfObservacion, gbc);

        // Estado
        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        tfEstado = new JTextField(2);
        tfEstado.setHorizontalAlignment(SwingConstants.CENTER);
        tfEstado.setEditable(false);
        tfEstado.setFocusable(false);
        tfEstado.setBackground(Color.decode("#FFFFFF"));
        tfEstado.setPreferredSize(new Dimension(60, tfEstado.getPreferredSize().height));
        add(tfEstado, gbc);

    }

    public JTextField getTfCodigo() { return tfCodigo; }
    public JComboBox<String> getCbEmpresa() { return cbEmpresa; }
    public JComboBox<Integer> getCbToxicidad() { return cbToxicidad; }
    public JComboBox<String> getCbTipoResiduo() { return cbTipoResiduo; }
    public JComboBox<String> getCbCodigoNormativo() { return cbCodigoNormativo; }
    public JTextField getTfCantidad() { return tfCantidad; }
    public JTextField getTfObservacion() { return tfObservacion; }
    public JTextField getTfEstado() { return tfEstado; }

    public void setEditableRegistro(boolean on) {
        tfCodigo.setEditable(on);
        cbEmpresa.setEnabled(on);
        cbToxicidad.setEnabled(on);
        cbTipoResiduo.setEnabled(on);
        cbCodigoNormativo.setEnabled(on);
        tfCantidad.setEditable(on);
        tfObservacion.setEditable(on);
    }

    public void cargarDesdeGrilla(JTable table, int fila) {
        tfCodigo.setText(table.getValueAt(fila, 0).toString());
        cbEmpresa.setSelectedItem(table.getValueAt(fila, 1).toString());
        cbToxicidad.setSelectedItem(Integer.parseInt(table.getValueAt(fila, 2).toString()));
        cbTipoResiduo.setSelectedItem(table.getValueAt(fila, 3).toString());
        cbCodigoNormativo.setSelectedItem(table.getValueAt(fila, 4).toString());
        tfCantidad.setText(table.getValueAt(fila, 5).toString());
        tfObservacion.setText(table.getValueAt(fila, 6).toString());

        String estado = table.getValueAt(fila, 7).toString();
        switch (estado) {
            case "Inactivo" -> setResEst('I');
            case "Eliminado" -> setResEst('*');
            default -> setResEst('A');
        }
    }


    public void limpiar() {
        tfCodigo.setText("");
        cbEmpresa.setSelectedIndex(-1);
        cbToxicidad.setSelectedIndex(-1);
        cbTipoResiduo.setSelectedIndex(-1);
        cbCodigoNormativo.setSelectedIndex(-1);
        tfCantidad.setText("");
        tfObservacion.setText("");
        tfEstado.setText(""); // ← esto limpia el estado
        resEst = '\0';         // ← sin valor
        setEditableRegistro(false);
    }




    public ResiduoModel obtenerDesdeFormulario() {
        int codigo = 0;
        try {
            codigo = Integer.parseInt(tfCodigo.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        String empresa = (String) cbEmpresa.getSelectedItem();
        int toxicidad = cbToxicidad.getSelectedIndex() + 1;
        String tipo = (String) cbTipoResiduo.getSelectedItem();
        String codigoNorm = (String) cbCodigoNormativo.getSelectedItem();
        double cantidad = 0;
        try {
            cantidad = Double.parseDouble(tfCantidad.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        String observacion = tfObservacion.getText().trim();

        return new ResiduoModel(codigo, empresa, toxicidad, tipo, codigoNorm, cantidad, observacion, resEst);
    }

    public void cargarEmpresas(List<String> empresas) {
        cbEmpresa.removeAllItems();
        for (String e : empresas) cbEmpresa.addItem(e);
        cbEmpresa.setSelectedIndex(-1);
    }

    public void cargarToxicidades(List<Integer> niveles) {
        cbToxicidad.removeAllItems();
        for (Integer n : niveles) cbToxicidad.addItem(n);
        cbToxicidad.setSelectedIndex(-1);
    }

    public void cargarTipos(List<String> tipos) {
        cbTipoResiduo.removeAllItems();
        for (String t : tipos) cbTipoResiduo.addItem(t);
        cbTipoResiduo.setSelectedIndex(-1);
    }

    public void cargarCodigosResiduo(List<String> codigos) {
        cbCodigoNormativo.removeAllItems();
        for (String c : codigos) cbCodigoNormativo.addItem(c);
        cbCodigoNormativo.setSelectedIndex(-1);
    }
}
