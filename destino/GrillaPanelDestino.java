package destino;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class GrillaPanelDestino extends JPanel {
    private JTable table;

    public GrillaPanelDestino() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Tabla de Destinos"));
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(
            new Object[]{
                "Código", "Nombre", "Ciudad", "Región", "Tipo Res.", 
                "Cap. Total", "Cap. Usada", "F. Cierre", "Aviso", "Estado", "Observaciones"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setFocusable(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            Color c1 = Color.decode("#FFFFFF");
            Color c2 = Color.decode("#F7F7F7");
            Color sel = new Color(200, 220, 255);

            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean selRow, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, val, selRow, hasFocus, row, col);
                setBackground(selRow ? sel : (row % 2 == 0 ? c1 : c2));
                setBorder(noFocusBorder);
                setHorizontalAlignment(col == 0 || col == 5 || col == 6 || col == 8 || col == 9 ? SwingConstants.CENTER : SwingConstants.LEFT);
                return this;
            }
        };

        TableColumnModel colModel = table.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); i++) {
            colModel.getColumn(i).setCellRenderer(renderer);
        }

        // Ajustes de ancho de columnas
        colModel.getColumn(0).setPreferredWidth(130); // Código
        colModel.getColumn(1).setPreferredWidth(150); // Nombre
        colModel.getColumn(2).setPreferredWidth(80);  // Ciudad
        colModel.getColumn(3).setPreferredWidth(80);  // Región
        colModel.getColumn(4).setPreferredWidth(90);  // Tipo Residuo
        colModel.getColumn(5).setPreferredWidth(90);  // Capacidad
        colModel.getColumn(6).setPreferredWidth(90);  // Cap. Usada
        colModel.getColumn(7).setPreferredWidth(110); // Fecha Cierre
        colModel.getColumn(8).setPreferredWidth(60);  // Aviso
        colModel.getColumn(9).setPreferredWidth(50);  // Estado
        colModel.getColumn(10).setPreferredWidth(200); // Observaciones

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlCenter.add(scroll, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }
}
