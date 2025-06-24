package codigoresiduo;

import codigoresiduo.dao.CodigoResiduoDAO;
import codigoresiduo.modelo.CodigoResiduoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesCodigoResiduo extends JFrame {
    private final CodigoResiduoDAO dao = new CodigoResiduoDAO();
    private GrillaPanelCodigoResiduo pnlGrilla;
    private RegistroPanelCodigoResiduo pnlRegistro;
    private BotonesPanelCodigoResiduo pnlBotones;
    private JLabel statusLabel;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesCodigoResiduo() {
        setTitle("GZZ_CODIGORESIDUO");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("GZZ_CODIGORESIDUO", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelCodigoResiduo();
        pnlGrilla = new GrillaPanelCodigoResiduo();
        pnlBotones = new BotonesPanelCodigoResiduo();

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
                    statusLabel.setText("Adicionar: Ingresa código y descripción.");
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
                    statusLabel.setText("Modificar: Cambia la descripción.");
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
                    if (JOptionPane.showConfirmDialog(this, "¿Eliminar?", "Confirmar", JOptionPane.YES_NO_OPTION)
                            != JOptionPane.YES_OPTION) return;
                    pnlRegistro.getTfEstado().setText("*");
                    pnlRegistro.setEditableRegistro(false);
                    CarFlaAct = 1;
                    operacion = 3;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Eliminar: Confirma eliminación.");
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
                    statusLabel.setText("Reactivar: Confirma reactivación.");
                }
                case "Actualizar" -> {
                    if (CarFlaAct != 1) return;
                    String cod = pnlRegistro.getTfCodigo().getText().trim();
                    String desc = pnlRegistro.getTfDescripcion().getText().trim();
                    String est = pnlRegistro.getTfEstado().getText().trim();
                    if ((operacion == 1 || operacion == 2) && (cod.isEmpty() || desc.isEmpty())) {
                        JOptionPane.showMessageDialog(this, "Código y descripción requeridos.");
                        return;
                    }
                    try {
                        switch (operacion) {
                            case 1 -> {
                                if (dao.findById(cod) != null) {
                                    JOptionPane.showMessageDialog(this, "Código ya existe.");
                                    return;
                                }
                                dao.insert(new CodigoResiduoModel(cod, desc, est));
                            }
                            case 2 -> dao.update(new CodigoResiduoModel(cod, desc, est));
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
                        statusLabel.setText("Registro procesado correctamente.");
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
                case "Salir" -> System.exit(0);
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) pnlGrilla.getTable().getModel();
        try {
            List<CodigoResiduoModel> lista = dao.findAll();
            model.setRowCount(0);
            for (CodigoResiduoModel r : lista) {
                model.addRow(new Object[]{r.getCodigo(), r.getDescripcion(), r.getEstado()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }
}
