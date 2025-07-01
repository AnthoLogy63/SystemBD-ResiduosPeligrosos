package destino;

import destino.dao.DestinoDAO;
import destino.modelo.DestinoModel;
import menu.MenuPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesDestino extends JFrame {

    private final DestinoDAO dao = new DestinoDAO();
    private GrillaPanelDestino pnlGrilla;
    private RegistroPanelDestino pnlRegistro;
    private BotonesPanelDestino pnlBotones;
    private JLabel statusLabel;
    private static final int FILAS_POR_PAGINA = 10;
    private int paginaActual = 1;
    private int totalPaginas = 1;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesDestino() {
        setTitle("R1M_DESTINO");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("R1M_DESTINO", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelDestino();
        pnlGrilla = new GrillaPanelDestino();
        pnlBotones = new BotonesPanelDestino();

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

        statusLabel = new JLabel("Estado: Selecciona una opción.");
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
                    statusLabel.setText("Estado: Adicionar nuevo destino.");
                }
                case "Modificar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 9).toString();
                    if ("*".equals(estado) || "I".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "No puedes modificar un registro inactivo o eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    cargarCombos();
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfCodigo().setEditable(false);
                    CarFlaAct = 1;
                    operacion = 2;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Modificar destino.");
                }
                case "Eliminar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    pnlRegistro.getTfEstado().setText("*");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 3;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Eliminar – Confirmar eliminación lógica.");
                }
                case "Inactivar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
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
                    String cod = pnlRegistro.getTfCodigo().getText().trim();
                    String nombre = pnlRegistro.getTfNombre().getText().trim();
                    String ciudad = (String) pnlRegistro.getCbCiudad().getSelectedItem();
                    String region = pnlRegistro.getTfRegion().getText().trim();
                    String tipoRes = (String) pnlRegistro.getCbTipoResiduo().getSelectedItem();
                    String capacidadStr = pnlRegistro.getTfCapacidad().getText().trim();
                    String usadaStr = pnlRegistro.getTfCapacidadUsada().getText().trim();
                    String cierre = pnlRegistro.getTfFechaCierre().getText().trim();
                    String aviso = pnlRegistro.getTfAviso().getText().trim();
                    String estado = pnlRegistro.getTfEstado().getText().trim();
                    String obs = pnlRegistro.getTfObservacion().getText().trim();

                    if (cod.isEmpty() || nombre.isEmpty() || ciudad == null || region.isEmpty() || tipoRes == null ||
                        capacidadStr.isEmpty() || usadaStr.isEmpty() || aviso.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Faltan datos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        double capacidad = Double.parseDouble(capacidadStr);
                        double usada = Double.parseDouble(usadaStr);

                        if (capacidad <= 0) {
                            JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (usada > capacidad) {
                            JOptionPane.showMessageDialog(this, "La capacidad usada supera la capacidad total.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        if (usada == capacidad) {
                            estado = "I";
                            JOptionPane.showMessageDialog(this, "Capacidad llena, el destino se marcará como inactivo automáticamente.");
                        }

                        DestinoModel d = new DestinoModel(cod, nombre, ciudad, region, tipoRes, capacidad, usada, cierre, aviso, estado, obs);

                        switch (operacion) {
                            case 1:
                                if (dao.findById(cod) != null) {
                                    JOptionPane.showMessageDialog(this, "El código ya existe.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                dao.insert(d);
                                break;
                            case 2: dao.update(d); break;
                            case 3: dao.softDelete(cod); break;
                            case 4: dao.inactivate(cod); break;
                            case 5: dao.reactivate(cod); break;
                        }

                        cargarTabla();
                        pnlRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        pnlBotones.marcarInactivo();
                        pnlBotones.activarModoNormal();
                        statusLabel.setText("Registro procesado correctamente.");

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
            List<DestinoModel> lista = dao.findAll();
            model.setRowCount(0);

            totalPaginas = Math.max(1, (int) Math.ceil((double) lista.size() / FILAS_POR_PAGINA));
            paginaActual = Math.min(paginaActual, totalPaginas);
            int ini = (paginaActual - 1) * FILAS_POR_PAGINA;
            int fin = Math.min(lista.size(), ini + FILAS_POR_PAGINA);

            for (int i = ini; i < fin; i++) {
                DestinoModel d = lista.get(i);
                model.addRow(new Object[]{
                        d.getCodigo(), d.getNombre(), d.getCiudadCod(), d.getRegionCod(), d.getTipoResiduo(),
                        d.getCapacidad(), d.getCapacidadUsada(), d.getFechaCierre(), d.getAviso(), d.getEstado(), d.getObservacion()
                });
            }

            pnlBotones.actualizarLabelPagina("Página " + paginaActual + " de " + totalPaginas);
            statusLabel.setText("Página " + paginaActual + " de " + totalPaginas);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCombos() {
        try {
            JComboBox<String> cbCiu = pnlRegistro.getCbCiudad();
            cbCiu.removeAllItems();
            for (String c : dao.getCiudadesActivas()) cbCiu.addItem(c);
            cbCiu.setSelectedIndex(-1);

            JComboBox<String> cbTipo = pnlRegistro.getCbTipoResiduo();
            cbTipo.removeAllItems();
            for (String t : dao.getTiposResiduosActivos()) cbTipo.addItem(t);
            cbTipo.setSelectedIndex(-1);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar combos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
