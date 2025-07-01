package constituyente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class BotonesPanelConstituyente extends JPanel {
    private Consumer<String> cmdListener;
    private final Color baseColor = Color.WHITE;
    private final Color hoverColor = new Color(225, 225, 225);
    private final Color activeColor = new Color(173, 216, 230);

    private final JButton btnAnterior = new JButton("Anterior");
    private final JButton btnSiguiente = new JButton("Siguiente");
    private final JLabel lblPagina = new JLabel("Página 1 de 1");

    private JPanel panelPaginacion;
    private JPanel panelCentral; // ← referencia directa al panel de botones

    public JPanel getPanelPaginacion() {
        return panelPaginacion;
    }

    public BotonesPanelConstituyente() {
        setLayout(new BorderLayout());
        setBackground(baseColor);

        // Panel de botones principales
        panelCentral = new JPanel(new GridLayout(2, 4, 40, 20));
        panelCentral.setBackground(baseColor);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 25, 0, 25));

        String[] texts = {
            "Adicionar", "Modificar", "Eliminar", "Cancelar",
            "Inactivar", "Reactivar", "Actualizar", "Salir"
        };

        for (String text : texts) {
            JButton btn = new JButton(text);
            btn.setBackground(baseColor);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            btn.setFont(btn.getFont().deriveFont(Font.PLAIN, 13f));

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(btn.getClientProperty("active") == Boolean.TRUE ? activeColor : baseColor);
                }
            });

            btn.addActionListener(e -> {
                if (cmdListener != null) cmdListener.accept(text);
            });

            panelCentral.add(btn);
        }

        // Panel de paginación
        panelPaginacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelPaginacion.setBackground(baseColor);

        btnAnterior.setFocusPainted(false);
        btnSiguiente.setFocusPainted(false);

        btnAnterior.addActionListener(e -> {
            if (cmdListener != null) cmdListener.accept("Anterior");
        });

        btnSiguiente.addActionListener(e -> {
            if (cmdListener != null) cmdListener.accept("Siguiente");
        });

        lblPagina.setFont(lblPagina.getFont().deriveFont(Font.PLAIN, 12f));

        panelPaginacion.add(lblPagina);
        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(btnSiguiente);

        // Agregar ambos paneles al layout
        add(panelPaginacion, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    public void setActionListener(Consumer<String> listener) {
        this.cmdListener = listener;
    }

    public void setButtonActive(String text, boolean on) {
        for (Component comp : panelCentral.getComponents()) {
            if (comp instanceof JButton btn && text.equals(btn.getText())) {
                btn.putClientProperty("active", on);
                btn.setBackground(on ? activeColor : baseColor);
                break;
            }
        }
    }

    public void marcarActivo() {
        setButtonActive("Actualizar", true);
        setButtonActive("Cancelar", true);
    }

    public void marcarInactivo() {
        setButtonActive("Actualizar", false);
        setButtonActive("Cancelar", false);
    }

    public void activarModoEdicion() {
        for (Component comp : panelCentral.getComponents()) {
            if (comp instanceof JButton btn) {
                String t = btn.getText();
                boolean enable = t.equals("Actualizar") || t.equals("Cancelar") || t.equals("Salir");
                btn.setEnabled(enable);
                btn.putClientProperty("active", (t.equals("Actualizar") || t.equals("Cancelar")) && enable);
            }
        }
    }

    public void activarModoNormal() {
        for (Component comp : panelCentral.getComponents()) {
            if (comp instanceof JButton btn) {
                String t = btn.getText();
                boolean enable = !("Actualizar".equals(t) || "Cancelar".equals(t));
                btn.setEnabled(enable);
                btn.putClientProperty("active", false);
                btn.setBackground(baseColor);
            }
        }
    }

    // Getters útiles
    public JButton getBtnAnterior() { return btnAnterior; }
    public JButton getBtnSiguiente() { return btnSiguiente; }
    public void actualizarLabelPagina(String texto) { lblPagina.setText(texto); }
}
