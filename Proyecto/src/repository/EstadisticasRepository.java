package repository;

import java.util.ArrayList;

import model.EstadisticaJugador;
import model.Estadisticas;
import model.Jugador;
import persistance.JsonRepository;
import persistance.Rutas;

public class EstadisticasRepository {
    private JsonRepository<Estadisticas> repository;
    private Estadisticas misEstadisticas;

    public EstadisticasRepository() {
        this.repository = new JsonRepository<>(Rutas.data("estadisticas.json"), Estadisticas.class);
        this.misEstadisticas = repository.readFile();
        if (this.misEstadisticas == null) {
            this.misEstadisticas = new Estadisticas();
        }
    }

    // Actualiza las estadísticas de todos los jugadores de una partida terminada
    public void registrarPartida(ArrayList<Jugador> jugadores, String nombreGanador) {
        for (Jugador j : jugadores) {
            EstadisticaJugador e = misEstadisticas.buscarOCrear(j.getNombre());
            e.registrarPartida(j.getNombre().equalsIgnoreCase(nombreGanador), j.getCasillaMaxima());
        }
        repository.writeFile(misEstadisticas);
    }

    public ArrayList<EstadisticaJugador> getEstadisticas() {
        return misEstadisticas.getLista();
    }
}

