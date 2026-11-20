package repository;

import java.util.ArrayList;

import model.RegistroPartida;
import model.RegistroPartidas;
import persistance.JsonRepository;

public class RegistroPartidasRepository {
    private JsonRepository<RegistroPartidas> repository;
    private RegistroPartidas misRegistros;

    public RegistroPartidasRepository() {
        this.repository = new JsonRepository<>("src/data/PartidasGraficas.json", RegistroPartidas.class);
        this.misRegistros = this.repository.readFile();
        if (this.misRegistros == null) {
            this.misRegistros = new RegistroPartidas();
        }
    }

    public void guardarRegistro(RegistroPartida registro) {
        this.misRegistros.agregar(registro);
        this.repository.writeFile(this.misRegistros);
    }

    public ArrayList<RegistroPartida> getTodas() {
        return this.misRegistros.ultimas(Integer.MAX_VALUE);
    }

    public ArrayList<RegistroPartida> getUltimas(int cantidad) {
        return this.misRegistros.ultimas(cantidad);
    }
}