package windowView;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.Timer;

public class PantallaCarga extends Pantalla {

    private static final String[] CONSEJOS = {
            "Cada turno lanzas dos dados y avanzas la suma. ¡El 7 es la suma más frecuente!",
            "Las escaleras te hacen subir y las serpientes te hacen bajar: mira bien el tablero antes de tirar.",
            "Gana quien llega primero a la casilla 100.",
            "Si una serpiente te muerde, no te desanimes: ¡todavía puedes remontar!",
            "Activa el guardado automático en Ajustes para no perder tu partida.",
            "Pulsa la casita durante la partida para guardarla y volver al menú.",
            "Revisa las reglas con el botón \"i\" para conocer los poderes de escaleras y serpientes.",
            "En el modo individual puedes elegir contra cuántas computadoras juegas.",
            "Profe ponganos 5.0 " };

    private static final int MILISEGUNDOS_POR_CUADRO = 40;

    private final VentanaGrafica ventana;
    private final Random azar = new Random();
    private Timer temporizador;
    private double progreso;
    private int indiceDelConsejo;
    private int cuadros;
    private String textoJugadores = "";

    public PantallaCarga(VentanaGrafica ventana) {
        super(1440, 900, "fondos/inicio.png");
        this.ventana = ventana;
    }

    public void iniciar(DatosPartida datos) {
        this.progreso = 0;
        this.cuadros = 0;
        this.indiceDelConsejo = this.azar.nextInt(CONSEJOS.length);

        String nombres = "";
        for (int i = 0; i < datos.getCantidad(); i++) {
            if (i > 0) {
                nombres = nombres + "  ·  ";
            }
            nombres = nombres + datos.getNombre(i);
        }
        this.textoJugadores = nombres;

        if (this.temporizador != null) {
            this.temporizador.stop();
        }
        this.temporizador = new Timer(MILISEGUNDOS_POR_CUADRO, evento -> this.avanzar());
        this.temporizador.start();
    }

    private void avanzar() {
        this.cuadros++;
        this.progreso = Math.min(1, this.progreso + 0.008); // unos 5 segundos en total
        if (this.cuadros % 45 == 0) { // cada 1.8 segundos cambia el consejo
            this.indiceDelConsejo = (this.indiceDelConsejo + 1) % CONSEJOS.length;
        }
        this.repaint();
        if (this.progreso >= 1) {
            this.temporizador.stop();
            this.ventana.empezarPartida();
        }
    }

    @Override
    protected void dibujar(Graphics2D g) {
        // franja oscura abajo para que se lea el texto sobre la imagen
        g.setPaint(new GradientPaint(0, 520, new Color(0, 10, 20, 0), 0, 900, new Color(0, 10, 20, 245)));
        g.fillRect(0, 520, 1440, 380);

        this.textoCentrado(g, "JUGADORES", 720, 585, 24, Recursos.DORADO, 1000);
        this.textoCentrado(g, this.textoJugadores, 720, 630, 32, Recursos.CREMA, 1200);

        this.textoCentrado(g, "CONSEJO", 720, 705, 24, Recursos.DORADO, 400);
        this.textoEnCaja(g, CONSEJOS[this.indiceDelConsejo], 150, 722, 1140, 78, 28, Recursos.CREMA);

     
        int x = 270;
        int y = 830;
        int ancho = 900;
        int alto = 22;
        g.setColor(new Color(0, 10, 20, 220));
        g.fillRoundRect(x, y, ancho, alto, alto, alto);
        g.setColor(Recursos.DORADO);
        g.fillRoundRect(x, y, (int) (ancho * this.progreso), alto, alto, alto);
        g.setColor(Recursos.GRIS_AZUL);
        g.drawRoundRect(x, y, ancho, alto, alto, alto);
        this.textoCentrado(g, "Cargando partida...  " + (int) (this.progreso * 100) + "%", 720, 874, 22,
                Recursos.CREMA, 600);
    }
}
