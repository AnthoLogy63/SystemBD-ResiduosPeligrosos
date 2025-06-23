package tipo_transporte;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import tipo_transporte.dao.TipoTransporteDAO;
import tipo_transporte.modelo.TipoTransporteModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesTipTran extends JFrame {
    private TipoTransporteDAO dao = new TipoTransporteDAO();
    private GrillaPanelTipTran pnlGrilla;
    private RegistroPanelTipTran pnlRegistro;
    private BotonesPanelTipTran pnlBotones;
    private JLabel statusLabel;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesTipTran() {
        setTitle("PANEL DE GESTION - TIPO DE TRANSPORTE");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(570, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("TIPO DE TRANSPORTE", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelTipTran();
        pnlGrilla = new GrillaPanelTipTran();
        pnlBotones = new BotonesPanelTipTran();

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
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfEstado().setText("A");
                    pnlRegistro.getTfCodigo().setFocusable(true);
                    pnlRegistro.getTfNombre().setFocusable(true);
                    CarFlaAct = 1;
                    operacion = 1;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado Eligido: Adicionar – Ingresa un nuevo código y nombre.");
                    break;

                case "Modificar":
                    int filaM = pnlGrilla.getTable().getSelectedRow();
                    if (filaM < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoM = pnlGrilla.getTable().getValueAt(filaM, 2).toString();
                    if ("*".equals(estadoM)) {
                        JOptionPane.showMessageDialog(this, "No puedes modificar un registro eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
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
                    statusLabel.setText("Estado Eligido: Modificar – Edita el nombre del registro seleccionado.");
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

                    int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro de que deseas eliminar este registro?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }

                    pnlRegistro.getTfEstado().setText("*");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 3;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado Eligido: Eliminar – Confirmar eliminación del registro seleccionado.");
                    break;

                case "Inactivar":
                    int filaI = pnlGrilla.getTable().getSelectedRow();
                    if (filaI < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoI = pnlGrilla.getTable().getValueAt(filaI, 2).toString();
                    if ("*".equals(estadoI)) {
                        JOptionPane.showMessageDialog(this, "No puedes inactivar un registro eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if ("I".equals(estadoI)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está inactivo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaI);
                    pnlRegistro.getTfEstado().setText("I");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 4;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado Eligido: Inactivar – Confirma para marcar el registro como inactivo.");
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
                    statusLabel.setText("Estado Eligido: Reactivar – Confirma reactivacióon del registro seleccionado.");
                    break;

                case "Actualizar":
                    if (CarFlaAct != 1) return;
                    String code = pnlRegistro.getTfCodigo().getText().trim();
                    String name = pnlRegistro.getTfNombre().getText().trim();

                    if ((operacion == 1 || operacion == 2) && (code.isEmpty() || name.isEmpty())) {
                        JOptionPane.showMessageDialog(this, "CÓDIGO y NOMBRE no pueden estar vacíos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        switch (operacion) {
                            case 1:
                                if (dao.findById(code) != null) {
                                    JOptionPane.showMessageDialog(this, "Ya existe un registro con ese código.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                dao.insert(new TipoTransporteModel(code, name, pnlRegistro.getTfEstado().getText()));
                                break;
                            case 2:
                                dao.update(new TipoTransporteModel(code, name, pnlRegistro.getTfEstado().getText()));
                                break;
                            case 3:
                                dao.softDelete(code);
                                break;
                            case 4:
                                dao.inactivate(code);
                                break;
                            case 5:
                                dao.reactivate(code);
                                break;
                        }
                        cargarTabla();
                        pnlRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        pnlBotones.marcarInactivo();
                        pnlBotones.activarModoNormal();
                        pnlRegistro.getTfCodigo().setFocusable(false);
                        pnlRegistro.getTfNombre().setFocusable(false);
                        statusLabel.setText("Registro procesado exitosamente - Elige una nueva acción para continuar.");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "Cancelar":
                    pnlRegistro.limpiar();
                    pnlRegistro.getTfCodigo().setFocusable(false);
                    pnlRegistro.getTfNombre().setFocusable(false);
                    CarFlaAct = 0;
                    operacion = 0;
                    pnlBotones.marcarInactivo();
                    pnlBotones.activarModoNormal();
                    statusLabel.setText("Operación cancelada. Elige una acción para continuar.");
                    break;

                case "Salir":
                    System.exit(0);
                    break;
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) pnlGrilla.getTable().getModel();
        try {
            List<TipoTransporteModel> lista = dao.findAll();
            model.setRowCount(0);
            for (TipoTransporteModel tt : lista) {
                model.addRow(new Object[]{ tt.getCodigo(), tt.getNombre(), tt.getEstado() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
