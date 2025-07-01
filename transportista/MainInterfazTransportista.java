package transportista;

import javax.swing.SwingUtilities;

public class MainInterfazTransportista {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesTransportista().setVisible(true));
    }
}
