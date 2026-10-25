package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Serpiente;
import model.Serpientes;
import persistance.JsonRepository;

public class SerpientesRepository {
    private static final String RUTA = "src/data/serpientesData.json";

    private JsonRepository<Serpientes> repository;
    private Serpientes misSerpientes;

    public SerpientesRepository() {
        this.repository = new JsonRepository<>(RUTA, Serpientes.class);
        if (new File(RUTA).exists()) {
            this.misSerpientes = repository.readFile();
        }
        if (this.misSerpientes == null) {
            this.misSerpientes = new Serpientes();
        }
    }

    public boolean hayDatos() {
        return !misSerpientes.getLista().isEmpty();
    }

    public ArrayList<Serpiente> getSerpientes() {
        return misSerpientes.getLista();
    }

    public void guardarSerpientes(List<Serpiente> serpientes) {
        this.misSerpientes = new Serpientes(new ArrayList<>(serpientes));
        repository.writeFile(this.misSerpientes);
    }
}