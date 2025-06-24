package ciudad;

import ciudad.dao.CiudadDAO;
import ciudad.modelo.CiudadModel;
import menu.MenuPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesCiudad extends JFrame {
    private CiudadDAO dao = new CiudadDAO();
    private GrillaPanelCiudad pnlGrilla;
    private RegistroPanelCiudad pnlRegistro;
    private BotonesPanelCiudad pnlBotones;
    private JLabel statusLabel;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesCiudad() {
        setTitle("GZZ_CIUDAD");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(620, 570);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("GZZ_CIUDAD", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelCiudad();
        pnlGrilla = new GrillaPanelCiudad();
        pnlBotones = new BotonesPanelCiudad();

        JPanel content = new JPanel(new BorderLayout());
        content.add(pnlRegistro, BorderLayout.NORTH);
        content.add(pnlGrilla, BorderLayout.CENTER);
        content.add(pnlBotones, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);

        statusLabel = new JLabel("Estado: Debes adicionar un registro nuevo o elegir una acción.");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 12f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        pnlBotones.setActionListener(cmd -> {
            switch (cmd) {
                case "Adicionar":
                    pnlRegistro.limpiar();
                    cargarComboRegiones();
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfEstado().setText("A");
                    pnlRegistro.getTfCodigo().setFocusable(true);
                    pnlRegistro.getTfNombre().setFocusable(true);
                    CarFlaAct = 1;
                    operacion = 1;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Adicionar – Ingresa los datos de la ciudad.");
                    break;

                case "Modificar":
                    int filaM = pnlGrilla.getTable().getSelectedRow();
                    if (filaM < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoM = pnlGrilla.getTable().getValueAt(filaM, 2).toString();
                    if ("*".equals(estadoM) || "I".equals(estadoM)) {
                        JOptionPane.showMessageDialog(this, "No puedes modificar un registro inactivo o eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaM);
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfCodigo().setEditable(false);
                    pnlRegistro.getTfEstado().setEditable(false);
                    pnlRegistro.getTfCodigo().setFocusable(false);
                    pnlRegistro.getTfNombre().setFocusable(true);
                    CarFlaAct = 1;
                    operacion = 2;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Modificar – Edita el registro seleccionado.");
                    break;

                case "Eliminar":
                    int filaE = pnlGrilla.getTable().getSelectedRow();
                    if (filaE < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoE = pnlGrilla.getTable().getValueAt(filaE, 2).toString();
                    if ("*".equals(estadoE)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaE);
                    int confirmE = JOptionPane.showConfirmDialog(this,
                            "¿Estás seguro de que deseas eliminar este registro?",
                            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirmE != JOptionPane.YES_OPTION) return;

                    pnlRegistro.getTfEstado().setText("*");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 3;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Eliminar – Confirma para continuar.");
                    break;

                case "Inactivar":
                    int filaI = pnlGrilla.getTable().getSelectedRow();
                    if (filaI < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoI = pnlGrilla.getTable().getValueAt(filaI, 2).toString();
                    if ("*".equals(estadoI) || "I".equals(estadoI)) {
                        JOptionPane.showMessageDialog(this, "Registro inválido para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaI);
                    pnlRegistro.getTfEstado().setText("I");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 4;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Inactivar – Confirmar acción.");
                    break;

                case "Reactivar":
                    int filaR = pnlGrilla.getTable().getSelectedRow();
                    if (filaR < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para reactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoR = pnlGrilla.getTable().getValueAt(filaR, 2).toString();
                    if ("A".equals(estadoR)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está activo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaR);
                    pnlRegistro.getTfEstado().setText("A");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 5;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Reactivar – Confirmar acción.");
                    break;

                case "Actualizar":
                    if (CarFlaAct != 1) return;
                    String cod = pnlRegistro.getTfCodigo().getText().trim();
                    String nom = pnlRegistro.getTfNombre().getText().trim();
                    String reg = (String) pnlRegistro.getCbRegion().getSelectedItem();

                    if ((operacion == 1 || operacion == 2) && (cod.isEmpty() || nom.isEmpty() || reg == null)) {
                        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        switch (operacion) {
                            case 1:
                                if (dao.findById(cod) != null) {
                                    JOptionPane.showMessageDialog(this, "Código ya existe.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                dao.insert(new CiudadModel(cod, nom, pnlRegistro.getTfEstado().getText(), reg));
                                break;
                            case 2:
                                dao.update(new CiudadModel(cod, nom, pnlRegistro.getTfEstado().getText(), reg));
                                break;
                            case 3:
                                dao.softDelete(cod);
                                break;
                            case 4:
                                dao.inactivate(cod);
                                break;
                            case 5:
                                dao.reactivate(cod);
                                break;
                        }
                        cargarTabla();
                        pnlRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        pnlBotones.marcarInactivo();
                        pnlBotones.activarModoNormal();
                        statusLabel.setText("Registro procesado exitosamente.");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "Cancelar":
                    pnlRegistro.limpiar();
                    CarFlaAct = 0;
                    operacion = 0;
                    pnlBotones.marcarInactivo();
                    pnlBotones.activarModoNormal();
                    statusLabel.setText("Operación cancelada.");
                    break;

                case "Salir":
                    new MenuPrincipal().setVisible(true);
                    dispose();
            }
        });

        cargarComboRegiones();
        cargarTabla();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) pnlGrilla.getTable().getModel();
        try {
            List<CiudadModel> lista = dao.findAll();
            model.setRowCount(0);
            for (CiudadModel c : lista) {
                model.addRow(new Object[]{ c.getCodigo(), c.getNombre(), c.getEstado(), c.getRegionCodigo() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarComboRegiones() {
        try {
            List<String> regiones = dao.getRegionesActivas();
            JComboBox<String> combo = pnlRegistro.getCbRegion();
            combo.removeAllItems();
            for (String r : regiones) {
                combo.addItem(r);
            }
            combo.setSelectedIndex(-1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando regiones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
