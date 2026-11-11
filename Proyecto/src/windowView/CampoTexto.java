package windowView;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class CampoTexto extends JTextField {

    private final String textoDeAyuda;

    public CampoTexto(String textoDeAyuda) {
        this.textoDeAyuda = textoDeAyuda;
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(0, 6, 4, 6));
        this.setHorizontalAlignment(CENTER);
        this.setForeground(Recursos.CREMA);
        this.setCaretColor(Recursos.CREMA);
    }

    @Override
    public void setBounds(int x, int y, int ancho, int alto) {
        super.setBounds(x, y, ancho, alto);
        this.setFont(Recursos.fuente(alto * 0.42f, false));
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        Graphics2D g = (Graphics2D) graficos.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Recursos.GRIS_AZUL);
        g.fillRect(0, this.getHeight() - 3, this.getWidth(), 2);

        if (this.getText().isEmpty()) {
            g.setFont(this.getFont());
            FontMetrics medidas = g.getFontMetrics();
            g.setColor(new Color(0x8A9BAA));
            int x = (this.getWidth() - medidas.stringWidth(this.textoDeAyuda)) / 2;
            int y = (this.getHeight() - medidas.getHeight()) / 2 + medidas.getAscent() - 2;
            g.drawString(this.textoDeAyuda, x, y);
        }
        g.dispose();
    }
}