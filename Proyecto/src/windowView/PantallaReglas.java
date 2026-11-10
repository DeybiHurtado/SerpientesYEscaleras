package windowView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class PantallaReglas extends Pantalla {

    private static final Color PERGAMINO = new Color(0xE9D8B0);
    private static final Color MARRON = new Color(0x6B4A2B);
    private static final Color TINTA = new Color(0x3A2A18);
    private static final Color COLOR_ESCALERAS = new Color(0xE0B050);
    private static final Color COLOR_SERPIENTES = new Color(0xE35D4B);

    private static final String[][] TARJETAS_BASICAS = {
            { "Objetivo", "Llegar primero a la casilla 100." },
            { "Tu turno", "Cada turno se lanzan dos dados y avanzas la suma." },
            { "Casillas especiales",
                    "Si caes en la base de una escalera subes; si caes en la cabeza de una serpiente bajas." } };

    private static final String[][] PODERES_ESCALERAS = {
            { "Normal", "Subes a la cima." },
            { "Un solo uso", "Subes, pero la escalera desaparece y nadie más podrá usarla." },
            { "Doble turno", "Subes y tiras otra vez de inmediato." },
            { "Doble o nada", "Tiras un dado. Par: subes el doble de la distancia. Impar: te quedas en la base." } };
    private static final String[][] PODERES_SERPIENTES = {
            { "Normal", "Bajas hasta la cola." },
            { "Quita turno", "Bajas y pierdes tu siguiente turno." },
            { "Veneno", "Bajas; en tus próximos 2 turnos avanzas la mitad y luego 3/4." },
            { "Doble o nada", "Tiras un dado. Par: te salvas. Impar: bajas el doble de la distancia." } };

    private final VentanaGrafica ventana;

    public PantallaReglas(VentanaGrafica ventana) {
        super(1200, 900, "fondos/reglas.png");
        this.ventana = ventana;
        BotonImagen botonVolver = this.agregar(
                new BotonImagen("VOLVER", "botones/normal.png", "botones/resaltado.png", 16), 60, 795, 250, 66);
        botonVolver.setSeleccionado(true);
        botonVolver.alHacerClic(evento -> this.ventana.cerrarSuperposicion());
    }

    @Override
    protected void dibujar(Graphics2D g) {
        this.textoCentrado(g, "REGLAS DE JUEGO", 600, 95, 56, Recursos.CREMA, 1000);
        this.dibujarTarjetasBasicas(g);
        this.dibujarColumna(g, 90, "PODERES DE LAS ESCALERAS", COLOR_ESCALERAS, PODERES_ESCALERAS,
                "escaleras/escalera_3.png");
        this.dibujarColumna(g, 615, "PODERES DE LAS SERPIENTES", COLOR_SERPIENTES, PODERES_SERPIENTES,
                "serpientes/serpiente_3.png");
        this.dibujarPie(g);
    }

    private void dibujarTarjetasBasicas(Graphics2D g) {
        int[] posicionesX = { 90, 440, 790 };
        for (int i = 0; i < TARJETAS_BASICAS.length; i++) {
            int x = posicionesX[i];
            int y = 150;
            int ancho = 320;
            int alto = 126;
            g.setColor(PERGAMINO);
            g.fillRoundRect(x, y, ancho, alto, 22, 22);
            g.setColor(MARRON);
            g.setStroke(new BasicStroke(4));
            g.drawRoundRect(x, y, ancho, alto, 22, 22);

            this.escribirSinSombra(g, TARJETAS_BASICAS[i][0].toUpperCase(), x + ancho / 2, y + 28, 20, MARRON, ancho - 60);
            this.textoEnCaja(g, TARJETAS_BASICAS[i][1], x + 18, y + 42, ancho - 36, alto - 48, 17, TINTA);
            this.dibujarCirculoConNumero(g, i + 1, x, y);
        }
    }

    private void dibujarColumna(Graphics2D g, int x, String titulo, Color acento, String[][] poderes,
            String rutaIcono) {
        int ancho = 495;
        int yEncabezado = 300;

        // Encabezado
        g.setColor(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 60));
        g.fillRoundRect(x, yEncabezado, ancho, 52, 20, 20);
        g.setColor(acento);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(x, yEncabezado, ancho, 52, 20, 20);
        java.awt.image.BufferedImage icono = Recursos.imagen(rutaIcono);
        int altoIcono = 44;
        int anchoIcono = icono.getWidth() * altoIcono / icono.getHeight();
        g.drawImage(icono, x + 18, yEncabezado + 4, anchoIcono, altoIcono, null);
        this.textoCentrado(g, titulo, x + ancho / 2 + 20, yEncabezado + 27, 24, acento, ancho - 110);

        for (int i = 0; i < poderes.length; i++) {
            int y = 364 + i * 88;
            g.setColor(new Color(0, 10, 20, 190));
            g.fillRoundRect(x, y, ancho, 80, 18, 18);
            g.setColor(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 150));
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(x, y, ancho, 80, 18, 18);

        
            g.setColor(acento);
            g.fillRoundRect(x + 10, y + 14, 150, 52, 16, 16);
            this.escribirSinSombra(g, poderes[i][0], x + 85, y + 40, 20, TINTA, 134);

           
            this.textoEnCajaIzquierda(g, poderes[i][1], x + 174, y + 4, ancho - 190, 72, 17, Recursos.CREMA);
        }
    }

    private void dibujarPie(Graphics2D g) {
        g.setColor(new Color(0, 10, 20, 190));
        g.fillRoundRect(90, 728, 1020, 44, 18, 18);
        g.setColor(Recursos.DORADO);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(90, 728, 1020, 44, 18, 18);
        this.textoCentrado(g, "Pulsa la casita durante la partida para guardarla y volver al menú.", 600, 751, 20,
                Recursos.CREMA, 980);
    }

    private void dibujarCirculoConNumero(Graphics2D g, int numero, int x, int y) {
        g.setColor(new Color(0xF4ECD2));
        g.fillOval(x - 26, y - 26, 52, 52);
        g.setColor(MARRON);
        g.setStroke(new BasicStroke(4));
        g.drawOval(x - 26, y - 26, 52, 52);
        g.setColor(TINTA);
        g.setFont(Recursos.fuente(30, true));
        String texto = String.valueOf(numero);
        g.drawString(texto, x - g.getFontMetrics().stringWidth(texto) / 2, y + 11);
    }

   private void escribirSinSombra(Graphics2D g, String texto, int centroX, int centroY, float tamano, Color color,
            int anchoMaximo) {
        g.setFont(Recursos.fuente(tamano, true));
        FontMetrics medidas = g.getFontMetrics();
        while (medidas.stringWidth(texto) > anchoMaximo && tamano > 8) {
            tamano = tamano - 1;
            g.setFont(Recursos.fuente(tamano, true));
            medidas = g.getFontMetrics();
        }
        g.setColor(color);
        g.drawString(texto, centroX - medidas.stringWidth(texto) / 2,
                centroY + (medidas.getAscent() - medidas.getDescent()) / 2);
    }
}
