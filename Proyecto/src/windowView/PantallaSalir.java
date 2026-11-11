package windowView;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.Timer;


public class PantallaSalir extends Pantalla {

    private static final int MILISEGUNDOS_POR_CUADRO = 40;

    private final VentanaGrafica ventana;
    private final BotonImagen botonCancelar;
    private Timer temporizador;
    private double progreso;     
    private int cuadrosDeEspera;  
    private int cuadrosTotales;

    public PantallaSalir(VentanaGrafica ventana) {
        super(1000, 900, "fondos/ajustes.png");
        this.ventana = ventana;
        this.botonCancelar = this.agregar(
                new BotonImagen("CANCELAR", "botones/normal.png", "botones/resaltado.png", 16), 375, 740, 250, 70);
        this.botonCancelar.setSeleccionado(true);
        this.botonCancelar.alHacerClic(evento -> this.cancelar());
    }

    
    @Override
    public void alMostrar() {
        this.progreso = 0;
        this.cuadrosDeEspera = 0;
        this.cuadrosTotales = 0;
        this.botonCancelar.setVisible(true);
        if (this.temporizador != null) {
            this.temporizador.stop();
        }
        this.temporizador = new Timer(MILISEGUNDOS_POR_CUADRO, evento -> this.avanzar());
        this.temporizador.start();
    }

    private void avanzar() {
        this.cuadrosTotales++;
        if (this.progreso < 1) {
            this.progreso = Math.min(1, this.progreso + 0.015);
            if (this.progreso >= 1) {
                this.botonCancelar.setVisible(false); 
            }
        } else {
            this.cuadrosDeEspera++;
            if (this.cuadrosDeEspera > 38) { 
                this.temporizador.stop();
                System.exit(0);
            }
        }
        this.repaint();
    }

    private void cancelar() {
        this.temporizador.stop();
        this.ventana.cerrarSuperposicion();
    }

    @Override
    protected void dibujar(Graphics2D g) {
        this.textoCentrado(g, "SALIR", 500, 170, 70, Recursos.CREMA, 800);

        String puntos = ".".repeat(1 + (this.cuadrosTotales / 8) % 3);
        this.textoCentrado(g, "Cargando datos guardados" + puntos, 500, 330, 36, Recursos.CREMA, 860);

        int x = 150;
        int y = 440;
        int ancho = 700;
        int alto = 36;
        g.setColor(new Color(0, 10, 20, 220));
        g.fillRoundRect(x, y, ancho, alto, alto, alto);
        g.setColor(Recursos.DORADO);
        g.fillRoundRect(x, y, (int) (ancho * this.progreso), alto, alto, alto);
        g.setColor(Recursos.GRIS_AZUL);
        g.drawRoundRect(x, y, ancho, alto, alto, alto);
        this.textoCentrado(g, (int) (this.progreso * 100) + "%", 500, 520, 28, Recursos.CREMA, 200);

        if (this.progreso >= 1) {
            this.textoCentrado(g, "¡Evita que las serpientes te muerdan!", 500, 640, 40, new Color(0xE35D4B), 880);
        }
    }
}
