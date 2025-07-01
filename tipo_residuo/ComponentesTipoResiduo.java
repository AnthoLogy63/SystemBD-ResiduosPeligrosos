package tipo_residuo;

import tipo_residuo.dao.TipoResiduoDAO;
import tipo_residuo.modelo.TipoResiduoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import menu.MenuPrincipal;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesTipoResiduo extends JFrame {
    private final TipoResiduoDAO dao = new TipoResiduoDAO();
    private GrillaPanelTipoResiduo pnlGrilla;
    private RegistroPanelTipoResiduo pnlRegistro;
    private BotonesPanelTipoResiduo pnlBotones;
    private JLabel statusLabel;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesTipoResiduo() {
        setTitle("GZZ_TIPORESIDUO");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(570, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("GZZ_TIPORESIDUO", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelTipoResiduo();
        pnlGrilla = new GrillaPanelTipoResiduo();
        pnlBotones = new BotonesPanelTipoResiduo();

        JPanel content = new JPanel(new BorderLayout());
        content.add(pnlRegistro, BorderLayout.NORTH);
        content.add(pnlGrilla, BorderLayout.CENTER);
        content.add(pnlBotones, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);

        statusLabel = new JLabel("Estado: Elige una acción.");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 12f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        pnlBotones.setActionListener(cmd -> {
            switch (cmd) {
                case "Adicionar" -> {
                    pnlRegistro.limpiar();
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfEstado().setText("A");
                    CarFlaAct = 1;
                    operacion = 1;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Adicionar: Ingresa nuevo código y nombre.");
                }
                case "Modificar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila.");
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("*".equals(estado) || "I".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "No puedes modificar un registro eliminado o inactivo.");
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfCodigo().setEditable(false);
                    CarFlaAct = 1;
                    operacion = 2;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Modificar: Cambia el nombre.");
                }
                case "Eliminar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila.");
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("*".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "Ya está eliminado.");
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) return;
                    pnlRegistro.getTfEstado().setText("*");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 3;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Eliminar: Confirma acción.");
                }
                case "Inactivar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila.");
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("I".equals(estado) || "*".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "Registro ya inactivo o eliminado.");
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    pnlRegistro.getTfEstado().setText("I");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 4;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Inactivar: Confirma acción.");
                }
                case "Reactivar" -> {
                    int fila = pnlGrilla.getTable().getSelectedRow();
                    if (fila < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila.");
                        return;
                    }
                    String estado = pnlGrilla.getTable().getValueAt(fila, 2).toString();
                    if ("A".equals(estado)) {
                        JOptionPane.showMessageDialog(this, "Ya está activo.");
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), fila);
                    pnlRegistro.getTfEstado().setText("A");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 5;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Reactivar: Confirma acción.");
                }
                case "Actualizar" -> {
                    if (CarFlaAct != 1) return;
                    String cod = pnlRegistro.getTfCodigo().getText().trim();
                    String nom = pnlRegistro.getTfNombre().getText().trim();
                    String est = pnlRegistro.getTfEstado().getText().trim();
                    if ((operacion == 1 || operacion == 2) && (cod.isEmpty() || nom.isEmpty())) {
                        JOptionPane.showMessageDialog(this, "Código y nombre requeridos.");
                        return;
                    }
                    try {
                        switch (operacion) {
                            case 1 -> {
                                if (dao.findById(cod) != null) {
                                    JOptionPane.showMessageDialog(this, "Código ya existe.");
                                    return;
                                }
                                dao.insert(new TipoResiduoModel(cod, nom, est));
                            }
                            case 2 -> dao.update(new TipoResiduoModel(cod, nom, est));
                            case 3 -> dao.softDelete(cod);
                            case 4 -> dao.inactivate(cod);
                            case 5 -> dao.reactivate(cod);
                        }
                        cargarTabla();
                        pnlRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        pnlBotones.marcarInactivo();
                        pnlBotones.activarModoNormal();
                        statusLabel.setText("Registro procesado.");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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
            List<TipoResiduoModel> lista = dao.findAll();
            model.setRowCount(0);
            for (TipoResiduoModel t : lista) {
                model.addRow(new Object[]{t.getCodigo(), t.getNombre(), t.getEstado()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }
}
