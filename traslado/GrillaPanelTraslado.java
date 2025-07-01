package traslado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class GrillaPanelTraslado extends JPanel {
    private JTable table;

    public GrillaPanelTraslado() {
        setBackground(Color.decode("#FFFFFF"));
        setBorder(BorderFactory.createTitledBorder("Tabla de Traslados"));
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(
            new Object[]{
                "Empresa",        // EmpNif
                "ResCod",         // ResCod
                "FecEnv",         // TraFecEnv
                "Destino",        // DestCod
                "Envase",         // TraEnv
                "FecLlega",       // TraFecLlega
                "Tratamiento",    // TraTrat
                "Cantidad",       // TraCant
                "Observación",    // TraObs
                "Transportista",  // TransCod
                "Estado"          // EstCod
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
            Color c2 = Color.decode("#f7f7f7");
            Color sel = new Color(200, 220, 255);

            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean selRow, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, val, selRow, hasFocus, row, col);
                setBackground(selRow ? sel : (row % 2 == 0 ? c1 : c2));
                setBorder(noFocusBorder);
                setHorizontalAlignment((col == 1 || col == 7 || col == 10) ? SwingConstants.CENTER : SwingConstants.LEFT);
                return this;
            }
        };

        TableColumnModel colModel = table.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); i++) {
            colModel.getColumn(i).setCellRenderer(renderer);
        }

        // Ajustes de ancho por columna (ajusta si lo deseas)
        colModel.getColumn(0).setPreferredWidth(100);  // Empresa
        colModel.getColumn(1).setPreferredWidth(60);   // ResCod
        colModel.getColumn(2).setPreferredWidth(90);   // FecEnv
        colModel.getColumn(3).setPreferredWidth(100);  // Destino
        colModel.getColumn(4).setPreferredWidth(80);   // Envase
        colModel.getColumn(5).setPreferredWidth(90);   // FecLlega
        colModel.getColumn(6).setPreferredWidth(90);   // Tratamiento
        colModel.getColumn(7).setPreferredWidth(80);   // Cantidad
        colModel.getColumn(8).setPreferredWidth(180);  // Observación
        colModel.getColumn(9).setPreferredWidth(100);  // Transportista
        colModel.getColumn(10).setPreferredWidth(60);  // Estado

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
