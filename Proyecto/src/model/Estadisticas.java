package model;

import java.util.ArrayList;

public class Estadisticas {
    private ArrayList<EstadisticaJugador> lista;

    public Estadisticas() {
        this.lista = new ArrayList<>();
    }

    public ArrayList<EstadisticaJugador> getLista() {
        return lista;
    }

    // Busca por nombre (sin importar mayúsculas); si no existe, la crea
    public EstadisticaJugador buscarOCrear(String nombre) {
        for (EstadisticaJugador e : lista) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                return e;
            }
        }
        EstadisticaJugador nueva = new EstadisticaJugador(nombre);
        lista.add(nueva);
        return nueva;
    }
}
