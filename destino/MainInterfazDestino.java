package destino;

import javax.swing.SwingUtilities;

public class MainInterfazDestino {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesDestino().setVisible(true));
    }
}
