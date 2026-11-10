
package repository;

import java.util.ArrayList;
import model.EstadisticaJugador;
import model.Estadisticas;
import model.Jugador;
import persistance.JsonRepository;
import persistance.Rutas;

public class EstadisticasRepository {
    private JsonRepository<Estadisticas> repository;

    public EstadisticasRepository() {
        this.repository = new JsonRepository<>(Rutas.data("estadisticas.json"), Estadisticas.class);
    }

    public void registrarPartida(ArrayList<Jugador> jugadores, String nombreGanador) {
      
        Estadisticas misEstadisticas = repository.readFile();
        if (misEstadisticas == null) {
            misEstadisticas = new Estadisticas();
        }

        for (Jugador j : jugadores) {
            EstadisticaJugador e = misEstadisticas.buscarOCrear(j.getNombre());
            e.registrarPartida(j.getNombre().equalsIgnoreCase(nombreGanador), j.getPosicion());
        }

        repository.writeFile(misEstadisticas);
    }

    public ArrayList<EstadisticaJugador> getEstadisticas() {
        Estadisticas misEstadisticas = repository.readFile();
        if (misEstadisticas == null || misEstadisticas.getLista() == null) {
            return new ArrayList<>();
        }
        return misEstadisticas.getLista();
    }
}