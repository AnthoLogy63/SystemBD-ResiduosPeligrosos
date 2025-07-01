package codigoresiduo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import codigoresiduo.dao.CodigoResiduoDAO;
import codigoresiduo.modelo.CodigoResiduoModel;
import menu.MenuPrincipal;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesCodigoResiduo extends JFrame {

    private CodigoResiduoDAO dao = new CodigoResiduoDAO();
    private GrillaPanelCodigoResiduo panelGrilla;
    private RegistroPanelCodigoResiduo panelRegistro;
    private BotonesPanelCodigoResiduo panelBotones;
    private JLabel statusLabel;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesCodigoResiduo() {
        setTitle("GZZ_CODIGORESIDUO");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(570, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("GZZ_CODIGORESIDUO", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        panelRegistro = new RegistroPanelCodigoResiduo();
        panelGrilla = new GrillaPanelCodigoResiduo();
        panelBotones = new BotonesPanelCodigoResiduo();

        JPanel content = new JPanel(new BorderLayout());
        content.add(panelRegistro, BorderLayout.NORTH);
        content.add(panelGrilla, BorderLayout.CENTER);
        content.add(panelBotones, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);

        statusLabel = new JLabel("Estado: Esperando acción.");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 12f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        panelBotones.setActionListener(cmd -> {
            switch (cmd) {
                case "Adicionar" -> {
                    panelRegistro.limpiar();
                    panelRegistro.setEditableRegistro(true);
                    panelRegistro.getTfEstado().setText("A");
                    CarFlaAct = 1;
                    operacion = 1;
                    panelBotones.activarModoEdicion();
                    panelBotones.marcarActivo();
                    statusLabel.setText("Adicionando nuevo código de residuo.");
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
                    statusLabel.setText("Modificando registro...");
                }

                case "Eliminar" -> {
                    int fila = panelGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String estado = panelGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("*".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "Ya está eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    panelRegistro.cargarDesdeGrilla(panelGrilla.getTable(), fila);
                    panelRegistro.getTfEstado().setText("*");
                    panelRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 3;
                    panelBotones.activarModoEdicion();
                    panelBotones.marcarActivo();
                    statusLabel.setText("Eliminando registro...");
                }

                case "Inactivar" -> {
                    int fila = panelGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String estado = panelGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("I".equals(estado) || "*".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "No puedes inactivar un registro ya inactivo o eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    panelRegistro.cargarDesdeGrilla(panelGrilla.getTable(), fila);
                    panelRegistro.getTfEstado().setText("I");
                    panelRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 4;
                    panelBotones.activarModoEdicion();
                    panelBotones.marcarActivo();
                    statusLabel.setText("Inactivando registro...");
                }

                case "Reactivar" -> {
                    int fila = panelGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para reactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String estado = panelGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("A".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "Ya está activo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    panelRegistro.cargarDesdeGrilla(panelGrilla.getTable(), fila);
                    panelRegistro.getTfEstado().setText("A");
                    panelRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 5;
                    panelBotones.activarModoEdicion();
                    panelBotones.marcarActivo();
                    statusLabel.setText("Reactivando registro...");
                }

                case "Actualizar" -> {
                    if (CarFlaAct != 1) return;

                    String codInput = panelRegistro.getTfCodigo().getText().trim();
                    String desc = panelRegistro.getTfDescripcion().getText().trim();
                    String estado = panelRegistro.getTfEstado().getText().trim();

                    if (codInput.isEmpty() || desc.isEmpty() || estado.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int codNum;
                    try {
                        codNum = Integer.parseInt(codInput);
                        if (codNum < 1 || codNum > 999) {
                            JOptionPane.showMessageDialog(this, "El código debe ser un número entre 1 y 999.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Código inválido. Debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String codFormateado = String.format("R%03d", codNum);

                    try {
                        switch (operacion) {
                            case 1 -> {
                                if (dao.findById(codFormateado) != null) {
                                    JOptionPane.showMessageDialog(this, "Ese código ya existe.", "Error", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                dao.insert(new CodigoResiduoModel(codFormateado, desc, estado));
                            }
                            case 2 -> dao.update(new CodigoResiduoModel(codFormateado, desc, estado));
                            case 3 -> dao.softDelete(codFormateado);
                            case 4 -> dao.inactivate(codFormateado);
                            case 5 -> dao.reactivate(codFormateado);
                        }

                        cargarTabla();
                        panelRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        panelBotones.marcarInactivo();
                        panelBotones.activarModoNormal();
                        panelRegistro.getTfCodigo().setFocusable(false);
                        panelRegistro.getTfDescripcion().setFocusable(false);
                        statusLabel.setText("Operación completada.");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                case "Cancelar" -> {
                    panelRegistro.limpiar();
                    CarFlaAct = 0;
                    operacion = 0;
                    panelBotones.marcarInactivo();
                    panelBotones.activarModoNormal();
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
        DefaultTableModel model = (DefaultTableModel) panelGrilla.getTable().getModel();
        try {
            List<CodigoResiduoModel> lista = dao.findAll();
            model.setRowCount(0);
            for (CodigoResiduoModel r : lista) {
                model.addRow(new Object[]{
                    r.getCodigo(),
                    r.getDescripcion(),
                    r.getEstado()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
