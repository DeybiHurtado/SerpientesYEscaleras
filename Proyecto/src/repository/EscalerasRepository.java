package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Escalera;
import model.Escaleras;
import persistance.JsonRepository;

public class EscalerasRepository {
    private static final String RUTA = "src/data/escalerasData.json";

    private JsonRepository<Escaleras> repository;
    private Escaleras misEscaleras;

    public EscalerasRepository() {
        this.repository = new JsonRepository<>(RUTA, Escaleras.class);
        if (new File(RUTA).exists()) {
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