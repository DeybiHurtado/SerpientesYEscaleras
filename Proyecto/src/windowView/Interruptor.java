package windowView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.Timer;

public class Interruptor extends JComponent {

    private boolean encendido;
    private double posicionPerilla;
    private Timer temporizadorAnimacion;
    private Consumer<Boolean> alCambiar;

    public Interruptor(boolean encendido) {
        this.encendido = encendido;
        if (encendido) {
            this.posicionPerilla = 1;
        } else {
            this.posicionPerilla = 0;
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                Interruptor.this.cambiar(!Interruptor.this.encendido, true);
            }
        });
    }

    public void setAlCambiar(Consumer<Boolean> alCambiar) {
        this.alCambiar = alCambiar;
    }

    public boolean isEncendido() {
        return this.encendido;
    }

    public void cambiar(boolean nuevoValor, boolean avisar) {
        this.encendido = nuevoValor;
        if (this.temporizadorAnimacion != null) {
            this.temporizadorAnimacion.stop();
        }
        this.temporizadorAnimacion = new Timer(15, evento -> this.moverPerilla());
        this.temporizadorAnimacion.start();
        if (avisar && this.alCambiar != null) {
            this.alCambiar.accept(nuevoValor);
        }
    }

    private void moverPerilla() {
        double destino = 0;
        if (this.encendido) {
            destino = 1;
        }
        this.posicionPerilla = this.posicionPerilla + (destino - this.posicionPerilla) * 0.35;
        if (Math.abs(destino - this.posicionPerilla) < 0.02) {
            this.posicionPerilla = destino;
            this.temporizadorAnimacion.stop();
        }
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        Graphics2D g = (Graphics2D) graficos.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int ancho = this.getWidth();
        int alto = this.getHeight();

        Color apagado = new Color(0x1B2A38);
        Color prendido = new Color(0x8A6A10);
        int rojo = (int) (apagado.getRed() + (prendido.getRed() - apagado.getRed()) * this.posicionPerilla);
        int verde = (int) (apagado.getGreen() + (prendido.getGreen() - apagado.getGreen()) * this.posicionPerilla);
        int azul = (int) (apagado.getBlue() + (prendido.getBlue() - apagado.getBlue()) * this.posicionPerilla);
        g.setColor(new Color(rojo, verde, azul));
        g.fillRoundRect(0, 0, ancho, alto, alto, alto);

        Color colorBorde = Recursos.GRIS_AZUL;
        Color colorPerilla = Recursos.CREMA;
        if (this.encendido) {
            colorBorde = Recursos.DORADO;
            colorPerilla = Recursos.DORADO;
        }
        g.setColor(colorBorde);
        g.setStroke(new BasicStroke(Math.max(2f, alto * 0.06f)));
        g.drawRoundRect(2, 2, ancho - 4, alto - 4, alto - 4, alto - 4);

        int diametro = alto - 14;
        int x = (int) (7 + (ancho - diametro - 14) * this.posicionPerilla);
        g.setColor(colorPerilla);
        g.fillOval(x, 7, diametro, diametro);
        g.dispose();
    }
}