package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Escalera;
import model.Escaleras;
import persistance.JsonRepository;
import persistance.Rutas;

public class EscalerasRepository {

    private JsonRepository<Escaleras> repository;
    private Escaleras misEscaleras;

    public EscalerasRepository() {
        this.repository = new JsonRepository<>(Rutas.data("Escaleras.json"), Escaleras.class);
        if (new File(Rutas.data("Escaleras.json")).exists()) {
            this.misEscaleras = repository.readFile();
        }
        if (this.misEscaleras == null) {
            this.misEscaleras = new Escaleras();
        }
    }

    public boolean hayDatos() {
        return !misEscaleras.getLista().isEmpty();
    }

    public ArrayList<Escalera> getEscaleras() {
        return misEscaleras.getLista();
    }

    public void guardarEscaleras(List<Escalera> escaleras) {
        this.misEscaleras = new Escaleras(new ArrayList<>(escaleras));
        repository.writeFile(this.misEscaleras);
    }
}