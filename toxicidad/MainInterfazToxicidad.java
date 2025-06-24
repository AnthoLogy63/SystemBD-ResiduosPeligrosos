package toxicidad;

import javax.swing.SwingUtilities;

public class MainInterfazToxicidad {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesToxicidad().setVisible(true));
    }
}
