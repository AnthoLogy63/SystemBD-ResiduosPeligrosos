package rescons;

import javax.swing.SwingUtilities;

public class MainInterfazResCons {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesResCons().setVisible(true));
    }
}
