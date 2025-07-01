package transportista;

import menu.MenuPrincipal;
import transportista.dao.TransportistaDAO;
import transportista.modelo.TransportistaModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesTransportista extends JFrame {
    private final TransportistaDAO dao = new TransportistaDAO();
    private GrillaPanelTransportista pnlGrilla;
    private RegistroPanelTransportista pnlRegistro;
    private BotonesPanelTransportista pnlBotones;
    private JLabel statusLabel;
    private static final int FILAS_POR_PAGINA = 10;
    private int paginaActual = 1;
    private int totalPaginas = 1;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesTransportista() {
        setTitle("R1M_TRANSPORTISTA");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(750, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("R1M_TRANSPORTISTA", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelTransportista();
        pnlGrilla = new GrillaPanelTransportista();
        pnlBotones = new BotonesPanelTransportista();

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
                    pnlRegistro.getTfNif().setFocusable(true);
                    CarFlaAct = 1;
                    operacion = 1;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Adicionar – Ingresa los datos del transportista.");
                }
                case "Modificar" -> {
                    int filaM = pnlGrilla.getTable().getSelectedRow();
                    if (filaM < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoM = pnlGrilla.getTable().getValueAt(filaM, 5).toString();
                    if ("*".equals(estadoM) || "I".equals(estadoM)) {
                        JOptionPane.showMessageDialog(this, "No puedes modificar un registro inactivo o eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaM);
                    cargarCombos();
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfNif().setEditable(false);
                    pnlRegistro.getTfNif().setFocusable(false);
                    CarFlaAct = 1;
                    operacion = 2;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Modificar – Edita el registro seleccionado.");
                }
                case "Eliminar" -> {
                    int filaE = pnlGrilla.getTable().getSelectedRow();
                    if (filaE < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoE = pnlGrilla.getTable().getValueAt(filaE, 5).toString();
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
                }
                case "Inactivar" -> {
                    int filaI = pnlGrilla.getTable().getSelectedRow();
                    if (filaI < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoI = pnlGrilla.getTable().getValueAt(filaI, 5).toString();
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
                }
                case "Reactivar" -> {
                    int filaR = pnlGrilla.getTable().getSelectedRow();
                    if (filaR < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para reactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoR = pnlGrilla.getTable().getValueAt(filaR, 5).toString();
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
                }
                case "Actualizar" -> {
                    if (CarFlaAct != 1) return;
                    String nif = pnlRegistro.getTfNif().getText().trim();
                    String nombre = pnlRegistro.getTfNombre().getText().trim();
                    String ciudad = (String) pnlRegistro.getCbCiudad().getSelectedItem();
                    String tipo = (String) pnlRegistro.getCbTipoTransporte().getSelectedItem();
                    String observacion = pnlRegistro.getTfObservacion().getText().trim();
                    String estado = pnlRegistro.getTfEstado().getText();

                    if ((operacion == 1 || operacion == 2) && (nif.isEmpty() || nombre.isEmpty() || ciudad == null || tipo == null)) {
                        JOptionPane.showMessageDialog(this, "NIF, Nombre, Ciudad y Tipo son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        switch (operacion) {
                            case 1:
                                if (dao.findById(nif) != null) {
                                    JOptionPane.showMessageDialog(this, "NIF ya existe.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                dao.insert(new TransportistaModel(nif, nombre, ciudad, tipo, estado, observacion));
                                break;
                            case 2:
                                dao.update(new TransportistaModel(nif, nombre, ciudad, tipo, estado, observacion));
                                break;
                            case 3:
                                dao.softDelete(nif);
                                break;
                            case 4:
                                dao.inactivate(nif);
                                break;
                            case 5:
                                dao.reactivate(nif);
                                break;
                        }

                        cargarTabla();
                        pnlRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        pnlBotones.marcarInactivo();
                        pnlBotones.activarModoNormal();
                        statusLabel.setText("Registro procesado exitosamente.");
                    } catch (SQLException ex) {
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
            List<TransportistaModel> lista = dao.findAll();
            model.setRowCount(0);

            int totalRegistros = lista.size();
            totalPaginas = (int) Math.ceil((double) totalRegistros / FILAS_POR_PAGINA);
            if (totalPaginas == 0) totalPaginas = 1; // 👈 Esto asegura al menos una página

            if (paginaActual > totalPaginas) paginaActual = totalPaginas;

            int inicio = (paginaActual - 1) * FILAS_POR_PAGINA;
            int fin = Math.min(inicio + FILAS_POR_PAGINA, totalRegistros);

            for (int i = inicio; i < fin; i++) {
                TransportistaModel t = lista.get(i);
                model.addRow(new Object[]{
                    t.getNif(), t.getNombre(), t.getCiudadCod(),
                    t.getTipoCod(), t.getEstado(), t.getObservacion()
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
            JComboBox<String> comboCiudad = pnlRegistro.getCbCiudad();
            comboCiudad.removeAllItems();
            for (String ciudad : dao.getCiudadesActivas()) comboCiudad.addItem(ciudad);
            comboCiudad.setSelectedIndex(-1);

            JComboBox<String> comboTipo = pnlRegistro.getCbTipoTransporte();
            comboTipo.removeAllItems();
            for (String tipo : dao.getTiposTransporteActivos()) comboTipo.addItem(tipo);
            comboTipo.setSelectedIndex(-1);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando combos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
