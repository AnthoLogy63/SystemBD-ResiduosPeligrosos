package codigoresiduo;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Pattern;

public class RegistroPanelCodigoResiduo extends JPanel {
    private JTextField tfCodigo;
    private JTextField tfDescripcion;
    private JTextField tfEstado;

    public RegistroPanelCodigoResiduo() {
        setBackground(Color.decode("#FFFFFF"));
        setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder("Registro de Código de Residuo"),
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
        tfCodigo = new JTextField(5);
        tfCodigo.setPreferredSize(new Dimension(80, tfCodigo.getPreferredSize().height));
        ((AbstractDocument) tfCodigo.getDocument()).setDocumentFilter(new UsarCodigoResiduo(5));
        add(tfCodigo, gbc);

        // Descripción
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tfDescripcion = new JTextField();
        ((AbstractDocument) tfDescripcion.getDocument()).setDocumentFilter(new UsarTextoLibre(100));
        add(tfDescripcion, gbc);

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

        tfDescripcion.setEditable(false);
        tfDescripcion.setDisabledTextColor(Color.BLACK);
        tfDescripcion.setFocusable(false);
    }

    public JTextField getTfCodigo() {
        return tfCodigo;
    }

    public JTextField getTfDescripcion() {
        return tfDescripcion;
    }

    public JTextField getTfEstado() {
        return tfEstado;
    }

    public void setEditableRegistro(boolean on) {
        tfCodigo.setEditable(on);
        tfCodigo.setFocusable(on);
        tfDescripcion.setEditable(on);
        tfDescripcion.setFocusable(on);
    }

    public void cargarDesdeGrilla(JTable table, int fila) {
        tfCodigo.setText(table.getValueAt(fila, 0).toString());
        tfDescripcion.setText(table.getValueAt(fila, 1).toString());
        tfEstado.setText(table.getValueAt(fila, 2).toString());
    }

    public void limpiar() {
        tfCodigo.setText("");
        tfDescripcion.setText("");
        tfEstado.setText("");
        setEditableRegistro(false);
    }

    private static class UsarCodigoResiduo extends DocumentFilter {
        private final int maxLength;
        private final Pattern pattern = Pattern.compile("[A-Z0-9]*");

        public UsarCodigoResiduo(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && pattern.matcher(string).matches()) {
                Document doc = fb.getDocument();
                String newText = doc.getText(0, doc.getLength()) + string;
                if (newText.length() <= maxLength) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && pattern.matcher(text).matches()) {
                Document doc = fb.getDocument();
                String current = doc.getText(0, doc.getLength());
                String before = current.substring(0, offset);
                String after = current.substring(offset + length);
                String newText = before + text + after;
                if (newText.length() <= maxLength) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        }
    }

    private static class UsarTextoLibre extends DocumentFilter {
        private final int maxLength;

        public UsarTextoLibre(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null) {
                Document doc = fb.getDocument();
                String newText = doc.getText(0, doc.getLength()) + string;
                if (newText.length() <= maxLength) {
                    super.insertString(fb, offset, string, attr);
                }
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null) {
                Document doc = fb.getDocument();
                String current = doc.getText(0, doc.getLength());
                String before = current.substring(0, offset);
                String after = current.substring(offset + length);
                String newText = before + text + after;
                if (newText.length() <= maxLength) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        }
    }
}
