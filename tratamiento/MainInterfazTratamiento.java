package tratamiento;

import javax.swing.SwingUtilities;

public class MainInterfazTratamiento {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesTratamiento().setVisible(true));
    }
}
