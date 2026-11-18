package repository;


import java.util.ArrayList;
import java.util.List;

import model.Serpiente;
import model.Serpientes;
import persistance.JsonRepository;


public class SerpientesRepository {
    private JsonRepository<Serpientes> repository;
    private Serpientes misSerpientes;

    public SerpientesRepository() {
       this.repository = new JsonRepository<>("src/data/Serpientes.json", Serpientes.class);
if (this.repository.exists()) {
    this.misSerpientes = this.repository.readFile();
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