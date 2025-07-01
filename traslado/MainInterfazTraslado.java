package traslado;

import javax.swing.SwingUtilities;

public class MainInterfazTraslado {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesTraslado().setVisible(true));
    }
}
