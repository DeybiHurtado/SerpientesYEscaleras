package windowView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public class CasillaBoton extends JComponent {

    private final int numero;
    private boolean mouseEncima;
    private boolean iluminada;

    public CasillaBoton(int numero) {
        this.numero = numero;
        this.setToolTipText("Casilla " + numero);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evento) {
                CasillaBoton.this.mouseEncima = true;
                CasillaBoton.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent evento) {
                CasillaBoton.this.mouseEncima = false;
                CasillaBoton.this.repaint();
            }
        });
    }

    public int getNumero() {
        return this.numero;
    }

    public void setIluminada(boolean iluminada) {
        this.iluminada = iluminada;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        Graphics2D g = (Graphics2D) graficos.create();
        if (this.iluminada) {
            g.setColor(new Color(245, 197, 24, 110));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        if (this.mouseEncima) {
            g.setColor(new Color(245, 197, 24, 200));
            g.setStroke(new BasicStroke(2f));
            g.drawRect(2, 2, this.getWidth() - 5, this.getHeight() - 5);
        }
        g.dispose();
    }
}