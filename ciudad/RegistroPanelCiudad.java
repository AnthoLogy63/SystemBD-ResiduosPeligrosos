package ciudad;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Pattern;

public class RegistroPanelCiudad extends JPanel {
    private JTextField tfCodigo;
    private JTextField tfNombre;
    private JTextField tfEstado;
    private JComboBox<String> cbRegion;

    public RegistroPanelCiudad() {
        setBackground(Color.decode("#FFFFFF"));
        setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder("Registro de Ciudad"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 60);
        gbc.anchor = GridBagConstraints.WEST;

        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        tfCodigo = new JTextField(3);
        tfCodigo.setPreferredSize(new Dimension(80, tfCodigo.getPreferredSize().height));
        ((AbstractDocument) tfCodigo.getDocument()).setDocumentFilter(new UsarLetras(3));
        add(tfCodigo, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tfNombre = new JTextField();
        ((AbstractDocument) tfNombre.getDocument()).setDocumentFilter(new UsarAlfaNumerico(30));
        add(tfNombre, gbc);

        // Región (combo)
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(new JLabel("Región:"), gbc);
        gbc.gridx = 1;
        cbRegion = new JComboBox<>();
        cbRegion.setPreferredSize(new Dimension(120, cbRegion.getPreferredSize().height));
        add(cbRegion, gbc);

        // Estado
        gbc.gridx = 0; gbc.gridy = 3;
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

        cbRegion.setEnabled(false);
    }

    public JTextField getTfCodigo() {
        return tfCodigo;
    }

    public JTextField getTfNombre() {
        return tfNombre;
    }

    public JTextField getTfEstado() {
        return tfEstado;
    }

    public JComboBox<String> getCbRegion() {
        return cbRegion;
    }

    public void setEditableRegistro(boolean on) {
        tfCodigo.setEditable(on);
        tfNombre.setEditable(on);
        cbRegion.setEnabled(on);
    }

    public void cargarDesdeGrilla(JTable table, int fila) {
        tfCodigo.setText(table.getValueAt(fila, 0).toString());
        tfNombre.setText(table.getValueAt(fila, 1).toString());
        tfEstado.setText(table.getValueAt(fila, 2).toString());
        cbRegion.setSelectedItem(table.getValueAt(fila, 3).toString());
    }

    public void limpiar() {
        tfCodigo.setText("");
        tfNombre.setText("");
        tfEstado.setText("");
        cbRegion.setSelectedIndex(-1);
        setEditableRegistro(false);
    }

    private static class UsarLetras extends DocumentFilter {
        private final int max;
        private final Pattern pattern = Pattern.compile("[a-zA-Z]*");

        public UsarLetras(int max) {
            this.max = max;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null) return;
            Document doc = fb.getDocument();
            String newText = doc.getText(0, doc.getLength()) + string.toUpperCase();
            if (newText.length() <= max && pattern.matcher(string).matches()) {
                super.insertString(fb, offset, string.toUpperCase(), attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) return;
            Document doc = fb.getDocument();
            String current = doc.getText(0, doc.getLength());
            String before = current.substring(0, offset);
            String after = current.substring(offset + length);
            String newText = before + text + after;

            if (newText.length() <= max && pattern.matcher(text).matches()) {
                super.replace(fb, offset, length, text.toUpperCase(), attrs);
            }
        }
    }

    private static class UsarAlfaNumerico extends DocumentFilter {
        private final int max;
        private final Pattern pattern = Pattern.compile("[a-zA-Z0-9 ]*");

        public UsarAlfaNumerico(int max) {
            this.max = max;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null) return;
            Document doc = fb.getDocument();
            String newText = doc.getText(0, doc.getLength()) + string;
            if (newText.length() <= max && pattern.matcher(string).matches()) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) return;
            Document doc = fb.getDocument();
            String current = doc.getText(0, doc.getLength());
            String before = current.substring(0, offset);
            String after = current.substring(offset + length);
            String newText = before + text + after;

            if (newText.length() <= max && pattern.matcher(text).matches()) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
