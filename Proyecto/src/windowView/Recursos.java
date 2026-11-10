package windowView;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;


public final class Recursos {

    public static final Color CREMA = new Color(0xEDE8C8);
    public static final Color DORADO = new Color(0xF5C518);
    public static final Color FONDO_OSCURO = new Color(0x000E18);
    public static final Color GRIS_AZUL = new Color(0x5B7286);


    private static final Map<String, BufferedImage> imagenesGuardadas = new HashMap<>();
    private static String familiaDeFuente;

    private Recursos() {
    }

    public static BufferedImage imagen(String ruta) {
        BufferedImage imagen = imagenesGuardadas.get(ruta);
        if (imagen != null) {
            return imagen;
        }
        try {
            URL url = Recursos.class.getResource("/resources/" + ruta);
            if (url != null) {
                imagen = ImageIO.read(url);
            } else {
                String[] carpetas = { "src/resources/", "Proyecto/src/resources/" };
                for (String carpeta : carpetas) {
                    File archivo = new File(carpeta + ruta);
                    if (archivo.exists()) {
                        imagen = ImageIO.read(archivo);
                        break;
                    }
                }
            }
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo leer la imagen: " + ruta, error);
        }
        if (imagen == null) {
            throw new IllegalStateException("No se encontró la imagen: " + ruta);
        }
        imagenesGuardadas.put(ruta, imagen);
        return imagen;
    }

    public static Font fuente(float tamano, boolean negrita) {
        if (familiaDeFuente == null) {
            Set<String> instaladas = new HashSet<>(Arrays.asList(
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
            familiaDeFuente = "Serif";
            String[] preferidas = { "Segoe Print", "Segoe Script", "Comic Sans MS", "Chalkboard SE" };
            for (String nombre : preferidas) {
                if (instaladas.contains(nombre)) {
                    familiaDeFuente = nombre;
                    break;
                }
            }
        }
        int estilo = negrita ? Font.BOLD : Font.PLAIN;
        return new Font(familiaDeFuente, estilo, 1).deriveFont(tamano);
    }

    public static void dibujarEstirada(Graphics2D g, BufferedImage imagen, int x, int y, int ancho, int alto,
            int margen, int margenEnPantalla) {
        if (margen <= 0) {
            g.drawImage(imagen, x, y, ancho, alto, null);
            return;
        }
        int anchoImagen = imagen.getWidth();
        int altoImagen = imagen.getHeight();
        int borde = Math.min(margenEnPantalla, Math.min(ancho, alto) / 2);
        int[] origenX = { 0, margen, anchoImagen - margen, anchoImagen };
        int[] origenY = { 0, margen, altoImagen - margen, altoImagen };
        int[] destinoX = { x, x + borde, x + ancho - borde, x + ancho };
        int[] destinoY = { y, y + borde, y + alto - borde, y + alto };
        for (int columna = 0; columna < 3; columna++) {
            for (int fila = 0; fila < 3; fila++) {
                g.drawImage(imagen, destinoX[columna], destinoY[fila], destinoX[columna + 1], destinoY[fila + 1],
                        origenX[columna], origenY[fila], origenX[columna + 1], origenY[fila + 1], null);
            }
        }
    }

    public static void textoCentrado(Graphics2D g, String texto, int centroX, int centroY, float tamano,
            Color color, int anchoMaximo) {
        Font fuente = fuente(tamano, true);
        FontMetrics medidas = g.getFontMetrics(fuente);
        while (medidas.stringWidth(texto) > anchoMaximo && tamano > 8) {
            tamano = tamano - 2;
            fuente = fuente(tamano, true);
            medidas = g.getFontMetrics(fuente);
        }
        int x = centroX - medidas.stringWidth(texto) / 2;
        int y = centroY + (medidas.getAscent() - medidas.getDescent()) / 2;
        escribirConSombra(g, texto, x, y, fuente, color, tamano);
    }

    public static void textoIzquierda(Graphics2D g, String texto, int x, int centroY, float tamano, Color color) {
        Font fuente = fuente(tamano, true);
        FontMetrics medidas = g.getFontMetrics(fuente);
        int y = centroY + (medidas.getAscent() - medidas.getDescent()) / 2;
        escribirConSombra(g, texto, x, y, fuente, color, tamano);
    }

    private static void escribirConSombra(Graphics2D g, String texto, int x, int y, Font fuente, Color color,
            float tamano) {
        int sombra = Math.max(2, Math.round(tamano * 0.04f));
        g.setFont(fuente);
        g.setColor(new Color(0, 0, 0, 170));
        g.drawString(texto, x + sombra, y + sombra);
        g.setColor(color);
        g.drawString(texto, x, y);
    }
}
