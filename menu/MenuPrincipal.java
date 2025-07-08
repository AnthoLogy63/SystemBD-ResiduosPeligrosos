package menu;

import javax.swing.*;
import java.awt.*;

// IMPORTS – Tablas Referenciales
import tipo_transporte.ComponentesTipTran;
import envase.ComponentesEnvase;
import ciudad.ComponentesCiudad;
import region.ComponentesRegion;
import toxicidad.ComponentesToxicidad;
import tratamiento.ComponentesTratamiento;
import tipo_residuo.ComponentesTipoResiduo;
import codigoresiduo.ComponentesCodigoResiduo;

// IMPORTS – Tablas Maestras
import empresa.ComponentesEmpresa;
import transportista.ComponentesTransportista;
import destino.ComponentesDestino;
import residuo.ComponentesResiduo;
import constituyente.ComponentesConstituyente;

// IMPORTS – Tablas Operacionales
import traslado.ComponentesTraslado;
import rescons.ComponentesResCons;

// IMPORTS – Reportes y Procesos
import manipulacion_datos.views.VistaPanelPrincipal;
import manipulacion_datos.Triggers.VistaHistorialAdvertencias;
import manipulacion_datos.PAlmacenados.PA_PanelPrincipal;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Sistema de Gestión de Residuos Peligrosos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TÍTULO
        JLabel titulo = new JLabel("Menú Principal", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(Color.WHITE);

        // TABLAS REFERENCIALES
        contenedor.add(crearTituloSeccion("Tablas Referenciales"));
        contenedor.add(crearPanelBotones(new String[]{
            "Tipo Transporte", "Envase", "Ciudad", "Región",
            "Toxicidad", "Tratamiento", "Tipo Residuo", "Código Residuo"
        }, new Runnable[]{
            () -> new ComponentesTipTran().setVisible(true),
            () -> new ComponentesEnvase().setVisible(true),
            () -> new ComponentesCiudad().setVisible(true),
            () -> new ComponentesRegion().setVisible(true),
            () -> new ComponentesToxicidad().setVisible(true),
            () -> new ComponentesTratamiento().setVisible(true),
            () -> new ComponentesTipoResiduo().setVisible(true),
            () -> new ComponentesCodigoResiduo().setVisible(true)
        }, 4));

        // TABLAS MAESTRAS
        contenedor.add(crearTituloSeccion("Tablas Maestras"));
        contenedor.add(crearPanelBotones(new String[]{
            "Empresa", "Transportista", "Destino",
            "Residuo", "Constituyente", "Res-Cons"
        }, new Runnable[]{
            () -> new ComponentesEmpresa().setVisible(true),
            () -> new ComponentesTransportista().setVisible(true),
            () -> new ComponentesDestino().setVisible(true),
            () -> new ComponentesResiduo().setVisible(true),
            () -> new ComponentesConstituyente().setVisible(true),
            () -> new ComponentesResCons().setVisible(true)
        }, 3));

        // TABLAS OPERACIONALES
        contenedor.add(crearTituloSeccion("Tablas Operacionales"));
        contenedor.add(crearPanelBotones(new String[]{
            "Traslado"
        }, new Runnable[]{
            () -> new ComponentesTraslado().setVisible(true),
        }, 1));

        // REPORTES Y PROCESOS
        contenedor.add(crearTituloSeccion("Reportes y Procesos"));
        contenedor.add(crearPanelBotones(new String[]{
            "Vistas", "Logs de Triggers", "Procedimientos"
        }, new Runnable[]{
            this::mostrarVistas,
            this::mostrarHistorialAdvertencias,
            this::ejecutarProcedimientos
        }, 3));

        add(contenedor, BorderLayout.CENTER);

        // BOTÓN SALIR
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

    private JLabel crearTituloSeccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel crearPanelBotones(String[] textos, Runnable[] acciones, int columnas) {
        JPanel panel = new JPanel(new GridLayout(0, columnas, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        for (int i = 0; i < textos.length; i++) {
            JButton btn = new JButton(textos[i]);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBackground(new Color(230, 240, 255));
            btn.setPreferredSize(new Dimension(180, 40));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            int finalI = i;
            btn.addActionListener(e -> acciones[finalI].run());
            panel.add(btn);
        }

        return panel;
    }

    private void mostrarVistas() {
        new VistaPanelPrincipal().setVisible(true);
    }

    private void ejecutarProcedimientos() {
        new PA_PanelPrincipal().setVisible(true); // Ventana para ejecutar PA
    }

    private void mostrarHistorialAdvertencias() {
        new VistaHistorialAdvertencias().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
