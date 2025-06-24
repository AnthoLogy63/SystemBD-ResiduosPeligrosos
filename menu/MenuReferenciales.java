package menu;

import javax.swing.*;
import java.awt.*;

// Importación de tus interfaces
import tipo_transporte.ComponentesTipTran;
import envase.ComponentesEnvase;
import ciudad.ComponentesCiudad;
import region.ComponentesRegion;
import toxicidad.ComponentesToxicidad;
import tratamiento.ComponentesTratamiento;
import tipo_residuo.ComponentesTipoResiduo;
import codigoresiduo.ComponentesCodigoResiduo;

public class MenuReferenciales extends JFrame {

    public MenuReferenciales() {
        setTitle("Menú de Tablas Referenciales");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridLayout(10, 1, 10, 10));

        add(crearBoton("GZZ_TIPOTRANSPORTE", e -> new ComponentesTipTran().setVisible(true)));
        add(crearBoton("GZZ_ENVASE", e -> new ComponentesEnvase().setVisible(true)));
        add(crearBoton("GZZ_CIUDAD", e -> new ComponentesCiudad().setVisible(true)));
        add(crearBoton("GZZ_REGION", e -> new ComponentesRegion().setVisible(true)));
        add(crearBoton("GZZ_TOXICIDAD", e -> new ComponentesToxicidad().setVisible(true)));
        add(crearBoton("GZZ_TRATAMIENTO", e -> new ComponentesTratamiento().setVisible(true)));
        add(crearBoton("GZZ_TIPORESIDUO", e -> new ComponentesTipoResiduo().setVisible(true)));
        add(crearBoton("GZZ_CODIGORESIDUO", e -> new ComponentesCodigoResiduo().setVisible(true)));

        JButton salir = new JButton("Salir");
        salir.setFont(new Font("Arial", Font.BOLD, 14));
        salir.setBackground(Color.LIGHT_GRAY);
        salir.addActionListener(e -> System.exit(0));
        add(salir);
    }

    // ✅ Método corregido que acepta ActionListener
    private JButton crearBoton(String texto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(230, 240, 250));
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btn.addActionListener(action);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuReferenciales().setVisible(true));
    }
}
