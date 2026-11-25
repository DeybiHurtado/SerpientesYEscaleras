package windowView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class PantallaReglas extends Pantalla {

    private static final Color PERGAMINO = new Color(0xE9D8B0);
    private static final Color MARRON = new Color(0x6B4A2B);
    private static final Color TINTA = new Color(0x3A2A18);
    private static final Color COLOR_ESCALERAS = new Color(0xE0B050);
    private static final Color COLOR_SERPIENTES = new Color(0xE35D4B);

    private static final String[][] TARJETAS_BASICAS = {
            { "Objetivo", "Debes caer exactamente en 100; si te pasas, retrocedes la diferencia." },
            { "Tu turno", "Dos dados; a menos de 6 casillas, uno. Un par da otro turno (no se encadena)." },
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
        this.textoCentradoReglas(g, "REGLAS DE JUEGO", 600, 95, 58, Recursos.CREMA, 1000);
        this.dibujarTarjetasBasicas(g);
        this.dibujarColumna(g, 90, "PODERES DE LAS ESCALERAS", COLOR_ESCALERAS, PODERES_ESCALERAS, true);
        this.dibujarColumna(g, 615, "PODERES DE LAS SERPIENTES", COLOR_SERPIENTES, PODERES_SERPIENTES, false);
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

            this.escribirSinSombra(g, TARJETAS_BASICAS[i][0].toUpperCase(), x + ancho / 2, y + 28, 22, MARRON, ancho - 60);
            this.textoEnCajaReglas(g, TARJETAS_BASICAS[i][1], x + 18, y + 42, ancho - 36, alto - 48, 19,
                    TINTA, true);
            this.dibujarCirculoConNumero(g, i + 1, x, y);
        }
    }

    private void dibujarColumna(Graphics2D g, int x, String titulo, Color acento, String[][] poderes,
            boolean esEscalera) {
        int ancho = 495;
        int yEncabezado = 300;

        // Encabezado
        g.setColor(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 60));
        g.fillRoundRect(x, yEncabezado, ancho, 52, 20, 20);
        g.setColor(acento);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(x, yEncabezado, ancho, 52, 20, 20);
        this.dibujarIconoEncabezado(g, x + 18, yEncabezado + 4, acento, esEscalera);
        this.textoCentradoReglas(g, titulo, x + ancho / 2 + 20, yEncabezado + 27, 26, acento, ancho - 110);

        for (int i = 0; i < poderes.length; i++) {
            int y = 364 + i * 88;
            g.setColor(new Color(0, 10, 20, 190));
            g.fillRoundRect(x, y, ancho, 80, 18, 18);
            g.setColor(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 150));
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(x, y, ancho, 80, 18, 18);

        
            g.setColor(acento);
            g.fillRoundRect(x + 10, y + 14, 150, 52, 16, 16);
            this.escribirSinSombra(g, poderes[i][0], x + 85, y + 40, 22, TINTA, 134);

           
            this.textoEnCajaReglas(g, poderes[i][1], x + 174, y + 4, ancho - 190, 72, 19, Recursos.CREMA,
                    false);
        }
    }

    private void dibujarIconoEncabezado(Graphics2D g, int x, int y, Color color, boolean esEscalera) {
        Graphics2D icono = (Graphics2D) g.create(x, y, 44, 44);
        icono.setColor(color);
        icono.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (esEscalera) {
            Path2D.Double rieles = new Path2D.Double();
            rieles.moveTo(10, 40);
            rieles.lineTo(10, 4);
            rieles.moveTo(34, 40);
            rieles.lineTo(34, 4);
            icono.draw(rieles);

            Path2D.Double peldaños = new Path2D.Double();
            for (int i = 0; i < 4; i++) {
                double yPeldaño = 11 + i * 9;
                peldaños.moveTo(10, yPeldaño);
                peldaños.lineTo(34, yPeldaño);
            }
            icono.draw(peldaños);
        } else {
            Path2D.Double cuerpo = new Path2D.Double();
            cuerpo.moveTo(31, 12);
            cuerpo.curveTo(24, 6, 14, 11, 13, 19);
            cuerpo.curveTo(11, 29, 22, 33, 29, 26);
            cuerpo.curveTo(36, 19, 28, 12, 22, 16);
            cuerpo.curveTo(17, 19, 21, 24, 25, 21);
            icono.draw(cuerpo);
            icono.fillRoundRect(29, 7, 11, 9, 5, 5);
            icono.setColor(Recursos.CREMA);
            icono.fillOval(36, 9, 2, 2);
        }
        icono.dispose();
    }

    private void dibujarPie(Graphics2D g) {
        g.setColor(new Color(0, 10, 20, 190));
        g.fillRoundRect(90, 728, 1020, 44, 18, 18);
        g.setColor(Recursos.DORADO);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(90, 728, 1020, 44, 18, 18);
        this.textoCentradoReglas(g, "Pulsa la casita durante la partida para guardarla y volver al menú.", 600, 751,
                22, Recursos.CREMA, 980);
    }

    private void dibujarCirculoConNumero(Graphics2D g, int numero, int x, int y) {
        g.setColor(new Color(0xF4ECD2));
        g.fillOval(x - 26, y - 26, 52, 52);
        g.setColor(MARRON);
        g.setStroke(new BasicStroke(4));
        g.drawOval(x - 26, y - 26, 52, 52);
        g.setColor(TINTA);
        g.setFont(Recursos.fuente(32, true));
        String texto = String.valueOf(numero);
        g.drawString(texto, x - g.getFontMetrics().stringWidth(texto) / 2, y + 11);
    }

    private void textoCentradoReglas(Graphics2D g, String texto, int centroX, int centroY, float tamano, Color color,
            int anchoMaximo) {
        Font fuente = Recursos.fuente(tamano, true);
        FontMetrics medidas = g.getFontMetrics(fuente);
        while (medidas.stringWidth(texto) > anchoMaximo && tamano > 8) {
            tamano--;
            fuente = Recursos.fuente(tamano, true);
            medidas = g.getFontMetrics(fuente);
        }

        int x = centroX - medidas.stringWidth(texto) / 2;
        int y = centroY + (medidas.getAscent() - medidas.getDescent()) / 2;
        int sombra = Math.max(2, Math.round(tamano * 0.04f));
        g.setFont(fuente);
        g.setColor(new Color(0, 0, 0, 170));
        g.drawString(texto, x + sombra, y + sombra);
        g.setColor(color);
        g.drawString(texto, x, y);
    }

    private void textoEnCajaReglas(Graphics2D g, String texto, int x, int y, int ancho, int alto, float tamano,
            Color color, boolean centrado) {
        Font fuente = Recursos.fuente(tamano, false);
        FontMetrics medidas = g.getFontMetrics(fuente);
        List<String> lineas = this.ajustarLineas(texto, ancho, medidas);
        while (lineas.size() * medidas.getHeight() > alto && tamano > 12) {
            tamano--;
            fuente = Recursos.fuente(tamano, false);
            medidas = g.getFontMetrics(fuente);
            lineas = this.ajustarLineas(texto, ancho, medidas);
        }

        int altoLinea = medidas.getHeight();
        int yLinea = y + (alto - altoLinea * lineas.size()) / 2 + medidas.getAscent();
        g.setFont(fuente);
        g.setColor(color);
        for (String linea : lineas) {
            int xLinea = centrado ? x + (ancho - medidas.stringWidth(linea)) / 2 : x;
            g.drawString(linea, xLinea, yLinea);
            yLinea += altoLinea;
        }
    }

    private List<String> ajustarLineas(String texto, int ancho, FontMetrics medidas) {
        List<String> lineas = new ArrayList<>();
        String lineaActual = "";
        for (String palabra : texto.split("\\s+")) {
            String prueba = lineaActual.isEmpty() ? palabra : lineaActual + " " + palabra;
            if (medidas.stringWidth(prueba) > ancho && !lineaActual.isEmpty()) {
                lineas.add(lineaActual);
                lineaActual = palabra;
            } else {
                lineaActual = prueba;
            }
        }
        if (!lineaActual.isEmpty()) {
            lineas.add(lineaActual);
        }
        return lineas;
    }

    private void escribirSinSombra(Graphics2D g, String texto, int centroX, int centroY, float tamano, Color color,
            int anchoMaximo) {
        Font fuente = Recursos.fuente(tamano, true);
        g.setFont(fuente);
        FontMetrics medidas = g.getFontMetrics();
        while (medidas.stringWidth(texto) > anchoMaximo && tamano > 8) {
            tamano = tamano - 1;
            fuente = Recursos.fuente(tamano, true);
            g.setFont(fuente);
            medidas = g.getFontMetrics();
        }
        g.setColor(color);
        g.drawString(texto, centroX - medidas.stringWidth(texto) / 2,
                centroY + (medidas.getAscent() - medidas.getDescent()) / 2);
    }
}
