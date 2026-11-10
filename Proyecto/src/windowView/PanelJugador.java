package windowView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class PanelJugador extends JPanel {

    public static final String[] COLORES = { "rojo", "azul", "verde", "amarillo" };
    public static final Color[] COLORES_DE_TEXTO = { new Color(0xE85A5A), new Color(0x5C8CF0),
            new Color(0x55CC78), new Color(0xF0CC3C) };

    private static final int[][][] RECTANGULOS_DADOS = {
            { { 7, 79, 55, 57 }, { 71, 79, 54, 57 } },
            { { 147, 6, 55, 57 }, { 211, 6, 54, 57 } },
            { { 12, 6, 55, 57 }, { 76, 6, 54, 57 } },
            { { 138, 76, 56, 55 }, { 204, 77, 56, 54 } } };
    private static final int[][] RECTANGULO_POSICION = { { 205, 74, 69, 69 }, { 2, 0, 69, 69 },
            { 207, 3, 69, 69 }, { 4, 72, 68, 63 } };
    private static final int[][] HUECO_DE_AYUDA = { { 125, 80 }, { 71, 76 }, { 130, 77 }, { 72, 66 } };
    private static final boolean[] BARRA_ABAJO = { false, true, true, false };
    private static final int ALTO_BARRA = 66;

    private final int indiceColor;
    private final BufferedImage imagenPanel;
    private final Color colorDelJugador;

    private volatile String nombre;
    private volatile int posicion;
    private volatile int dado1 = 1;
    private volatile int dado2 = 1;
    private volatile boolean lanzando;
    private volatile double giroDeLosDados;
    private volatile boolean esTurno;
    private volatile boolean esperandoLanzamiento;
    private volatile double xFicha;
    private volatile double yFicha;
    private Runnable alPulsarDados;

    public PanelJugador(int indiceColor, String nombre) {
        this.indiceColor = indiceColor;
        this.nombre = nombre;
        this.imagenPanel = Recursos.imagen("jugadores/panel_" + COLORES[indiceColor] + ".png");
        this.colorDelJugador = COLORES_DE_TEXTO[indiceColor];
        this.setOpaque(false);
        this.activarMouse();
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.repaint();
    }

    public int getPosicion() {
        return this.posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
        this.repaint();
    }

    public int getDado1() {
        return this.dado1;
    }

    public int getDado2() {
        return this.dado2;
    }

    public void setDados(int dado1, int dado2) {
        this.dado1 = dado1;
        this.dado2 = dado2;
        this.repaint();
    }

    public void setLanzando(boolean lanzando, double giro) {
        this.lanzando = lanzando;
        this.giroDeLosDados = giro;
        this.repaint();
    }

    public void setEsTurno(boolean esTurno) {
        this.esTurno = esTurno;
        this.repaint();
    }

    public boolean isEsperandoLanzamiento() {
        return this.esperandoLanzamiento;
    }

    public void setEsperandoLanzamiento(boolean esperandoLanzamiento) {
        this.esperandoLanzamiento = esperandoLanzamiento;
        this.repaint();
    }

    public double getXFicha() {
        return this.xFicha;
    }

    public double getYFicha() {
        return this.yFicha;
    }

    public void setFicha(double x, double y) {
        this.xFicha = x;
        this.yFicha = y;
    }

    public Color getColorDelJugador() {
        return this.colorDelJugador;
    }

    public int getIndiceColor() {
        return this.indiceColor;
    }

    public int getAnchoImagen() {
        return this.imagenPanel.getWidth();
    }

    public int getAltoImagen() {
        return this.imagenPanel.getHeight();
    }

    public void setAlPulsarDados(Runnable alPulsarDados) {
        this.alPulsarDados = alPulsarDados;
    }

    private void activarMouse() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                if (PanelJugador.this.estaSobreLosDados(evento) && PanelJugador.this.alPulsarDados != null) {
                    PanelJugador.this.alPulsarDados.run();
                }
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent evento) {
                if (PanelJugador.this.estaSobreLosDados(evento)) {
                    PanelJugador.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    PanelJugador.this.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }

    private boolean estaSobreLosDados(MouseEvent evento) {
        double escala = (double) this.getWidth() / this.imagenPanel.getWidth();
        double x = evento.getX() / escala;
        double y = evento.getY() / escala;
        int[] primero = RECTANGULOS_DADOS[this.indiceColor][0];
        int[] segundo = RECTANGULOS_DADOS[this.indiceColor][1];
        boolean dentroEnX = x >= primero[0] && x <= segundo[0] + segundo[2];
        boolean dentroEnY = y >= primero[1] && y <= primero[1] + primero[3];
        return dentroEnX && dentroEnY;
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        Graphics2D g = (Graphics2D) graficos.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        double escala = (double) this.getWidth() / this.imagenPanel.getWidth();
        g.scale(escala, escala);
        g.drawImage(this.imagenPanel, 0, 0, null);

        this.dibujarResplandorDeTurno(g);
        this.dibujarNombre(g);
        this.dibujarDados(g);
        this.dibujarPosicion(g);
        this.dibujarAyuda(g);
        g.dispose();
    }

    private void dibujarResplandorDeTurno(Graphics2D g) {
        if (!this.esTurno) {
            return;
        }
        g.setColor(new Color(245, 197, 24, 220));
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(2, 2, this.imagenPanel.getWidth() - 5, this.imagenPanel.getHeight() - 5, 18, 18);
    }

    private void dibujarNombre(Graphics2D g) {
        int yBarra = 0;
        if (BARRA_ABAJO[this.indiceColor]) {
            yBarra = this.imagenPanel.getHeight() - ALTO_BARRA;
        }
        Recursos.textoCentrado(g, this.nombre, this.imagenPanel.getWidth() / 2, yBarra + ALTO_BARRA / 2, 32,
                Recursos.CREMA, this.imagenPanel.getWidth() - 40);
    }

    private void dibujarDados(Graphics2D g) {
        int[] primero = RECTANGULOS_DADOS[this.indiceColor][0];
        int[] segundo = RECTANGULOS_DADOS[this.indiceColor][1];
        double giro = 0;
        if (this.lanzando) {
            giro = this.giroDeLosDados;
        }
        this.dibujarUnDado(g, primero, this.dado1, giro);
        this.dibujarUnDado(g, segundo, this.dado2, -giro);
    }

    private void dibujarUnDado(Graphics2D g, int[] rectangulo, int valor, double giro) {
        int x = rectangulo[0];
        int y = rectangulo[1];
        int ancho = rectangulo[2];
        int alto = rectangulo[3];
        Graphics2D d = (Graphics2D) g.create();
        d.rotate(giro, x + ancho / 2.0, y + alto / 2.0);

        d.setColor(new Color(180, 168, 144));
        d.fillRoundRect(x, y, ancho, alto, 10, 10);
        d.setColor(new Color(120, 108, 86));
        d.setStroke(new BasicStroke(2.5f));
        d.drawRoundRect(x + 1, y + 1, ancho - 2, alto - 2, 10, 10);

        int[][] puntosPorValor = { {}, { 4 }, { 0, 8 }, { 0, 4, 8 }, { 0, 2, 6, 8 }, { 0, 2, 4, 6, 8 },
                { 0, 2, 3, 5, 6, 8 } };
        double[] fraccion = { 0.26, 0.5, 0.74 };
        d.setColor(Color.BLACK);
        for (int punto : puntosPorValor[valor]) {
            int centroX = (int) (x + ancho * fraccion[punto % 3]);
            int centroY = (int) (y + alto * fraccion[punto / 3]);
            d.fillOval(centroX - 5, centroY - 5, 10, 10);
        }
        d.dispose();
    }
    private void dibujarPosicion(Graphics2D g) {
        int[] caja = RECTANGULO_POSICION[this.indiceColor];
        Recursos.textoCentrado(g, String.valueOf(this.posicion), caja[0] + caja[2] / 2, caja[1] + caja[3] / 2, 36,
                this.colorDelJugador, caja[2] - 18);
    }

    private void dibujarAyuda(Graphics2D g) {
        if (!(this.esTurno && this.esperandoLanzamiento)) {
            return;
        }
        int[] hueco = HUECO_DE_AYUDA[this.indiceColor];
        int[] dado = RECTANGULOS_DADOS[this.indiceColor][0];
        Recursos.textoCentrado(g, "¡LANZA!", hueco[0] + hueco[1] / 2, dado[1] + dado[3] / 2, 20, Recursos.DORADO,
                hueco[1]);
    }
}

