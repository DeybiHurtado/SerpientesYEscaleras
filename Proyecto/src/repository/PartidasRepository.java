package repository;

import java.util.ArrayList;

import model.Partida;
import model.Partidas;
import persistance.JsonRepository;
import persistance.Rutas;

public class PartidasRepository {
    private JsonRepository<Partidas> repository;
    private Partidas misPartidas;

    public PartidasRepository() {
        this.repository = new JsonRepository<>(Rutas.data("historialPartidas.json"), Partidas.class);
        this.misPartidas = repository.readFile();
        if (this.misPartidas == null) {
            this.misPartidas = new Partidas();
        }
    }

    public void guardarPartida(Partida partida) {
        this.misPartidas.agregar(partida);
        repository.writeFile(this.misPartidas);
    }

    public ArrayList<Partida> getPartidas() {
        return this.misPartidas.getLista();
    }
}
