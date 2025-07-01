package rescons;

import menu.MenuPrincipal;
import rescons.dao.ResConsDAO;
import rescons.modelo.ResConsModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesResCons extends JFrame {
    private final ResConsDAO dao = new ResConsDAO();
    private GrillaPanelResCons pnlGrilla;
    private RegistroPanelResCons pnlRegistro;
    private BotonesPanelResCons pnlBotones;
    private JLabel statusLabel;
    private static final int FILAS_POR_PAGINA = 10;
    private int paginaActual = 1;
    private int totalPaginas = 1;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesResCons() {
        setTitle("R1T_RES_CONS");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("R1T_RES_CONS", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelResCons();
        pnlGrilla = new GrillaPanelResCons();
        pnlBotones = new BotonesPanelResCons();

        pnlBotones.getBtnAnterior().addActionListener(e -> {
            if (paginaActual > 1) {
                paginaActual--;
                cargarTabla();
            }
        });

        pnlBotones.getBtnSiguiente().addActionListener(e -> {
            if (paginaActual < totalPaginas) {
                paginaActual++;
                cargarTabla();
            }
        });

        JPanel content = new JPanel(new BorderLayout());
        content.add(pnlRegistro, BorderLayout.NORTH);
        content.add(pnlGrilla, BorderLayout.CENTER);

        JPanel panelBotonesContainer = new JPanel(new BorderLayout());
        panelBotonesContainer.setBackground(Color.WHITE);
        panelBotonesContainer.add(pnlBotones.getPanelPaginacion(), BorderLayout.NORTH);
        panelBotonesContainer.add(pnlBotones, BorderLayout.CENTER);
        content.add(panelBotonesContainer, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        statusLabel = new JLabel("Estado: Debes adicionar un registro nuevo o elegir una acción.");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 12f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        pnlBotones.setActionListener(cmd -> {
            switch (cmd) {
                case "Adicionar" -> {
                    pnlRegistro.limpiar();
                    cargarCombos();
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfEstado().setText("A");
                    CarFlaAct = 1;
                    operacion = 1;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Adicionar – Ingresa los datos del registro.");
                }
                case "Modificar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 4).toString();
                    if ("*".equals(estado) || "I".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "No puedes modificar un registro inactivo o eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    cargarCombos();
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getCbEmpresa().setEnabled(false);
                    pnlRegistro.getCbResiduo().setEnabled(false);
                    pnlRegistro.getCbConstituyente().setEnabled(false);
                    CarFlaAct = 1;
                    operacion = 2;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Modificar – Edita el registro seleccionado.");
                }
                case "Eliminar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 4).toString();
                    if ("*".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirm != JOptionPane.YES_OPTION) return;
                    pnlRegistro.getTfEstado().setText("*");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 3;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Eliminar – Confirmar eliminación.");
                }
                case "Inactivar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 4).toString();
                    if ("*".equals(estado) || "I".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "Registro ya inactivo o eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    pnlRegistro.getTfEstado().setText("I");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 4;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Inactivar – Confirmar acción.");
                }
                case "Reactivar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para reactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 4).toString();
                    if ("A".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está activo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    pnlRegistro.getTfEstado().setText("A");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 5;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Reactivar – Confirmar acción.");
                }
                case "Actualizar" -> {
                    if (CarFlaAct != 1) return;

                    String empresa = (String) pnlRegistro.getCbEmpresa().getSelectedItem();
                    String residuo = (String) pnlRegistro.getCbResiduo().getSelectedItem();
                    String constitu = (String) pnlRegistro.getCbConstituyente().getSelectedItem();
                    String cantidad = pnlRegistro.getTfCantidad().getText().trim();
                    String estado = pnlRegistro.getTfEstado().getText().trim();

                    if (empresa == null || residuo == null || constitu == null || cantidad.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        int resCod = Integer.parseInt(residuo);
                        int conCod = Integer.parseInt(constitu);
                        double can = Double.parseDouble(cantidad);

                        ResConsModel obj = new ResConsModel(empresa, resCod, conCod, can, estado);

                        switch (operacion) {
                            case 1:
                                if (dao.findById(empresa, resCod, conCod) != null) {
                                    JOptionPane.showMessageDialog(this, "El registro ya existe.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                dao.insert(obj);
                                break;
                            case 2:
                                dao.update(obj);
                                break;
                            case 3:
                                dao.softDelete(empresa, resCod, conCod);
                                break;
                            case 4:
                                dao.inactivate(empresa, resCod, conCod);
                                break;
                            case 5:
                                dao.reactivate(empresa, resCod, conCod);
                                break;
                        }

                        cargarTabla();
                        pnlRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        pnlBotones.marcarInactivo();
                        pnlBotones.activarModoNormal();
                        statusLabel.setText("Registro procesado exitosamente.");

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Error de formato numérico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                case "Cancelar" -> {
                    pnlRegistro.limpiar();
                    CarFlaAct = 0;
                    operacion = 0;
                    pnlBotones.marcarInactivo();
                    pnlBotones.activarModoNormal();
                    statusLabel.setText("Operación cancelada.");
                }
                case "Salir" -> {
                    dispose();
                }
            }
        });

        cargarCombos();
        cargarTabla();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) pnlGrilla.getTable().getModel();
        try {
            List<ResConsModel> lista = dao.findAll();
            model.setRowCount(0);

            int totalRegistros = lista.size();
            totalPaginas = (int) Math.ceil((double) totalRegistros / FILAS_POR_PAGINA);
            if (totalPaginas == 0) totalPaginas = 1;

            if (paginaActual > totalPaginas) paginaActual = totalPaginas;

            int inicio = (paginaActual - 1) * FILAS_POR_PAGINA;
            int fin = Math.min(inicio + FILAS_POR_PAGINA, totalRegistros);

            for (int i = inicio; i < fin; i++) {
                ResConsModel r = lista.get(i);
                model.addRow(new Object[]{
                    r.getEmpNif(), r.getResCod(), r.getConCod(), r.getCantidad(), r.getEstado()
                });
            }

            statusLabel.setText(String.format("Página %d de %d", paginaActual, totalPaginas));
            pnlBotones.actualizarLabelPagina("Página " + paginaActual + " de " + totalPaginas);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCombos() {
        try {
            JComboBox<String> cbEmp = pnlRegistro.getCbEmpresa();
            cbEmp.removeAllItems();
            for (String s : dao.getEmpresasActivas()) cbEmp.addItem(s);
            cbEmp.setSelectedIndex(-1);

            JComboBox<String> cbRes = pnlRegistro.getCbResiduo();
            cbRes.removeAllItems();
            for (String s : dao.getResiduosActivos()) cbRes.addItem(s);
            cbRes.setSelectedIndex(-1);

            JComboBox<String> cbCon = pnlRegistro.getCbConstituyente();
            cbCon.removeAllItems();
            for (String s : dao.getConstituyentesActivos()) cbCon.addItem(s);
            cbCon.setSelectedIndex(-1);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar combos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
