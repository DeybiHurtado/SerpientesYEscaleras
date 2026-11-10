package windowView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class VentanaAviso extends JComponent {

    public static final int ANCHO_DISENO = 680;
    public static final int ALTO_DISENO = 400;

    private volatile boolean visibleAhora;
    private volatile String[] lineas = new String[0];
    private volatile Color[] colores = new Color[0];
    private volatile float[] tamanos = new float[0];

   public void mostrar(String[] lineas, Color[] colores, float[] tamanos) {
        this.lineas = lineas;
        this.colores = colores;
        this.tamanos = tamanos;
        this.visibleAhora = true;
        this.repaint();
    }

    public void mostrarDosLineas(String titulo, float tamanoTitulo, Color colorTitulo, String texto,
            float tamanoTexto, Color colorTexto) {
        this.mostrar(new String[] { titulo, texto }, new Color[] { colorTitulo, colorTexto },
                new float[] { tamanoTitulo, tamanoTexto });
    }

    public void ocultar() {
        this.visibleAhora = false;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        if (!this.visibleAhora) {
            return;
        }
        String[] textos = this.lineas;
        Color[] colorLinea = this.colores;
        float[] tamanoLinea = this.tamanos;

        Graphics2D g = (Graphics2D) graficos.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        double escala = (double) this.getWidth() / ANCHO_DISENO;
        g.scale(escala, escala);

        int altoCuadro = 44;
        for (int i = 0; i < textos.length; i++) {
            altoCuadro = altoCuadro + (int) (tamanoLinea[i] * 1.45);
        }
        int anchoCuadro = 640;
        int x = (ANCHO_DISENO - anchoCuadro) / 2;
        int y = (ALTO_DISENO - altoCuadro) / 2;

        g.setColor(new Color(0, 10, 20, 238));
        g.fillRoundRect(x, y, anchoCuadro, altoCuadro, 28, 28);
        Color colorBorde = Recursos.DORADO;
        if (textos.length > 0) {
            colorBorde = colorLinea[textos.length - 1];
        }
        g.setColor(colorBorde);
        g.setStroke(new BasicStroke(5));
        g.drawRoundRect(x, y, anchoCuadro, altoCuadro, 28, 28);

        int yLinea = y + 22;
        for (int i = 0; i < textos.length; i++) {
            int centroY = yLinea + (int) (tamanoLinea[i] * 0.72);
            Recursos.textoCentrado(g, textos[i], ANCHO_DISENO / 2, centroY, tamanoLinea[i], colorLinea[i],
                    anchoCuadro - 60);
            yLinea = yLinea + (int) (tamanoLinea[i] * 1.45);
        }
        g.dispose();
    }
}
