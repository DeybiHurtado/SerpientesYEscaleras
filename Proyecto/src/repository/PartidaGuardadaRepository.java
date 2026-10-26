package repository;

import model.PartidaGuardada;
import persistance.JsonRepository;
import persistance.Rutas;

/**
 * Guarda solo la última partida que el usuario decidió guardar.
 */
public class PartidaGuardadaRepository {
    private JsonRepository<PartidaGuardada> repository;

    public PartidaGuardadaRepository() {
        this.repository = new JsonRepository<>(Rutas.data("partidaGuardada.json"), PartidaGuardada.class);
    }

    public boolean hayPartida() {
        return repository.readFile() != null;
    }

    public PartidaGuardada cargar() {
        return repository.readFile();
    }

    public void guardar(PartidaGuardada partida) {
        repository.writeFile(partida);
    }

    public void borrar() {
        repository.deleteFile();
    }
}
