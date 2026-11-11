package windowView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

public class Superposicion extends JPanel {

    private static final double PROPORCION_DE_LA_VENTANA = 0.88;
    private Pantalla contenido;

    public Superposicion() {
        this.setOpaque(false);
        this.setLayout(null);
        this.setVisible(false);
        this.addMouseListener(new MouseAdapter() {
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
        });
        this.addMouseWheelListener(evento -> {
        });
    }

    public void mostrar(Pantalla pantalla) {
        this.ocultar();
        this.contenido = pantalla;
        this.add(this.contenido);
        this.contenido.alMostrar();
        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }

    public void ocultar() {
        if (this.contenido != null) {
            this.remove(this.contenido);
            this.contenido = null;
        }
        this.setVisible(false);
    }

    public boolean estaAbierta() {
        return this.contenido != null;
    }

    @Override
    public void doLayout() {
        if (this.contenido == null) {
            return;
        }
        double proporcion = (double) this.contenido.getAnchoLienzo() / this.contenido.getAltoLienzo();
        int ancho = (int) (this.getWidth() * PROPORCION_DE_LA_VENTANA);
        int alto = (int) (ancho / proporcion);
        if (alto > this.getHeight() * PROPORCION_DE_LA_VENTANA) {
            alto = (int) (this.getHeight() * PROPORCION_DE_LA_VENTANA);
            ancho = (int) (alto * proporcion);
        }
        int x = (this.getWidth() - ancho) / 2;
        int y = (this.getHeight() - alto) / 2;
        this.contenido.setBounds(x, y, ancho, alto);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}