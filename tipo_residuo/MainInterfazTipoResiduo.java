package tipo_residuo;

import javax.swing.SwingUtilities;

public class MainInterfazTipoResiduo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesTipoResiduo().setVisible(true));
    }
}