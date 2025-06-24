package toxicidad;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import menu.MenuPrincipal;
import toxicidad.dao.ToxicidadDAO;
import toxicidad.modelo.ToxicidadModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesToxicidad extends JFrame {

    private ToxicidadDAO dao = new ToxicidadDAO();
    private GrillaPanelToxicidad panelGrilla;
    private RegistroPanelToxicidad panelRegistro;
    private BotonesPanelToxicidad panelBotones;
    private JLabel statusLabel;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesToxicidad() {
        setTitle("GZZ_TOXICIDAD");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(570, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("GZZ_TOXICIDAD", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        panelRegistro = new RegistroPanelToxicidad();
        panelGrilla = new GrillaPanelToxicidad();
        panelBotones = new BotonesPanelToxicidad();

        JPanel content = new JPanel(new BorderLayout());
        content.add(panelRegistro, BorderLayout.NORTH);
        content.add(panelGrilla, BorderLayout.CENTER);
        content.add(panelBotones, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);

        statusLabel = new JLabel("Estado: Debes adicionar un registro nuevo o elegir una acción.");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 12f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        panelBotones.setActionListener(cmd -> {
            switch (cmd) {
                case "Adicionar" -> {
                    panelRegistro.limpiar();
                    panelRegistro.setEditableRegistro(true);
                    panelRegistro.getTfEstado().setText("A");
                    panelRegistro.getTfCodigo().setFocusable(true);
                    panelRegistro.getTfNombre().setFocusable(true);
                    CarFlaAct = 1;
                    operacion = 1;
                    panelBotones.activarModoEdicion();
                    panelBotones.marcarActivo();
                    statusLabel.setText("Estado: Adicionando nuevo registro...");
                }

                case "Modificar" -> {
                    int fila = panelGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String estado = panelGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("*".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "No puedes modificar un registro eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if ("I".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "No puedes modificar un registro inactivo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    panelRegistro.cargarDesdeGrilla(panelGrilla.getTable(), fila);
                    panelRegistro.setEditableRegistro(true);
                    panelRegistro.getTfCodigo().setEditable(false);
                    CarFlaAct = 1;
                    operacion = 2;
                    panelBotones.activarModoEdicion();
                    panelBotones.marcarActivo();
                    statusLabel.setText("Estado: Modificando registro...");
                }

                case "Eliminar" -> {
                    int fila = panelGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String estado = panelGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("*".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    panelRegistro.cargarDesdeGrilla(panelGrilla.getTable(), fila);
                    panelRegistro.getTfEstado().setText("*");
                    panelRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 3;
                    panelBotones.activarModoEdicion();
                    panelBotones.marcarActivo();
                    statusLabel.setText("Estado: Eliminando registro...");
                }

                case "Inactivar" -> {
                    int fila = panelGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String estado = panelGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("*".equals(estado) || "I".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "Registro ya inactivo o eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    panelRegistro.cargarDesdeGrilla(panelGrilla.getTable(), fila);
                    panelRegistro.getTfEstado().setText("I");
                    panelRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 4;
                    panelBotones.activarModoEdicion();
                    panelBotones.marcarActivo();
                    statusLabel.setText("Estado: Inactivando registro...");
                }

                case "Reactivar" -> {
                    int fila = panelGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para reactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String estado = panelGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("A".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está activo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    panelRegistro.cargarDesdeGrilla(panelGrilla.getTable(), fila);
                    panelRegistro.getTfEstado().setText("A");
                    panelRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 5;
                    panelBotones.activarModoEdicion();
                    panelBotones.marcarActivo();
                    statusLabel.setText("Estado: Reactivando registro...");
                }

                case "Actualizar" -> {
                    if (CarFlaAct != 1) return;

                    String codStr = panelRegistro.getTfCodigo().getText().trim();
                    String nom = panelRegistro.getTfNombre().getText().trim();
                    String est = panelRegistro.getTfEstado().getText().trim();

                    if (codStr.isEmpty() || nom.isEmpty() || est.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Código, nombre y estado no deben estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int cod;
                    try {
                        cod = Integer.parseInt(codStr);
                        if (cod < 1 || cod > 3) {
                            JOptionPane.showMessageDialog(this, "Código debe estar entre 1 y 3.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Código inválido. Debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        switch (operacion) {
                            case 1 -> {
                                if (dao.findById(cod) != null) {
                                    JOptionPane.showMessageDialog(this, "Ese código ya existe.", "Error", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                dao.insert(new ToxicidadModel(cod, nom, est));
                            }
                            case 2 -> dao.update(new ToxicidadModel(cod, nom, est));
                            case 3 -> dao.softDelete(cod);
                            case 4 -> dao.inactivate(cod);
                            case 5 -> dao.reactivate(cod);
                        }

                        cargarTabla();
                        panelRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        panelBotones.marcarInactivo();
                        panelBotones.activarModoNormal();
                        panelRegistro.getTfCodigo().setFocusable(false);
                        panelRegistro.getTfNombre().setFocusable(false);
                        statusLabel.setText("Registro procesado correctamente.");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error en operación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                case "Cancelar" -> {
                    panelRegistro.limpiar();
                    panelRegistro.getTfCodigo().setFocusable(false);
                    panelRegistro.getTfNombre().setFocusable(false);
                    CarFlaAct = 0;
                    operacion = 0;
                    panelBotones.marcarInactivo();
                    panelBotones.activarModoNormal();
                    statusLabel.setText("Operación cancelada.");
                }

                case "Salir" -> {
                    new MenuPrincipal().setVisible(true);
                    dispose();
                }
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) panelGrilla.getTable().getModel();
        try {
            List<ToxicidadModel> lista = dao.findAll();
            model.setRowCount(0);
            for (ToxicidadModel tox : lista) {
                model.addRow(new Object[]{
                    tox.getCodigo(),
                    tox.getNombre(),
                    tox.getEstado()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
