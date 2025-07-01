package traslado;

import traslado.dao.TrasladoDAO;
import traslado.modelo.TrasladoModel;
import menu.MenuPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesTraslado extends JFrame {

    private final TrasladoDAO dao = new TrasladoDAO();
    private GrillaPanelTraslado pnlGrilla;
    private RegistroPanelTraslado pnlRegistro;
    private BotonesPanelTraslado pnlBotones;
    private JLabel statusLabel;
    private static final int FILAS_POR_PAGINA = 10;
    private int paginaActual = 1;
    private int totalPaginas = 1;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesTraslado() {
        setTitle("R1T_TRASLADO");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("R1T_TRASLADO", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelTraslado();
        pnlGrilla = new GrillaPanelTraslado();
        pnlBotones = new BotonesPanelTraslado();

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

        statusLabel = new JLabel("Estado: Esperando acción.");
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
                    statusLabel.setText("Estado: Adicionar – Llena los campos del traslado.");
                }
                case "Modificar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 9).toString();
                    if (!"A".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "Solo se puede modificar traslados activos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    cargarCombos();
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.bloquearCamposClave();
                    CarFlaAct = 1;
                    operacion = 2;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Modificar – Edita los datos necesarios.");
                }
                case "Eliminar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar este traslado?", "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (confirmar != JOptionPane.YES_OPTION) return;
                    pnlRegistro.getTfEstado().setText("*");
                    CarFlaAct = 1;
                    operacion = 3;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Eliminación pendiente de confirmar.");
                }
                case "Inactivar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona un traslado para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    pnlRegistro.getTfEstado().setText("I");
                    CarFlaAct = 1;
                    operacion = 4;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Inactivación lista para actualizar.");
                }
                case "Reactivar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona un traslado para reactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    pnlRegistro.getTfEstado().setText("A");
                    CarFlaAct = 1;
                    operacion = 5;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Reactivación lista para actualizar.");
                }
                case "Actualizar" -> {
                    if (CarFlaAct != 1) return;
                    try {
                        TrasladoModel traslado = pnlRegistro.toModel();

                        // Validación de capacidad solo al adicionar o modificar
                        if (operacion == 1 || operacion == 2) {
                            String destCod = traslado.getDestCod();
                            double cantidadNueva = traslado.getCantidad();

                            double acumulado = dao.obtenerCantidadAcumuladaPorDestino(destCod);
                            double capacidad = new destino.dao.DestinoDAO().obtenerCapacidadDestino(destCod);

                            // Si es actualización, restar el valor anterior antes de verificar
                            if (operacion == 2) {
                                TrasladoModel anterior = dao.findById(
                                        traslado.getEmpNif(), traslado.getResCod(),
                                        traslado.getFechaEnvio(), traslado.getDestCod()
                                );
                                if (anterior != null) {
                                    acumulado -= anterior.getCantidad(); // restamos lo viejo, sumamos lo nuevo
                                }
                            }

                            if (acumulado + cantidadNueva > capacidad) {
                                JOptionPane.showMessageDialog(this,
                                    "⚠ El destino excede su capacidad máxima.\n" +
                                    "Capacidad: " + capacidad + " t\n" +
                                    "Acumulado actual: " + acumulado + " t\n" +
                                    "Intentado trasladar: " + cantidadNueva + " t",
                                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }

                        switch (operacion) {
                            case 1 -> dao.insert(traslado);
                            case 2 -> dao.update(traslado);
                            case 3 -> dao.softDelete(traslado.getEmpNif(), traslado.getResCod(), traslado.getFechaEnvio(), traslado.getDestCod());
                            case 4 -> dao.inactivate(traslado.getEmpNif(), traslado.getResCod(), traslado.getFechaEnvio(), traslado.getDestCod());
                            case 5 -> dao.reactivate(traslado.getEmpNif(), traslado.getResCod(), traslado.getFechaEnvio(), traslado.getDestCod());
                        }

                        cargarTabla();
                        pnlRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        pnlBotones.marcarInactivo();
                        pnlBotones.activarModoNormal();
                        statusLabel.setText("Operación completada exitosamente.");
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
            List<TrasladoModel> lista = dao.findAll();
            model.setRowCount(0);

            int totalRegistros = lista.size();
            totalPaginas = (int) Math.ceil((double) totalRegistros / FILAS_POR_PAGINA);
            if (totalPaginas == 0) totalPaginas = 1;
            if (paginaActual > totalPaginas) paginaActual = totalPaginas;

            int inicio = (paginaActual - 1) * FILAS_POR_PAGINA;
            int fin = Math.min(inicio + FILAS_POR_PAGINA, totalRegistros);

            for (int i = inicio; i < fin; i++) {
                TrasladoModel t = lista.get(i);
                model.addRow(new Object[]{
                    t.getEmpNif(), t.getResCod(), t.getFechaEnvio(), t.getDestCod(),
                    t.getTipoEnvase(),      // ✅ CORRECTO
                    t.getFechaLlegada(),    // ✅ CORRECTO
                    t.getTratamiento(),     // ✅ CORRECTO
                    t.getCantidad(),        // ✅ CORRECTO
                    t.getObservacion(),    
                    t.getTransportista(),  // ✅ CORRECTO
                    t.getEstado()        // ✅ CORRECTO
 
                });
            }

            statusLabel.setText(String.format("Página %d de %d", paginaActual, totalPaginas));
            pnlBotones.actualizarLabelPagina("Página " + paginaActual + " de " + totalPaginas);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCombos() {
        try {
            pnlRegistro.cargarCombosDesdeDAO(dao);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar combos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
