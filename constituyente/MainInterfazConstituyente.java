package constituyente;

import javax.swing.SwingUtilities;

public class MainInterfazConstituyente {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesConstituyente().setVisible(true));
    }
}
