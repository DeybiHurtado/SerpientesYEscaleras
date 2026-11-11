package windowView;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;


 
public abstract class Pantalla extends JPanel {

   
    private static class ComponenteColocado {
        private final JComponent componente;
        private final int x;
        private final int y;
        private final int ancho;
        private final int alto;

        ComponenteColocado(JComponent componente, int x, int y, int ancho, int alto) {
            this.componente = componente;
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            this.alto = alto;
        }
    }

    private final int anchoLienzo;
    private final int altoLienzo;
    private BufferedImage imagenFondo;
    private final List<ComponenteColocado> componentesColocados = new ArrayList<>();

    protected Pantalla(int anchoLienzo, int altoLienzo, String rutaFondo) {
        this.anchoLienzo = anchoLienzo;
        this.altoLienzo = altoLienzo;
        this.imagenFondo = Recursos.imagen(rutaFondo);
        this.setLayout(null); 
        this.setOpaque(true);
        this.setBackground(Recursos.FONDO_OSCURO);
    }

    protected void setFondo(String rutaFondo) {
        this.imagenFondo = Recursos.imagen(rutaFondo);
        this.repaint();
    }

   
    protected <T extends JComponent> T agregar(T componente, int x, int y, int ancho, int alto) {
        this.componentesColocados.add(new ComponenteColocado(componente, x, y, ancho, alto));
        this.add(componente);
        return componente;
    }

    
    protected void dibujar(Graphics2D g) {
    }

    
    public void alMostrar() {
    }

    public int getAnchoLienzo() {
        return this.anchoLienzo;
    }

    public int getAltoLienzo() {
        return this.altoLienzo;
    }

    
    public double getEscala() {
        double escalaAncho = (double) this.getWidth() / this.anchoLienzo;
        double escalaAlto = (double) this.getHeight() / this.altoLienzo;
        return Math.min(escalaAncho, escalaAlto);
    }

    private int margenIzquierdo() {
        return (int) Math.round((this.getWidth() - this.anchoLienzo * this.getEscala()) / 2);
    }

    private int margenSuperior() {
        return (int) Math.round((this.getHeight() - this.altoLienzo * this.getEscala()) / 2);
    }

    
    @Override
    public void doLayout() {
        double escala = this.getEscala();
        int margenX = this.margenIzquierdo();
        int margenY = this.margenSuperior();
        for (ComponenteColocado item : this.componentesColocados) {
            int x = margenX + (int) Math.round(item.x * escala);
            int y = margenY + (int) Math.round(item.y * escala);
            int ancho = (int) Math.round(item.ancho * escala);
            int alto = (int) Math.round(item.alto * escala);
            item.componente.setBounds(x, y, ancho, alto);
        }
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        Graphics2D g = (Graphics2D) graficos.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        double escala = this.getEscala();
        g.translate(this.margenIzquierdo(), this.margenSuperior()); 
        g.scale(escala, escala); 

        g.drawImage(this.imagenFondo, 0, 0, this.anchoLienzo, this.altoLienzo, null);
        this.dibujar(g);
        g.dispose();
    }

   

    protected void textoCentrado(Graphics2D g, String texto, int centroX, int centroY, float tamano, Color color,
            int anchoMaximo) {
        Recursos.textoCentrado(g, texto, centroX, centroY, tamano, color, anchoMaximo);
    }

    protected void textoIzquierda(Graphics2D g, String texto, int x, int centroY, float tamano, Color color) {
        Recursos.textoIzquierda(g, texto, x, centroY, tamano, color);
    }

   
    protected void textoEnCaja(Graphics2D g, String texto, int x, int y, int ancho, int alto, float tamano,
            Color color) {
        this.escribirEnCaja(g, texto, x, y, ancho, alto, tamano, color, true);
    }

  
    protected void textoEnCajaIzquierda(Graphics2D g, String texto, int x, int y, int ancho, int alto,
            float tamano, Color color) {
        this.escribirEnCaja(g, texto, x, y, ancho, alto, tamano, color, false);
    }

    private void escribirEnCaja(Graphics2D g, String texto, int x, int y, int ancho, int alto, float tamano,
            Color color, boolean centrado) {
        Font fuente = Recursos.fuente(tamano, false);
        g.setFont(fuente);
        FontMetrics medidas = g.getFontMetrics();

       
        List<String> lineas = new ArrayList<>();
        String lineaActual = "";
        for (String palabra : texto.split(" ")) {
            String prueba = lineaActual.isEmpty() ? palabra : lineaActual + " " + palabra;
            if (medidas.stringWidth(prueba) > ancho && !lineaActual.isEmpty()) {
                lineas.add(lineaActual);
                lineaActual = palabra;
            } else {
                lineaActual = prueba;
            }
        }
        lineas.add(lineaActual);

       
        int altoLinea = medidas.getHeight();
        int yLinea = y + (alto - altoLinea * lineas.size()) / 2 + medidas.getAscent();
        g.setColor(color);
        for (String linea : lineas) {
            int xLinea = x;
            if (centrado) {
                xLinea = x + (ancho - medidas.stringWidth(linea)) / 2;
            }
            g.drawString(linea, xLinea, yLinea);
            yLinea = yLinea + altoLinea;
        }
    }
}
