package persistance;

import java.io.File;

public class Rutas {

    public static String data(String nombreArchivo) {
        if (new File("Proyecto/src/data").isDirectory()) {
            return "Proyecto/src/data/" + nombreArchivo;
        }
        if (new File("Proyecto/src/data").isDirectory()) {
            return "Proyecto/src/data/" + nombreArchivo;
        }
        return "Proyecto/src/data/" + nombreArchivo;
    }
}
