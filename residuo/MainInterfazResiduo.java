package residuo;

import javax.swing.SwingUtilities;

public class MainInterfazResiduo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ComponentesResiduo ventana = new ComponentesResiduo();
            ventana.setVisible(true);
        });
    }
}
