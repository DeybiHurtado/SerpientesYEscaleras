package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Serpiente;
import model.Serpientes;
import persistance.JsonRepository;
import persistance.Rutas;

public class SerpientesRepository {
    private JsonRepository<Serpientes> repository;
    private Serpientes misSerpientes;

    public SerpientesRepository() {
        this.repository = new JsonRepository<>(Rutas.data("Serpientes.json"), Serpientes.class);
        if (new File(Rutas.data("Serpientes.json")).exists()) {
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