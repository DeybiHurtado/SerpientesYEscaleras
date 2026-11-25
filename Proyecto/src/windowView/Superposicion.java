package windowView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Superposicion extends JPanel {

    private static final double PROPORCION_DE_LA_VENTANA = 0.88;
    private Pantalla contenido;
    private final VentanaAviso ventanaAviso = new VentanaAviso();
    private Timer temporizadorAviso;

    public Superposicion() {
        this.setOpaque(false);
        this.setLayout(null);
        this.setVisible(false);
        this.add(this.ventanaAviso);
        this.setComponentZOrder(this.ventanaAviso, 0);
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
        this.setComponentZOrder(this.ventanaAviso, 0);
        this.contenido.alMostrar();
        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }

    public void ocultar() {
        if (this.temporizadorAviso != null) {
            this.temporizadorAviso.stop();
            this.temporizadorAviso = null;
        }
        this.ventanaAviso.ocultar();
        if (this.contenido != null) {
            this.remove(this.contenido);
            this.contenido = null;
        }
        this.setVisible(false);
    }

    public void mostrarError(String mensaje) {
        this.setVisible(true);
        this.revalidate();
        this.doLayout();
        this.setComponentZOrder(this.ventanaAviso, 0);
        this.ventanaAviso.setVisible(true);
        this.ventanaAviso.mostrarDosLineas("NOMBRE NO VÁLIDO", 32, new Color(0xFAFAE5), mensaje, 24,
                Recursos.DORADO);
        if (this.temporizadorAviso != null) {
            this.temporizadorAviso.stop();
        }
        this.temporizadorAviso = new Timer(4000, evento -> {
            this.ventanaAviso.ocultar();
            this.temporizadorAviso = null;
        });
        this.temporizadorAviso.setRepeats(false);
        this.temporizadorAviso.start();
        this.ventanaAviso.repaint();
        this.repaint();
    }

    public boolean estaAbierta() {
        return this.contenido != null;
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    @Override
    public void doLayout() {
        if (this.contenido != null) {
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
        double escalaAviso = Math.min(1.0, Math.min(this.getWidth() * 0.9 / VentanaAviso.ANCHO_DISENO,
                this.getHeight() * 0.9 / VentanaAviso.ALTO_DISENO));
        int anchoAviso = (int) (VentanaAviso.ANCHO_DISENO * escalaAviso);
        int altoAviso = (int) (VentanaAviso.ALTO_DISENO * escalaAviso);
        this.ventanaAviso.setBounds((this.getWidth() - anchoAviso) / 2, (this.getHeight() - altoAviso) / 2,
                anchoAviso, altoAviso);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}