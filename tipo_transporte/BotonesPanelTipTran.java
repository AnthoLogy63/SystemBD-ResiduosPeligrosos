package tipo_transporte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class BotonesPanelTipTran extends JPanel {
    private Consumer<String> cmdListener;
    private Color baseColor = Color.WHITE;
    private Color hoverColor = new Color(225, 225, 225);
    private Color activeColor = new Color(173, 216, 230);

    public BotonesPanelTipTran() {
        setBackground(baseColor);
        setLayout(new GridLayout(2, 4, 40, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));

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
                @Override public void mouseEntered(MouseEvent e) {
                    btn.setBackground(hoverColor);
                }
                @Override public void mouseExited(MouseEvent e) {
                    // vuelve a su color actual sin interferir
                    btn.setBackground(btn.getClientProperty("active") == Boolean.TRUE
                        ? activeColor : baseColor);
                }
            });

            btn.addActionListener(e -> {
                if (cmdListener != null) cmdListener.accept(text);
            });

            add(btn);
        }
    }

    public void setActionListener(Consumer<String> listener) {
        this.cmdListener = listener;
    }

    public void setButtonActive(String text, boolean on) {
        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (text.equals(btn.getText())) {
                    btn.putClientProperty("active", on);
                    btn.setBackground(on ? activeColor : baseColor);
                    break;
                }
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
        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String t = btn.getText();
                boolean enable = t.equals("Actualizar") || t.equals("Cancelar") || t.equals("Salir");
                btn.setEnabled(enable);
                btn.putClientProperty("active", (t.equals("Actualizar") || t.equals("Cancelar")) && enable);
            }
        }
    }

    public void activarModoNormal() {
        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String t = btn.getText();
                boolean enable = !("Actualizar".equals(t) || "Cancelar".equals(t));
                btn.setEnabled(enable);
                btn.putClientProperty("active", false);
                btn.setBackground(baseColor); 
            }
        }
    }
}
