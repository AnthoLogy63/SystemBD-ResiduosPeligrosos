package region;

import javax.swing.SwingUtilities;

public class MainInterfazRegion {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesRegion().setVisible(true));
    }
}
