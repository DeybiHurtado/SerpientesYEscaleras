package windowView;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import model.RegistroPartida;
import model.ResultadoJugador;

public class PantallaDetallePartida extends Pantalla {

    private static final Color COLOR_ESCALERAS = new Color(0xE0B050);
    private static final Color COLOR_SERPIENTES = new Color(0xE35D4B);

    private final VentanaGrafica ventana;
    private RegistroPartida registro;

    public PantallaDetallePartida(VentanaGrafica ventana) {
        super(1200, 900, "fondos/reglas.png");
        this.ventana = ventana;

        BotonImagen botonCerrar = this.agregar(new BotonImagen("CERRAR", "botones/normal.png", "botones/resaltado.png", 16),
                394, 790, 412, 74);
        botonCerrar.setSeleccionado(true);
        botonCerrar.alHacerClic(evento -> this.ventana.cerrarSuperposicion());
    }

    public void mostrarRegistro(RegistroPartida registro) {
        this.registro = registro;
        this.repaint();
    }

    @Override
    protected void dibujar(Graphics2D g) {
        if (this.registro == null) {
            return;
        }
        this.dibujarEncabezado(g);
        this.dibujarOrdenDeLlegada(g);
        this.dibujarSaltos(g, "ESCALERAS", this.registro.getEscaleras(), 100, COLOR_ESCALERAS);
        this.dibujarSaltos(g, "SERPIENTES", this.registro.getSerpientes(), 610, COLOR_SERPIENTES);
    }

    private void dibujarEncabezado(Graphics2D g) {
        BufferedImage hexagono = Recursos.imagen("botones/resaltado.png");
        Recursos.dibujarEstirada(g, hexagono, 240, 40, 720, 90, 16, 16);
        this.textoCentrado(g, "DETALLE DE LA PARTIDA", 600, 85, 46, Recursos.CREMA, 640);
        this.textoCentrado(g, this.registro.getFecha(), 600, 160, 24, Recursos.GRIS_AZUL, 600);

        Recursos.dibujarEstirada(g, Recursos.imagen("botones/resaltado.png"), 250, 185, 700, 80, 16, 16);
        this.textoCentrado(g, "GANADOR: " + this.registro.getGanador(), 600, 225, 42, Recursos.DORADO, 640);
    }

    private void dibujarOrdenDeLlegada(Graphics2D g) {
        this.textoIzquierda(g, "PUESTO", 200, 295, 20, Recursos.GRIS_AZUL);
        this.textoIzquierda(g, "JUGADOR", 330, 295, 20, Recursos.GRIS_AZUL);
        this.textoIzquierda(g, "CASILLA", 720, 295, 20, Recursos.GRIS_AZUL);
        this.textoIzquierda(g, "JUGADAS", 900, 295, 20, Recursos.GRIS_AZUL);

        ArrayList<ResultadoJugador> orden = this.registro.getOrdenLlegada();
        for (int i = 0; i < orden.size(); i++) {
            ResultadoJugador resultado = orden.get(i);
            int y = 315 + i * 56;
            Color color = Recursos.CREMA;
            String marco = "botones/normal.png";
            if (i == 0) {
                color = Recursos.DORADO;
                marco = "botones/resaltado.png";
            }
            Recursos.dibujarEstirada(g, Recursos.imagen(marco), 150, y, 900, 50, 16, 16);

            String casilla = String.valueOf(resultado.getCasillaFinal());
            if (resultado.getCasillaFinal() >= Casillas.TOTAL) {
                casilla = "META";
            }
            this.textoIzquierda(g, (i + 1) + "°", 200, y + 25, 28, color);
            this.textoIzquierda(g, resultado.getNombre(), 330, y + 25, 28, color);
            this.textoIzquierda(g, casilla, 720, y + 25, 28, color);
            this.textoIzquierda(g, String.valueOf(resultado.getJugadas()), 900, y + 25, 28, color);
        }
    }

    private void dibujarSaltos(Graphics2D g, String titulo, ArrayList<String> saltos, int x, Color colorTitulo) {
        Recursos.dibujarEstirada(g, Recursos.imagen("botones/normal.png"), x, 555, 490, 210, 16, 16);
        this.textoCentrado(g, titulo, x + 245, 590, 30, colorTitulo, 440);
        for (int i = 0; i < saltos.size(); i++) {
            this.textoCentrado(g, saltos.get(i), x + 245, 635 + i * 32, 24, Recursos.CREMA, 440);
        }
    }
}