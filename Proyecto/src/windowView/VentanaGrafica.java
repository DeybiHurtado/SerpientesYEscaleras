package windowView;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


public class VentanaGrafica extends JFrame {

    private final CardLayout paneles = new CardLayout();
    private final JPanel panelPrincipal = new JPanel(this.paneles);
    private final Superposicion superposicion = new Superposicion();
    private final Map<String, Pantalla> ventanasEmergentes = new HashMap<>();

    private final PantallaTablero pantallaTablero;
    private final PantallaCarga pantallaCarga;
    private final PantallaGanador pantallaGanador;

    private String pantallaActual = "inicio";
    private boolean guardadoAutomatico = false;
    private DatosPartida partidaActual;

    public VentanaGrafica() {
        this.setTitle("Serpientes y Escaleras");

        this.pantallaTablero = new PantallaTablero(this);
        this.pantallaCarga = new PantallaCarga(this);
        this.pantallaGanador = new PantallaGanador(this);
        
        this.panelPrincipal.add(new PantallaInicio(this), "inicio");
        this.panelPrincipal.add(this.pantallaCarga, "carga");
        this.panelPrincipal.add(this.pantallaTablero, "tablero");
        this.panelPrincipal.add(this.pantallaGanador, "ganador");

        this.ventanasEmergentes.put("modo", new PantallaModo(this));
        this.ventanasEmergentes.put("individual", new PantallaIndividual(this));
        this.ventanasEmergentes.put("jugadores", new PantallaJugadores(this));
        this.ventanasEmergentes.put("ajustes", new PantallaAjustes(this));
        this.ventanasEmergentes.put("reglas", new PantallaReglas(this));
        this.ventanasEmergentes.put("salir", new PantallaSalir(this));
        this.ventanasEmergentes.put("guardar", new PantallaGuardar(this));

        this.setContentPane(this.panelPrincipal);
        this.setGlassPane(this.superposicion); 
        this.panelPrincipal.setPreferredSize(new Dimension(1152, 720));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setMinimumSize(new Dimension(720, 450));
        this.setLocationRelativeTo(null);
        this.configurarTeclas();
    }
 private void configurarTeclas() {
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "cerrar");
        this.getRootPane().getActionMap().put("cerrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                VentanaGrafica.this.cerrarSuperposicion();
            }
        });
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), "ganador");
        this.getRootPane().getActionMap().put("ganador", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                VentanaGrafica.this.mostrarGanador("JUGADOR n");
            }
        });
    }

   
    public void mostrar(String nombrePantalla) {
        this.cerrarSuperposicion();
        this.pantallaActual = nombrePantalla;
        this.paneles.show(this.panelPrincipal, nombrePantalla);
    }

  public void abrirSuperposicion(String nombre) {
        if (this.pantallaActual.equals("tablero")) {
            this.pantallaTablero.pausar();
        }
        this.superposicion.mostrar(this.ventanasEmergentes.get(nombre));
    }

    public void cerrarSuperposicion() {
        if (this.superposicion.estaAbierta()) {
            this.superposicion.ocultar();
            if (this.pantallaActual.equals("tablero")) {
                this.pantallaTablero.reanudar();
            }
        }
    }

     public void prepararPartida(DatosPartida datos) {
        this.partidaActual = datos;
        this.mostrar("carga");
        this.pantallaCarga.iniciar(datos);
    }

      public void empezarPartida() {
        this.mostrar("tablero");
        this.pantallaTablero.iniciarPartida(this.partidaActual);
    }

    public void reiniciarPartida() {
        this.partidaActual.reiniciar();
        this.empezarPartida();
    }

    public void mostrarGanador(String nombre) {
        this.pantallaTablero.detenerPartida();
        this.pantallaGanador.setNombreGanador(nombre.toUpperCase());
        this.mostrar("ganador");
    }

    public void volverAlMenu() {
        this.pantallaTablero.detenerPartida();
        this.mostrar("inicio");
    }

    public void guardarPartida() {
        try {
            this.pantallaTablero.tomarFoto().guardar();
        } catch (IOException error) {
            System.out.println("No se pudo guardar la partida: " + error.getMessage());
        }
    }

    public void setTema(int tema) {
        this.pantallaTablero.setTema(tema);
    }

    public boolean isGuardadoAutomatico() {
        return this.guardadoAutomatico;
    }

    public void setGuardadoAutomatico(boolean guardadoAutomatico) {
        this.guardadoAutomatico = guardadoAutomatico;
    }
}
