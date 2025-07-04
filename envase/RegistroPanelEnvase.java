package envase;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Pattern;

public class RegistroPanelEnvase extends JPanel {
    private JTextField tfCodigo;
    private JTextField tfNombre;
    private JTextField tfEstado;

    public RegistroPanelEnvase() {
        setBackground(Color.decode("#FFFFFF"));
        setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder("Registro de Tipo de Envase"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 60);
        gbc.anchor = GridBagConstraints.WEST;

        // Código (máx. 2 dígitos numéricos)
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        tfCodigo = new JTextField(2);
        tfCodigo.setPreferredSize(new Dimension(60, tfCodigo.getPreferredSize().height));
        ((AbstractDocument) tfCodigo.getDocument()).setDocumentFilter(new UsarNumerico(2));

        // Rellenar con ceros al perder el foco
        tfCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String text = tfCodigo.getText().trim();
                if (!text.isEmpty()) {
                    try {
                        int num = Integer.parseInt(text);
                        tfCodigo.setText(String.format("%02d", num)); // 01, 02, etc.
                    } catch (NumberFormatException ex) {
                        tfCodigo.setText(""); // Limpia si no es válido
                    }
                }
            }
        });

        add(tfCodigo, gbc);

        // Nombre (máx. 20 caracteres alfanuméricos)
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tfNombre = new JTextField();
        ((AbstractDocument) tfNombre.getDocument()).setDocumentFilter(new UsarAlfaNumerico(20));
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

    public JTextField getTfCodigo() {
        return tfCodigo;
    }

    public JTextField getTfNombre() {
        return tfNombre;
    }

    public JTextField getTfEstado() {
        return tfEstado;
    }

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

    private static class UsarNumerico extends DocumentFilter {
        private final int maxDigits;

        public UsarNumerico(int maxDigits) {
            this.maxDigits = maxDigits;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null || !string.matches("\\d*")) return;

            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = new StringBuilder(current).insert(offset, string).toString();

            if (newText.length() <= maxDigits) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null || !text.matches("\\d*")) return;

            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = new StringBuilder(current).replace(offset, offset + length, text).toString();

            if (newText.length() <= maxDigits) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
