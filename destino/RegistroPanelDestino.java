package destino;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ItemEvent;

public class RegistroPanelDestino extends JPanel {

    private JTextField tfCodigo;
    private JTextField tfNombre;
    private JComboBox<String> cbCiudad;
    private JTextField tfRegion;
    private JComboBox<String> cbTipoResiduo;
    private JTextField tfCapacidad;
    private JTextField tfCapacidadUsada;
    private JTextField tfFechaCierre;
    private JTextField tfAviso;
    private JTextField tfEstado;
    private JTextField tfObservacion;
    private boolean inicializado = false;
    private boolean eventoManual = false;

    public RegistroPanelDestino() {
        setBackground(Color.WHITE);
        setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Registro de Destino"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 40);
        gbc.anchor = GridBagConstraints.WEST;

        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        tfCodigo = new JTextField(13);
        tfCodigo.setPreferredSize(new Dimension(150, tfCodigo.getPreferredSize().height));
        add(tfCodigo, gbc);
        tfCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String input = tfCodigo.getText().trim();
                if (!input.isEmpty()) {
                    if (input.matches("\\d{1,11}")) {
                        long numero = Long.parseLong(input);
                        tfCodigo.setText(String.format("D%011d", numero));
                    } else if (!input.matches("D\\d{11}")) {
                        JOptionPane.showMessageDialog(null, "Código inválido. Debe ser un número de hasta 12 dígitos o estar en formato DXXXXXXXXXXXX.", "Formato incorrecto", JOptionPane.WARNING_MESSAGE);
                        tfCodigo.setText("");
                    }
                }
            }

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                String input = tfCodigo.getText().trim();
                if (input.startsWith("D") && input.length() == 13) {
                    tfCodigo.setText(input.substring(1));
                }
            }
        });

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        tfNombre = new JTextField();
        tfNombre.setPreferredSize(new Dimension(250, tfNombre.getPreferredSize().height));
        add(tfNombre, gbc);

        // Ciudad
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Ciudad:"), gbc);
        gbc.gridx = 1;
        cbCiudad = new JComboBox<>();
        cbCiudad.setPreferredSize(new Dimension(150, cbCiudad.getPreferredSize().height));
        add(cbCiudad, gbc);

        // Región (texto)
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Región:"), gbc);
        gbc.gridx = 1;
        tfRegion = new JTextField();
        tfRegion.setPreferredSize(new Dimension(150, tfRegion.getPreferredSize().height));
        tfRegion.setEditable(false);
        tfRegion.setText("");
        tfRegion.setForeground(Color.BLACK);
        add(tfRegion, gbc);
        // Evento para autocompletar región
        cbCiudad.addItemListener(e -> {
            if (eventoManual && e.getStateChange() == ItemEvent.SELECTED) {
                String ciudadCod = (String) cbCiudad.getSelectedItem();
                if (ciudadCod != null && !ciudadCod.isEmpty()) {
                    try {
                        String regCod = new destino.dao.DestinoDAO().getRegionByCiudad(ciudadCod);
                        tfRegion.setText(regCod);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al obtener región para la ciudad seleccionada: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    tfRegion.setText("");
                }
            }
        });
        

        // Tipo de Residuo
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Tipo Residuo:"), gbc);
        gbc.gridx = 1;
        cbTipoResiduo = new JComboBox<>();
        cbTipoResiduo.setPreferredSize(new Dimension(100, cbTipoResiduo.getPreferredSize().height));
        add(cbTipoResiduo, gbc);

        // Capacidad
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Capacidad Total (t):"), gbc);
        gbc.gridx = 1;
        tfCapacidad = new JTextField();
        tfCapacidad.setPreferredSize(new Dimension(100, tfCapacidad.getPreferredSize().height));
        add(tfCapacidad, gbc);

        // Capacidad Usada
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Capacidad Usada (t):"), gbc);
        gbc.gridx = 1;
        tfCapacidadUsada = new JTextField();
        tfCapacidadUsada.setPreferredSize(new Dimension(100, tfCapacidadUsada.getPreferredSize().height));
        add(tfCapacidadUsada, gbc);
        tfCapacidadUsada.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String capTotalStr = tfCapacidad.getText().trim();
                String capUsadaStr = tfCapacidadUsada.getText().trim();

                if (!capTotalStr.isEmpty() && !capUsadaStr.isEmpty()) {
                    try {
                        double capacidad = Double.parseDouble(capTotalStr);
                        double usada = Double.parseDouble(capUsadaStr);

                        if (capacidad <= 0) {
                            JOptionPane.showMessageDialog(null, "La capacidad total debe ser mayor que cero.", "Error", JOptionPane.ERROR_MESSAGE);
                            tfCapacidad.setText("");
                            return;
                        }

                        if (usada > capacidad) {
                            JOptionPane.showMessageDialog(null, "La capacidad usada no puede ser mayor a la total.", "Exceso de capacidad", JOptionPane.WARNING_MESSAGE);
                            tfCapacidadUsada.setText("");
                            return;
                        }

                        double porcentaje = (usada / capacidad) * 100;
                        if (porcentaje >= 90) {
                            tfAviso.setText("AL");
                        } else if (porcentaje >= 60) {
                            tfAviso.setText("ME");
                        } else {
                            tfAviso.setText("BA");
                        }

                        if (usada == capacidad) {
                            tfEstado.setText("I");
                            JOptionPane.showMessageDialog(null, "Capacidad máxima alcanzada. El destino ha sido marcado como INACTIVO.", "Destino lleno", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ingrese valores numéricos válidos para capacidad.", "Error", JOptionPane.ERROR_MESSAGE);
                        tfCapacidadUsada.setText("");
                    }
                }
            }
        });

        // Fecha Cierre
        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Fecha Cierre (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        tfFechaCierre = new JTextField();
        tfFechaCierre.setPreferredSize(new Dimension(130, tfFechaCierre.getPreferredSize().height));
        add(tfFechaCierre, gbc);

        // Aviso
        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Nivel de Alerta:"), gbc);
        gbc.gridx = 1;
        tfAviso = new JTextField(3);
        tfAviso.setPreferredSize(new Dimension(60, tfAviso.getPreferredSize().height));
        add(tfAviso, gbc);

        // Estado
        gbc.gridx = 0; gbc.gridy = 9;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        tfEstado = new JTextField(2);
        tfEstado.setHorizontalAlignment(SwingConstants.CENTER);
        tfEstado.setEditable(false);
        tfEstado.setFocusable(false);
        tfEstado.setBackground(Color.WHITE);
        tfEstado.setPreferredSize(new Dimension(60, tfEstado.getPreferredSize().height));
        add(tfEstado, gbc);

        // Observaciones
        gbc.gridx = 0; gbc.gridy = 10;
        add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        tfObservacion = new JTextField();
        tfObservacion.setPreferredSize(new Dimension(250, tfObservacion.getPreferredSize().height));
        add(tfObservacion, gbc);

        inicializado = true;
        setEditableRegistro(false);
    }

    // Getters actualizados
    public JTextField getTfCodigo() { return tfCodigo; }
    public JTextField getTfNombre() { return tfNombre; }
    public JComboBox<String> getCbCiudad() { return cbCiudad; }
    public JTextField getTfRegion() { return tfRegion; }
    public JComboBox<String> getCbTipoResiduo() { return cbTipoResiduo; }
    public JTextField getTfCapacidad() { return tfCapacidad; }
    public JTextField getTfCapacidadUsada() { return tfCapacidadUsada; }
    public JTextField getTfFechaCierre() { return tfFechaCierre; }
    public JTextField getTfAviso() { return tfAviso; }
    public JTextField getTfEstado() { return tfEstado; }
    public JTextField getTfObservacion() { return tfObservacion; }

    public void setEditableRegistro(boolean on) {
        tfCodigo.setEditable(on);
        tfNombre.setEditable(on);
        cbCiudad.setEnabled(on);
        cbTipoResiduo.setEnabled(on);
        tfCapacidad.setEditable(on);
        tfCapacidadUsada.setEditable(on);
        tfFechaCierre.setEditable(on);
        tfAviso.setEditable(on);
        tfObservacion.setEditable(on);
    }

    public void cargarDesdeGrilla(JTable table, int fila) {
        tfCodigo.setText(table.getValueAt(fila, 0).toString());
        tfNombre.setText(table.getValueAt(fila, 1).toString());
        String ciudad = table.getValueAt(fila, 2).toString();
        boolean encontrada = false;
        for (int i = 0; i < cbCiudad.getItemCount(); i++) {
            if (cbCiudad.getItemAt(i).equals(ciudad)) {
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            cbCiudad.addItem(ciudad);  // Lo agrega temporalmente si no estaba
        }
        cbCiudad.setSelectedItem(ciudad);

        tfRegion.setText(table.getValueAt(fila, 3).toString());
        cbTipoResiduo.setSelectedItem(table.getValueAt(fila, 4).toString());
        tfCapacidad.setText(table.getValueAt(fila, 5).toString());
        tfCapacidadUsada.setText(table.getValueAt(fila, 6).toString());
        tfFechaCierre.setText(table.getValueAt(fila, 7).toString());
        tfAviso.setText(table.getValueAt(fila, 8).toString());
        tfEstado.setText(table.getValueAt(fila, 9).toString());
        tfObservacion.setText(table.getValueAt(fila, 10).toString());
    }

    public void limpiar() {
        tfCodigo.setText("");
        tfNombre.setText("");
        eventoManual = false; // Desactiva eventos
        cbCiudad.removeAllItems();
        // llenar combo
        cbCiudad.setSelectedIndex(-1);
        eventoManual = true; // Reactiva eventos
        tfRegion.setText("");
        cbTipoResiduo.setSelectedIndex(-1);
        tfCapacidad.setText("");
        tfCapacidadUsada.setText("");
        tfFechaCierre.setText("");
        tfAviso.setText("");
        tfEstado.setText("");
        tfObservacion.setText("");
        setEditableRegistro(false);
    }
}
