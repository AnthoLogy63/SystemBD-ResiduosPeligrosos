package menu;

import javax.swing.*;
import java.awt.*;

// IMPORTS DE TUS COMPONENTES
import tipo_transporte.ComponentesTipTran;
import envase.ComponentesEnvase;
import ciudad.ComponentesCiudad;
import region.ComponentesRegion;
import toxicidad.ComponentesToxicidad;
import tratamiento.ComponentesTratamiento;
import tipo_residuo.ComponentesTipoResiduo;
import codigoresiduo.ComponentesCodigoResiduo;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Sistema de Gestión de Residuos Peligrosos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ENCABEZADO
        JLabel titulo = new JLabel("Menú Principal", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);

        // PANEL CENTRAL CON BOTONES
        JPanel panelBotones = new JPanel(new GridLayout(5, 2, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panelBotones.setBackground(Color.WHITE);

        panelBotones.add(crearBoton("TIPO DE TRANSPORTE", e -> new ComponentesTipTran().setVisible(true)));
        panelBotones.add(crearBoton("ENVASE", e -> new ComponentesEnvase().setVisible(true)));
        panelBotones.add(crearBoton("CIUDAD", e -> new ComponentesCiudad().setVisible(true)));
        panelBotones.add(crearBoton("REGIÓN", e -> new ComponentesRegion().setVisible(true)));
        panelBotones.add(crearBoton("TOXICIDAD", e -> new ComponentesToxicidad().setVisible(true)));
        panelBotones.add(crearBoton("TRATAMIENTO", e -> new ComponentesTratamiento().setVisible(true)));
        panelBotones.add(crearBoton("TIPO RESIDUO", e -> new ComponentesTipoResiduo().setVisible(true)));
        panelBotones.add(crearBoton("CÓDIGO RESIDUO", e -> new ComponentesCodigoResiduo().setVisible(true)));

        add(panelBotones, BorderLayout.CENTER);

        // BOTÓN DE SALIDA
        JButton salir = new JButton("Salir del sistema");
        salir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        salir.setBackground(new Color(200, 60, 60));
        salir.setForeground(Color.WHITE);
        salir.setFocusPainted(false);
        salir.addActionListener(e -> System.exit(0));

        JPanel panelSur = new JPanel();
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panelSur.add(salir);
        add(panelSur, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(new Color(230, 240, 255));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btn.addActionListener(action);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
