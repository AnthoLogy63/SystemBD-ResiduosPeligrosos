package rescons;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class RegistroPanelResCons extends JPanel {

    private JComboBox<String> cbEmpresa;
    private JComboBox<String> cbResiduo;
    private JComboBox<String> cbConstituyente;
    private JTextField tfCantidad;
    private JTextField tfEstado;

    public RegistroPanelResCons() {
        setBackground(Color.WHITE);
        setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Registro Residuo-Constituyente"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 40);
        gbc.anchor = GridBagConstraints.WEST;

        // Empresa (NIF)
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Empresa (NIF):"), gbc);
        gbc.gridx = 1;
        cbEmpresa = new JComboBox<>();
        cbEmpresa.setPreferredSize(new Dimension(200, cbEmpresa.getPreferredSize().height));
        add(cbEmpresa, gbc);

        // Código de Residuo
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Código Residuo:"), gbc);
        gbc.gridx = 1;
        cbResiduo = new JComboBox<>();
        cbResiduo.setPreferredSize(new Dimension(120, cbResiduo.getPreferredSize().height));
        add(cbResiduo, gbc);

        // Código de Constituyente
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Código Constituyente:"), gbc);
        gbc.gridx = 1;
        cbConstituyente = new JComboBox<>();
        cbConstituyente.setPreferredSize(new Dimension(120, cbConstituyente.getPreferredSize().height));
        add(cbConstituyente, gbc);

        // Cantidad
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        tfCantidad = new JTextField();
        tfCantidad.setPreferredSize(new Dimension(100, tfCantidad.getPreferredSize().height));
        add(tfCantidad, gbc);

        // Estado
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        tfEstado = new JTextField(2);
        tfEstado.setHorizontalAlignment(SwingConstants.CENTER);
        tfEstado.setEditable(false);
        tfEstado.setFocusable(false);
        tfEstado.setBackground(Color.WHITE);
        tfEstado.setPreferredSize(new Dimension(60, tfEstado.getPreferredSize().height));
        add(tfEstado, gbc);

        setEditableRegistro(false);
    }

    // Getters públicos (alineados con ComponentesResCons)
    public JComboBox<String> getCbEmpresa() { return cbEmpresa; }
    public JComboBox<String> getCbResiduo() { return cbResiduo; }
    public JComboBox<String> getCbConstituyente() { return cbConstituyente; }
    public JTextField getTfCantidad() { return tfCantidad; }
    public JTextField getTfEstado() { return tfEstado; }

    public void setEditableRegistro(boolean on) {
        cbEmpresa.setEnabled(on);
        cbResiduo.setEnabled(on);
        cbConstituyente.setEnabled(on);
        tfCantidad.setEditable(on);
    }

    public void limpiar() {
        cbEmpresa.setSelectedIndex(-1);
        cbResiduo.setSelectedIndex(-1);
        cbConstituyente.setSelectedIndex(-1);
        tfCantidad.setText("");
        tfEstado.setText("");
        setEditableRegistro(false);
    }

    public void cargarDesdeGrilla(JTable table, int fila) {
        cbEmpresa.setSelectedItem(table.getValueAt(fila, 0).toString());
        cbResiduo.setSelectedItem(table.getValueAt(fila, 1).toString());
        cbConstituyente.setSelectedItem(table.getValueAt(fila, 2).toString());
        tfCantidad.setText(table.getValueAt(fila, 3).toString());
        tfEstado.setText(table.getValueAt(fila, 4).toString());
    }
}
