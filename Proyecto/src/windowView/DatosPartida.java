package windowView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DatosPartida {

    public static final File ARCHIVO = new File("partida_guardada.properties");

    private final String[] nombres;
    private final boolean[] sonComputadora;
    private final int[] posiciones;
    private int turno;

    public DatosPartida(String[] nombres, boolean[] sonComputadora) {
        this.nombres = nombres;
        this.sonComputadora = sonComputadora;
        this.posiciones = new int[nombres.length];
        this.turno = 0;
    }

    public int getCantidad() {
        return this.nombres.length;
    }

    public String getNombre(int indice) {
        return this.nombres[indice];
    }

    public boolean esComputadora(int indice) {
        return this.sonComputadora[indice];
    }

    public int getPosicion(int indice) {
        return this.posiciones[indice];
    }

    public void setPosicion(int indice, int posicion) {
        this.posiciones[indice] = posicion;
    }

    public int getTurno() {
        return this.turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public void reiniciar() {
        for (int i = 0; i < this.posiciones.length; i++) {
            this.posiciones[i] = 0;
        }
        this.turno = 0;
    }

    public void guardar() throws IOException {
        Properties propiedades = new Properties();
        propiedades.setProperty("jugadores", String.valueOf(this.nombres.length));
        propiedades.setProperty("turno", String.valueOf(this.turno));
        for (int i = 0; i < this.nombres.length; i++) {
            propiedades.setProperty("nombre." + i, this.nombres[i]);
            propiedades.setProperty("computadora." + i, String.valueOf(this.sonComputadora[i]));
            propiedades.setProperty("posicion." + i, String.valueOf(this.posiciones[i]));
        }
        try (Writer escritor = new OutputStreamWriter(new FileOutputStream(ARCHIVO), StandardCharsets.UTF_8)) {
            propiedades.store(escritor, "Partida guardada de Serpientes y Escaleras");
        }
    }

    public static DatosPartida cargar() throws IOException {
        Properties propiedades = new Properties();
        try (Reader lector = new InputStreamReader(new FileInputStream(ARCHIVO), StandardCharsets.UTF_8)) {
            propiedades.load(lector);
        }
        int cantidad = Integer.parseInt(propiedades.getProperty("jugadores"));
        String[] nombres = new String[cantidad];
        boolean[] computadora = new boolean[cantidad];
        for (int i = 0; i < cantidad; i++) {
            nombres[i] = propiedades.getProperty("nombre." + i);
            computadora[i] = Boolean.parseBoolean(propiedades.getProperty("computadora." + i));
        }
        DatosPartida datos = new DatosPartida(nombres, computadora);
        for (int i = 0; i < cantidad; i++) {
            datos.setPosicion(i, Integer.parseInt(propiedades.getProperty("posicion." + i)));
        }
        datos.setTurno(Integer.parseInt(propiedades.getProperty("turno")));
        return datos;
    }
}