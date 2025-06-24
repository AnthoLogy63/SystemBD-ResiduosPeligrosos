package envase;

import javax.swing.SwingUtilities;

public class MainInterfazEnvase {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesEnvase().setVisible(true));
    }
}
