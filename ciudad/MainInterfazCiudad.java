package ciudad;

import javax.swing.SwingUtilities;

public class MainInterfazCiudad {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesCiudad().setVisible(true));
    }
}
