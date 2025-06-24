package codigoresiduo;

import javax.swing.SwingUtilities;

public class MainInterfazCodigoResiduo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesCodigoResiduo().setVisible(true));
    }
}
