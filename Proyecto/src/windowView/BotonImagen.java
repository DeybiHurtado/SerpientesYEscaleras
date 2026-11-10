package windowView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;


public class BotonImagen extends JComponent {

    private final BufferedImage imagenNormal;
    private final BufferedImage imagenResaltada;
    private final int margenEsquina; 
    private String texto;
    private String simbolo;
    private boolean mouseEncima;
    private boolean seleccionado;
    private final List<ActionListener> oyentes = new ArrayList<>();

    public BotonImagen(String texto, String rutaNormal, String rutaResaltada, int margenEsquina) {
        this.texto = texto;
        this.imagenNormal = Recursos.imagen(rutaNormal);
        this.imagenResaltada = Recursos.imagen(rutaResaltada);
        this.margenEsquina = margenEsquina;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.activarMouse();
    }

    private void activarMouse() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evento) {
                BotonImagen.this.mouseEncima = true;
                BotonImagen.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent evento) {
                BotonImagen.this.mouseEncima = false;
                BotonImagen.this.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent evento) {
                BotonImagen.this.avisarClic();
            }
        });
    }

    private void avisarClic() {
        List<ActionListener> copia = new ArrayList<>(this.oyentes);
        for (ActionListener oyente : copia) {
            oyente.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this.texto));
        }
    }

    public void alHacerClic(ActionListener oyente) {
        this.oyentes.add(oyente);
    }

    public void setTexto(String texto) {
        this.texto = texto;
        this.repaint();
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        this.repaint();
    }

    public boolean isSeleccionado() {
        return this.seleccionado;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        Graphics2D g = (Graphics2D) graficos.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int ancho = this.getWidth();
        int alto = this.getHeight();
        boolean resaltado = this.mouseEncima || this.seleccionado;

        double escala = 1;
        if (this.getParent() instanceof Pantalla) {
            escala = ((Pantalla) this.getParent()).getEscala();
        }
        BufferedImage imagen = this.imagenNormal;
        if (resaltado) {
            imagen = this.imagenResaltada;
        }
        Recursos.dibujarEstirada(g, imagen, 0, 0, ancho, alto, this.margenEsquina,
                (int) Math.round(this.margenEsquina * escala));

        Color colorTexto = Recursos.CREMA;
        if (resaltado) {
            colorTexto = Recursos.DORADO;
        }
        int centroDelTexto = alto / 2;
        if (this.simbolo != null) {
            this.dibujarSimbolo(g, ancho, alto, colorTexto);
            centroDelTexto = (int) (alto * 0.76);
        }
        this.dibujarTexto(g, ancho, alto, centroDelTexto, colorTexto);
        g.dispose();
    }

    private void dibujarSimbolo(Graphics2D g, int ancho, int alto, Color color) {
        int radio = (int) (alto * 0.17);
        int centroX = ancho / 2;
        int centroY = (int) (alto * 0.38);
        g.setColor(color);
        g.setStroke(new BasicStroke(Math.max(2f, alto * 0.02f)));
        g.drawOval(centroX - radio, centroY - radio, 2 * radio, 2 * radio);
        g.drawLine(centroX - radio / 2, centroY, centroX + radio / 2, centroY);
        g.drawLine(centroX, centroY - radio / 2, centroX, centroY + radio / 2);
    }

    private void dibujarTexto(Graphics2D g, int ancho, int alto, int centroY, Color color) {
        if (this.texto == null || this.texto.isEmpty()) {
            return;
        }
        float tamano = alto * 0.36f;
        if (this.simbolo != null) {
            tamano = alto * 0.12f;
        }
        Font fuente = Recursos.fuente(tamano, true);
        FontMetrics medidas = g.getFontMetrics(fuente);
        // Si el texto no cabe, se achica hasta que quepa
        while (medidas.stringWidth(this.texto) > ancho * 0.88 && tamano > 8) {
            tamano = tamano - 1;
            fuente = Recursos.fuente(tamano, true);
            medidas = g.getFontMetrics(fuente);
        }
        g.setFont(fuente);
        g.setColor(color);
        int x = (ancho - medidas.stringWidth(this.texto)) / 2;
        int y = centroY + (medidas.getAscent() - medidas.getDescent()) / 2;
        g.drawString(this.texto, x, y);
    }
}
