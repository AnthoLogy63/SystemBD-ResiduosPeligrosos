package residuo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class GrillaPanelResiduo extends JPanel {
    private JTable table;

    public GrillaPanelResiduo() {
        setBackground(Color.decode("#FFFFFF"));
        setBorder(BorderFactory.createTitledBorder("Tabla de Residuos"));
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Código", "Empresa", "Toxicidad", "Tipo", "Normativo", "Cantidad", "Observación", "Estado"}, 0
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
            Color c2 = Color.decode("#f7f7f7");
            Color sel = new Color(200, 220, 255);

            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean selRow, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, val, selRow, hasFocus, row, col);
                setBackground(selRow ? sel : (row % 2 == 0 ? c1 : c2));
                setBorder(noFocusBorder);

                // Centrar en las columnas 0 (Código), 2 (Toxicidad), 5 (Cantidad), 7 (Estado)
                if (col == 0 || col == 2 || col == 5 || col == 7) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return this;
            }
        };

        TableColumnModel colModel = table.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); i++) {
            colModel.getColumn(i).setCellRenderer(renderer);
        }

        // Ajustar anchos para mejor visualización
        colModel.getColumn(0).setPreferredWidth(60);   // Código
        colModel.getColumn(1).setPreferredWidth(140);  // Empresa
        colModel.getColumn(2).setPreferredWidth(70);   // Toxicidad
        colModel.getColumn(3).setPreferredWidth(120);  // Tipo
        colModel.getColumn(4).setPreferredWidth(120);  // Normativo
        colModel.getColumn(5).setPreferredWidth(90);   // Cantidad
        colModel.getColumn(6).setPreferredWidth(180);  // Observación
        colModel.getColumn(7).setPreferredWidth(60);   // Estado

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

    public DefaultTableModel getModel() {
        return (DefaultTableModel) table.getModel();
    }
}
