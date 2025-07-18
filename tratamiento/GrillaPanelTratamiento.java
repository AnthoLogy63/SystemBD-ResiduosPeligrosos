package tratamiento;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class GrillaPanelTratamiento extends JPanel {
    private JTable table;

    public GrillaPanelTratamiento() {
        setBackground(Color.decode("#FFFFFF"));
        setBorder(BorderFactory.createTitledBorder("Tabla_Tratamiento"));
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Código", "Nombre", "Estado Registro"}, 0
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
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            Color c1 = Color.decode("#FFFFFF");
            Color c2 = Color.decode("#f7f7f7");
            Color sel = new Color(200, 220, 255);

            @Override
            public Component getTableCellRendererComponent(
                JTable t, Object val, boolean selRow, boolean hasFocus, int row, int col
            ) {
                super.getTableCellRendererComponent(t, val, selRow, hasFocus, row, col);
                setBackground(selRow ? sel : (row % 2 == 0 ? c1 : c2));
                setBorder(noFocusBorder);
                setHorizontalAlignment((col == 0 || col == 2) ? SwingConstants.CENTER : SwingConstants.LEFT);
                return this;
            }
        };

        TableColumnModel colModel = table.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); i++) {
            colModel.getColumn(i).setCellRenderer(renderer);
        }

        colModel.getColumn(0).setPreferredWidth(80);
        colModel.getColumn(1).setPreferredWidth(300);
        colModel.getColumn(2).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.decode("#FFFFFF"));

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.decode("#FFFFFF"));
        pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlCenter.add(scroll, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }
}
