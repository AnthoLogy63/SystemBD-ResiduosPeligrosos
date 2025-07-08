package traslado;

import javax.swing.*;
import javax.swing.border.CompoundBorder;

import traslado.dao.TrasladoDAO;
import traslado.modelo.TrasladoModel;

import java.awt.*;
import java.sql.SQLException;

public class RegistroPanelTraslado extends JPanel {

    private JComboBox<String> cbEmpresa;
    private JComboBox<Integer> cbResiduo;
    private JTextField tfFechaEnvio;
    private JComboBox<String> cbDestino;
    private JComboBox<String> cbEnvase;
    private JTextField tfFechaLlegada;
    private JComboBox<String> cbTratamiento;
    private JTextField tfCantidad;
    private JTextField tfObservacion;
    private JComboBox<String> cbTransportista;
    private JTextField tfEstado;

    public RegistroPanelTraslado() {
        setBackground(Color.WHITE);
        setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Registro de Traslado"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 40);
        gbc.anchor = GridBagConstraints.WEST;

        // Empresa
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Empresa (NIF):"), gbc);
        gbc.gridx = 1;
        cbEmpresa = new JComboBox<>();
        cbEmpresa.setPreferredSize(new Dimension(200, cbEmpresa.getPreferredSize().height));
        add(cbEmpresa, gbc);

        // Residuo
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Código Residuo:"), gbc);
        gbc.gridx = 1;
        cbResiduo = new JComboBox<>();
        cbResiduo.setPreferredSize(new Dimension(200, cbResiduo.getPreferredSize().height));
        add(cbResiduo, gbc);

        // Fecha de Envío
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Fecha Envío (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        tfFechaEnvio = new JTextField();
        tfFechaEnvio.setPreferredSize(new Dimension(120, tfFechaEnvio.getPreferredSize().height));
        add(tfFechaEnvio, gbc);
        tfFechaEnvio.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String texto = tfFechaEnvio.getText().trim();
                if (!texto.isEmpty() && !texto.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(null,
                        "La fecha de envío debe tener el formato YYYY-MM-DD",
                        "Formato inválido", JOptionPane.WARNING_MESSAGE);
                    tfFechaEnvio.requestFocus();
                }
            }
        });


        // Destino
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Código Destino:"), gbc);
        gbc.gridx = 1;
        cbDestino = new JComboBox<>();
        cbDestino.setPreferredSize(new Dimension(200, cbDestino.getPreferredSize().height));
        add(cbDestino, gbc);

        // Envase
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Tipo de Envase:"), gbc);
        gbc.gridx = 1;
        cbEnvase = new JComboBox<>();
        cbEnvase.setPreferredSize(new Dimension(150, cbEnvase.getPreferredSize().height));
        add(cbEnvase, gbc);

        // Fecha Llegada
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Fecha Llegada (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        tfFechaLlegada = new JTextField();
        tfFechaLlegada.setPreferredSize(new Dimension(120, tfFechaLlegada.getPreferredSize().height));
        add(tfFechaLlegada, gbc);
        tfFechaLlegada.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String texto = tfFechaLlegada.getText().trim();
                if (!texto.isEmpty() && !texto.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(null,
                        "La fecha de llegada debe tener el formato YYYY-MM-DD",
                        "Formato inválido", JOptionPane.WARNING_MESSAGE);
                    tfFechaLlegada.requestFocus();
                }
            }
        });

        // Tratamiento
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Tratamiento:"), gbc);
        gbc.gridx = 1;
        cbTratamiento = new JComboBox<>();
        cbTratamiento.setPreferredSize(new Dimension(150, cbTratamiento.getPreferredSize().height));
        add(cbTratamiento, gbc);

        // Cantidad
        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Cantidad (t):"), gbc);
        gbc.gridx = 1;
        tfCantidad = new JTextField();
        tfCantidad.setPreferredSize(new Dimension(100, tfCantidad.getPreferredSize().height));
        add(tfCantidad, gbc);

        // Observación
        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Observación:"), gbc);
        gbc.gridx = 1;
        tfObservacion = new JTextField();
        tfObservacion.setPreferredSize(new Dimension(250, tfObservacion.getPreferredSize().height));
        add(tfObservacion, gbc);

        // Transportista
        gbc.gridx = 0; gbc.gridy = 9;
        add(new JLabel("Transportista:"), gbc);
        gbc.gridx = 1;
        cbTransportista = new JComboBox<>();
        cbTransportista.setPreferredSize(new Dimension(200, cbTransportista.getPreferredSize().height));
        add(cbTransportista, gbc);

        // Estado
        gbc.gridx = 0; gbc.gridy = 10;
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

    public JComboBox<String> getCbEmpresa() { return cbEmpresa; }
    public JComboBox<Integer> getCbResiduo() { return cbResiduo; }
    public JTextField getTfFechaEnvio() { return tfFechaEnvio; }
    public JComboBox<String> getCbDestino() { return cbDestino; }
    public JComboBox<String> getCbEnvase() { return cbEnvase; }
    public JTextField getTfFechaLlegada() { return tfFechaLlegada; }
    public JComboBox<String> getCbTratamiento() { return cbTratamiento; }
    public JTextField getTfCantidad() { return tfCantidad; }
    public JTextField getTfObservacion() { return tfObservacion; }
    public JComboBox<String> getCbTransportista() { return cbTransportista; }
    public JTextField getTfEstado() { return tfEstado; }

    public void setEditableRegistro(boolean on) {
        cbEmpresa.setEnabled(on);
        cbResiduo.setEnabled(on);
        tfFechaEnvio.setEditable(on);
        cbDestino.setEnabled(on);
        cbEnvase.setEnabled(on);
        tfFechaLlegada.setEditable(on);
        cbTratamiento.setEnabled(on);
        tfCantidad.setEditable(on);
        tfObservacion.setEditable(on);
        cbTransportista.setEnabled(on);
    }

    public void cargarDesdeGrilla(JTable table, int fila) {
        cbEmpresa.setSelectedItem(table.getValueAt(fila, 0).toString().trim());
        cbResiduo.setSelectedItem(Integer.parseInt(table.getValueAt(fila, 1).toString()));
        tfFechaEnvio.setText(table.getValueAt(fila, 2).toString().trim());
        cbDestino.setSelectedItem(table.getValueAt(fila, 3).toString().trim());
        cbEnvase.setSelectedItem(table.getValueAt(fila, 4).toString().trim());
        tfFechaLlegada.setText(table.getValueAt(fila, 5) == null ? "" : table.getValueAt(fila, 5).toString().trim());
        cbTratamiento.setSelectedItem(table.getValueAt(fila, 6).toString().trim());
        tfCantidad.setText(table.getValueAt(fila, 7).toString().trim());
        tfObservacion.setText(table.getValueAt(fila, 8) == null ? "" : table.getValueAt(fila, 8).toString().trim());
        cbTransportista.setSelectedItem(table.getValueAt(fila, 9).toString().trim());
        tfEstado.setText(table.getValueAt(fila, 10).toString().trim());
    }


    public void limpiar() {
        cbEmpresa.setSelectedIndex(-1);
        cbResiduo.setSelectedIndex(-1);
        tfFechaEnvio.setText("");
        cbDestino.setSelectedIndex(-1);
        cbEnvase.setSelectedIndex(-1);
        tfFechaLlegada.setText("");
        cbTratamiento.setSelectedIndex(-1);
        tfCantidad.setText("");
        tfObservacion.setText("");
        cbTransportista.setSelectedIndex(-1);
        tfEstado.setText("");
        setEditableRegistro(false);
    }
    public TrasladoModel toModel() {
        TrasladoModel t = new TrasladoModel();
        t.setEmpNif((String) cbEmpresa.getSelectedItem());
        t.setResCod((Integer) cbResiduo.getSelectedItem());
        t.setFechaEnvio(tfFechaEnvio.getText().trim());
        t.setDestCod((String) cbDestino.getSelectedItem());
        t.setTipoEnvase((String) cbEnvase.getSelectedItem());
        t.setFechaLlegada(tfFechaLlegada.getText().trim().isEmpty() ? null : tfFechaLlegada.getText().trim());
        t.setTratamiento((String) cbTratamiento.getSelectedItem());
        t.setCantidad(Double.parseDouble(tfCantidad.getText().trim()));
        t.setObservacion(tfObservacion.getText().trim().isEmpty() ? null : tfObservacion.getText().trim());
        t.setTransportista((String) cbTransportista.getSelectedItem());
        t.setEstado(tfEstado.getText().trim());
        return t;
    }
    public void bloquearCamposClave() {
        cbEmpresa.setEnabled(false);
        cbResiduo.setEnabled(false);
        tfFechaEnvio.setEditable(false);
        cbDestino.setEnabled(false);
    }
    public void cargarCombosDesdeDAO(TrasladoDAO dao) throws SQLException {
        cbEmpresa.removeAllItems();
        cbResiduo.removeAllItems();
        cbDestino.removeAllItems();
        cbEnvase.removeAllItems();
        cbTratamiento.removeAllItems();
        cbTransportista.removeAllItems();

        for (String emp : dao.getEmpresasActivas()) cbEmpresa.addItem(emp);
        for (Integer res : dao.getResiduosActivos()) cbResiduo.addItem(res);
        for (String dest : dao.getDestinosActivos()) cbDestino.addItem(dest);
        for (String env : dao.getEnvasesActivos()) cbEnvase.addItem(env);
        for (String trat : dao.getTratamientosActivos()) cbTratamiento.addItem(trat);
        for (String trans : dao.getTransportistasActivos()) cbTransportista.addItem(trans);

        cbEmpresa.setSelectedIndex(-1);
        cbResiduo.setSelectedIndex(-1);
        cbDestino.setSelectedIndex(-1);
        cbEnvase.setSelectedIndex(-1);
        cbTratamiento.setSelectedIndex(-1);
        cbTransportista.setSelectedIndex(-1);
    }

}
