package windowView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public class BotonRedondo extends JComponent {

    public enum Tipo {
        INFO, CASA
    }

    private final Tipo tipo;
    private boolean mouseEncima;
    private ActionListener oyente;

    public BotonRedondo(Tipo tipo, String textoDeAyuda) {
        this.tipo = tipo;
        this.setToolTipText(textoDeAyuda);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evento) {
                BotonRedondo.this.mouseEncima = true;
                BotonRedondo.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent evento) {
                BotonRedondo.this.mouseEncima = false;
                BotonRedondo.this.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent evento) {
                if (BotonRedondo.this.oyente != null) {
                    BotonRedondo.this.oyente.actionPerformed(
                            new ActionEvent(BotonRedondo.this, ActionEvent.ACTION_PERFORMED, "clic"));
                }
            }
        });
    }

    public void alHacerClic(ActionListener oyente) {
        this.oyente = oyente;
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        Graphics2D g = (Graphics2D) graficos.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int diametro = Math.min(this.getWidth(), this.getHeight());
        float grosor = Math.max(2f, diametro * 0.07f);
        int margen = (int) grosor;
        Color colorDelBorde = new Color(0xE0A82E);
        if (this.mouseEncima) {
            colorDelBorde = Recursos.DORADO;
        }

        g.setColor(new Color(0, 10, 20, 200));
        g.fillOval(margen, margen, diametro - 2 * margen, diametro - 2 * margen);
        g.setColor(colorDelBorde);
        g.setStroke(new BasicStroke(grosor));
        g.drawOval(margen, margen, diametro - 2 * margen, diametro - 2 * margen);

        if (this.tipo == Tipo.INFO) {
            this.dibujarLetraI(g, diametro);
        } else {
            this.dibujarCasita(g, diametro);
        }
        g.dispose();
    }

    private void dibujarLetraI(Graphics2D g, int diametro) {
        Font fuente = Recursos.fuente(diametro * 0.55f, true);
        FontMetrics medidas = g.getFontMetrics(fuente);
        g.setFont(fuente);
        int x = (diametro - medidas.stringWidth("i")) / 2;
        int y = diametro / 2 + (medidas.getAscent() - medidas.getDescent()) / 2;
        g.drawString("i", x, y);
    }

    private void dibujarCasita(Graphics2D g, int d) {
        int[] techoX = { d / 2, (int) (d * 0.80), (int) (d * 0.20) };
        int[] techoY = { (int) (d * 0.22), (int) (d * 0.50), (int) (d * 0.50) };
        g.fillPolygon(new Polygon(techoX, techoY, 3));
        g.fillRect((int) (d * 0.30), (int) (d * 0.50), (int) (d * 0.40), (int) (d * 0.28));
        g.setColor(new Color(0, 10, 20));
        g.fillRect((int) (d * 0.45), (int) (d * 0.60), (int) (d * 0.10), (int) (d * 0.18));
    }
}