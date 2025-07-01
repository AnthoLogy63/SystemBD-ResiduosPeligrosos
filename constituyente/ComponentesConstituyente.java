package constituyente;

import constituyente.dao.ConstituyenteDAO;
import constituyente.modelo.ConstituyenteModel;
import menu.MenuPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesConstituyente extends JFrame {
    private ConstituyenteDAO dao = new ConstituyenteDAO();
    private GrillaPanelConstituyente pnlGrilla;
    private RegistroPanelConstituyente pnlRegistro;
    private BotonesPanelConstituyente pnlBotones;
    private JLabel statusLabel;
    private static final int FILAS_POR_PAGINA = 10;
    private int paginaActual = 1;
    private int totalPaginas = 1;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesConstituyente() {
        setTitle("R1Z_CONSTITUYENTE");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(750, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("R1Z_CONSTITUYENTE", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelConstituyente();
        pnlGrilla = new GrillaPanelConstituyente();
        pnlBotones = new BotonesPanelConstituyente();

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

        statusLabel = new JLabel("Estado: Elige una acción para comenzar.");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 12f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        pnlBotones.setActionListener(cmd -> {
            switch (cmd) {
                case "Adicionar" -> {
                    pnlRegistro.limpiar();
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfEstado().setText("A");
                    pnlRegistro.getTfCodigo().setFocusable(true);
                    CarFlaAct = 1;
                    operacion = 1;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Adicionar – Ingresa los datos del constituyente.");
                }
                case "Modificar" -> {
                    int filaM = pnlGrilla.getTable().getSelectedRow();
                    if (filaM < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoM = pnlGrilla.getTable().getValueAt(filaM, 3).toString();
                    if ("*".equals(estadoM) || "I".equals(estadoM)) {
                        JOptionPane.showMessageDialog(this, "No puedes modificar un registro inactivo o eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaM);
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfCodigo().setEditable(false);
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
                    String estadoE = pnlGrilla.getTable().getValueAt(filaE, 3).toString();
                    if ("*".equals(estadoE)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaE);
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "¿Estás seguro de que deseas eliminar este registro?",
                            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirm != JOptionPane.YES_OPTION) return;
                    pnlRegistro.getTfEstado().setText("*");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 3;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Eliminar – Presiona Actualizar para confirmar.");
                }
                case "Inactivar" -> {
                    int filaI = pnlGrilla.getTable().getSelectedRow();
                    if (filaI < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoI = pnlGrilla.getTable().getValueAt(filaI, 3).toString();
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
                    statusLabel.setText("Estado: Inactivar – Presiona Actualizar para confirmar.");
                }
                case "Reactivar" -> {
                    int filaR = pnlGrilla.getTable().getSelectedRow();
                    if (filaR < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para reactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoR = pnlGrilla.getTable().getValueAt(filaR, 3).toString();
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
                    statusLabel.setText("Estado: Reactivar – Presiona Actualizar para confirmar.");
                }
                case "Actualizar" -> {
                    if (CarFlaAct != 1) return;
                    String codStr = pnlRegistro.getTfCodigo().getText().trim();
                    String nom = pnlRegistro.getTfNombre().getText().trim();
                    String obs = pnlRegistro.getTfObservacion().getText().trim();
                    String estStr = pnlRegistro.getTfEstado().getText().trim();

                    if ((operacion == 1 || operacion == 2) && (codStr.isEmpty() || nom.isEmpty())) {
                        JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        int cod = Integer.parseInt(codStr);
                        char est = estStr.isEmpty() ? 'A' : estStr.charAt(0);
                        ConstituyenteModel model = new ConstituyenteModel(cod, nom, obs, est);

                        switch (operacion) {
                            case 1:
                                if (dao.findById(cod) != null) {
                                    JOptionPane.showMessageDialog(this, "Código ya existe.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                dao.insert(model);
                                break;
                            case 2:
                                dao.update(model);
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
                        statusLabel.setText("Registro procesado correctamente.");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Código inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        cargarTabla();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) pnlGrilla.getTable().getModel();
        try {
            List<ConstituyenteModel> lista = dao.findAll();
            model.setRowCount(0);

            int totalRegistros = lista.size();
            totalPaginas = (int) Math.ceil((double) totalRegistros / FILAS_POR_PAGINA);
            if (totalPaginas == 0) totalPaginas = 1;
            if (paginaActual > totalPaginas) paginaActual = totalPaginas;

            int inicio = (paginaActual - 1) * FILAS_POR_PAGINA;
            int fin = Math.min(inicio + FILAS_POR_PAGINA, totalRegistros);

            for (int i = inicio; i < fin; i++) {
                ConstituyenteModel c = lista.get(i);
                model.addRow(new Object[]{
                    String.format("%03d", c.getCodigo()), // ← código con formato 3 dígitos
                    c.getNombre(),
                    c.getObservacion(),
                    c.getEstado()
                });
            }

            statusLabel.setText(String.format("Página %d de %d", paginaActual, totalPaginas));
            pnlBotones.actualizarLabelPagina("Página " + paginaActual + " de " + totalPaginas);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
