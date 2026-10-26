package persistance;

import java.io.File;

public class Rutas {

    public static String data(String nombreArchivo) {
        if (new File("src/data").isDirectory()) {
            return "src/data/" + nombreArchivo;
        }
        if (new File("Proyecto/src/data").isDirectory()) {
            return "Proyecto/src/data/" + nombreArchivo;
        }
        return "src/data/" + nombreArchivo;
    }
}
