package empresa;

import javax.swing.SwingUtilities;

public class MainInterfazEmpresa {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComponentesEmpresa().setVisible(true));
    }
}
