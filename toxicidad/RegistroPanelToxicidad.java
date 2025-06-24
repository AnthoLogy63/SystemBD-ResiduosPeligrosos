package toxicidad;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;

public class RegistroPanelToxicidad extends JPanel {
    private JTextField tfCodigo;
    private JTextField tfNombre;
    private JTextField tfEstado;

    public RegistroPanelToxicidad() {
        setBackground(Color.decode("#FFFFFF"));
        setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder("Registro de Nivel de Toxicidad"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 60);
        gbc.anchor = GridBagConstraints.WEST;

        // Código (máx. 1 dígito)
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        tfCodigo = new JTextField(2);
        tfCodigo.setPreferredSize(new Dimension(60, tfCodigo.getPreferredSize().height));
        ((AbstractDocument) tfCodigo.getDocument()).setDocumentFilter(new UsarNumerico(1));
        add(tfCodigo, gbc);

        // Nombre (máx. 10 caracteres)
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tfNombre = new JTextField();
        ((AbstractDocument) tfNombre.getDocument()).setDocumentFilter(new UsarAlfaNumerico(10));
        add(tfNombre, gbc);

        // Estado
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1;
        tfEstado = new JTextField(2);
        tfEstado.setHorizontalAlignment(SwingConstants.CENTER);
        tfEstado.setEditable(false);
        tfEstado.setFocusable(false);
        tfEstado.setBackground(Color.decode("#FFFFFF"));
        tfEstado.setPreferredSize(new Dimension(60, tfEstado.getPreferredSize().height));
        add(tfEstado, gbc);

        // Inicialmente desactivados
        tfCodigo.setEditable(false);
        tfCodigo.setDisabledTextColor(Color.BLACK);
        tfCodigo.setFocusable(false);

        tfNombre.setEditable(false);
        tfNombre.setDisabledTextColor(Color.BLACK);
        tfNombre.setFocusable(false);
    }

    public JTextField getTfCodigo() { return tfCodigo; }
    public JTextField getTfNombre() { return tfNombre; }
    public JTextField getTfEstado() { return tfEstado; }

    public void setEditableRegistro(boolean on) {
        tfCodigo.setEditable(on);
        tfNombre.setEditable(on);
    }

    public void cargarDesdeGrilla(JTable table, int fila) {
        tfCodigo.setText(table.getValueAt(fila, 0).toString());
        tfNombre.setText(table.getValueAt(fila, 1).toString());
        tfEstado.setText(table.getValueAt(fila, 2).toString());
    }

    public void limpiar() {
        tfCodigo.setText("");
        tfNombre.setText("");
        tfEstado.setText("");
        setEditableRegistro(false);
    }

    // Filtro para campos numéricos
    private static class UsarNumerico extends DocumentFilter {
        private final int max;

        public UsarNumerico(int max) {
            this.max = max;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null || !string.matches("\\d*")) return;
            String text = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
            if (text.length() <= max) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs)
                throws BadLocationException {
            if (string == null || !string.matches("\\d*")) return;
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = current.substring(0, offset) + string + current.substring(offset + length);
            if (newText.length() <= max) {
                super.replace(fb, offset, length, string, attrs);
            }
        }
    }

    // Filtro para campos alfanuméricos y espacios
    private static class UsarAlfaNumerico extends DocumentFilter {
        private final int max;

        public UsarAlfaNumerico(int max) {
            this.max = max;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null || !string.matches("[a-zA-Z0-9 ]*")) return;
            String text = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
            if (text.length() <= max) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs)
                throws BadLocationException {
            if (string == null || !string.matches("[a-zA-Z0-9 ]*")) return;
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = current.substring(0, offset) + string + current.substring(offset + length);
            if (newText.length() <= max) {
                super.replace(fb, offset, length, string, attrs);
            }
        }
    }
}
