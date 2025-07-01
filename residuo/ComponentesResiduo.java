
package residuo;

import residuo.dao.ResiduoDAO;
import residuo.modelo.ResiduoModel;
import menu.MenuPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentesResiduo extends JFrame {
    private ResiduoDAO dao = new ResiduoDAO();
    private GrillaPanelResiduo pnlGrilla;
    private RegistroPanelResiduo pnlRegistro;
    private BotonesPanelResiduo pnlBotones;
    private JLabel statusLabel;
    private static final int FILAS_POR_PAGINA = 10;
    private int paginaActual = 1;
    private int totalPaginas = 1;
    private int CarFlaAct = 0;
    private int operacion = 0;

    public ComponentesResiduo() {
        setTitle("R1T_RESIDUO");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F1F3F8"));

        JLabel lblTitulo = new JLabel("R1T_RESIDUO", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        pnlRegistro = new RegistroPanelResiduo();
        pnlRegistro.setEditableRegistro(false);
        pnlGrilla = new GrillaPanelResiduo();
        pnlBotones = new BotonesPanelResiduo();

        cargarCombosReferenciales();

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

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(pnlRegistro, BorderLayout.NORTH);
        centro.add(pnlGrilla, BorderLayout.CENTER);
        centro.add(pnlBotones, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);

        statusLabel = new JLabel("Estado: Elija una acción para comenzar.");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 12f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        pnlBotones.setActionListener(cmd -> {
            switch (cmd) {
                case "Adicionar":
                pnlRegistro.limpiar(); 
                pnlRegistro.setEditableRegistro(true);
                pnlRegistro.getTfEstado().setText("A"); // ← muestra el estado 'A'
                pnlRegistro.setResEst('A');             // ← asegura que el modelo tenga el estado
                CarFlaAct = 1;
                operacion = 1;
                pnlBotones.activarModoEdicion();
                pnlBotones.marcarActivo();
                statusLabel.setText("Estado: Adicionar – Completa los campos.");
                break;


                case "Modificar":
                    int filaM = pnlGrilla.getTable().getSelectedRow();
                    if (filaM < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaM);
                    pnlRegistro.setEditableRegistro(true);
                    pnlRegistro.getTfCodigo().setEditable(false);
                    CarFlaAct = 1;
                    operacion = 2;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Modificar – Edita el registro.");
                    break;

                case "Eliminar":
                    int filaE = pnlGrilla.getTable().getSelectedRow();
                    if (filaE < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoE = pnlGrilla.getTable().getValueAt(filaE, 7).toString();
                    if ("*".equals(estadoE)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    int confirmE = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirmE != JOptionPane.YES_OPTION) return;

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaE);
                    pnlRegistro.setEditableRegistro(false);
                    pnlRegistro.setResEst('*');
                    pnlRegistro.getTfEstado().setText("*");
                    CarFlaAct = 1;
                    operacion = 3;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Eliminar – Presiona Actualizar para confirmar.");
                    break;

                case "Inactivar":
                    int filaI = pnlGrilla.getTable().getSelectedRow();
                    if (filaI < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoI = pnlGrilla.getTable().getValueAt(filaI, 7).toString();
                    if ("*".equals(estadoI) || "I".equals(estadoI)) {
                        JOptionPane.showMessageDialog(this, "Registro inválido para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaI);
                    pnlRegistro.setEditableRegistro(false);
                    pnlRegistro.setResEst('I');
                    pnlRegistro.getTfEstado().setText("I");
                    CarFlaAct = 1;
                    operacion = 4;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Inactivar – Presiona Actualizar para confirmar.");
                    break;

                case "Reactivar":
                    int filaR = pnlGrilla.getTable().getSelectedRow();
                    if (filaR < 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una fila para reactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String estadoR = pnlGrilla.getTable().getValueAt(filaR, 7).toString();
                    if ("A".equals(estadoR)) {
                        JOptionPane.showMessageDialog(this, "El registro ya está activo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    pnlRegistro.cargarDesdeGrilla(pnlGrilla.getTable(), filaR);
                    pnlRegistro.setEditableRegistro(false);
                    pnlRegistro.setResEst('A');
                    pnlRegistro.getTfEstado().setText("A");
                    CarFlaAct = 1;
                    operacion = 5;
                    pnlBotones.activarModoEdicion();
                    pnlBotones.marcarActivo();
                    statusLabel.setText("Estado: Reactivar – Presiona Actualizar para confirmar.");
                    break;

                case "Actualizar":
                    if (CarFlaAct != 1) return;
                    try {
                        ResiduoModel r = pnlRegistro.obtenerDesdeFormulario();
                        switch (operacion) {
                            case 1 -> dao.insert(r);
                            case 2 -> dao.update(r);
                            case 3 -> dao.delete(r.getResCod()); // ← ya lo tienes
                            case 4 -> {
                                r.setResEst('I');
                                dao.update(r);
                            }
                            case 5 -> {
                                r.setResEst('A');
                                dao.update(r);
                            }
                        }
                        cargarTabla();
                        pnlRegistro.limpiar();
                        CarFlaAct = 0;
                        operacion = 0;
                        pnlBotones.marcarInactivo();
                        pnlBotones.activarModoNormal();
                        statusLabel.setText("Registro procesado.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                    break;


                case "Cancelar":
                    pnlRegistro.limpiar();
                    CarFlaAct = 0;
                    operacion = 0;
                    pnlBotones.marcarInactivo();
                    pnlBotones.activarModoNormal();
                    statusLabel.setText("Operación cancelada.");
                    break;

                case "Salir":
                    dispose();
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) pnlGrilla.getTable().getModel();
        try {
            List<ResiduoModel> lista = dao.findAll();
            model.setRowCount(0);

            int totalRegistros = lista.size();
            totalPaginas = (int) Math.ceil((double) totalRegistros / FILAS_POR_PAGINA);

            int inicio = (paginaActual - 1) * FILAS_POR_PAGINA;
            int fin = Math.min(inicio + FILAS_POR_PAGINA, totalRegistros);

            for (int i = inicio; i < fin; i++) {
                ResiduoModel r = lista.get(i);
                String estadoTexto = switch (r.getResEst()) {
                    case 'I' -> "I";
                    case '*' -> "*";
                    default -> "A";
                };

                model.addRow(new Object[]{
                    String.format("%06d", r.getResCod()),  // ← Aquí el cambio
                    r.getEmpNif(), r.getResTox(),
                    r.getTipoResCod(), r.getCodResNorm(),
                    r.getResCant(), r.getResObs(), estadoTexto
                });
            }

            if (totalPaginas == 0) totalPaginas = 1;
            if (paginaActual > totalPaginas) paginaActual = totalPaginas;

            pnlBotones.actualizarLabelPagina("Página " + paginaActual + " de " + totalPaginas);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }


    private void cargarCombosReferenciales() {
        try {
            ResiduoDAO dao = new ResiduoDAO();

            pnlRegistro.cargarEmpresas(dao.getEmpresasActivas());
            pnlRegistro.cargarTipos(dao.getTiposResiduoActivos());
            pnlRegistro.cargarToxicidades(dao.getToxicidadesActivas());
            pnlRegistro.cargarCodigosResiduo(dao.getCodigosResiduoActivos());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando combos: " + e.getMessage());
        }
    }
}
