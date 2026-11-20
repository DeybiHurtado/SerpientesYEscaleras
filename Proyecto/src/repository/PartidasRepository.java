package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;

import model.Jugador;
import model.Partida;
import model.Partidas;
import model.Jugadores;
import persistance.JsonRepository;


public class PartidasRepository {
    private JsonRepository<Partidas> repository;
    private Partidas misPartidas;

    public PartidasRepository() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Partida.class, (JsonDeserializer<Partida>) (json, type, context) -> {
                    JsonObject partida = json.getAsJsonObject();
                    JsonElement participantesJson = partida.get("participantes");
                    Jugadores participantes = new Jugadores();
                    if (participantesJson != null && participantesJson.isJsonArray()) {
                        JsonArray nombres = participantesJson.getAsJsonArray();
                        for (JsonElement nombre : nombres) {
                            participantes.anadirJugador(new Jugador(nombre.getAsString()));
                        }
                    } else if (participantesJson != null && participantesJson.isJsonObject()) {
                        participantes = context.deserialize(participantesJson, Jugadores.class);
                    }
                    return new Partida(
                            partida.get("fecha").getAsString(),
                            participantes,
                            partida.get("ganador").getAsString(),
                            partida.get("turnos").getAsInt(),
                            partida.get("contraConsola").getAsBoolean());
                })
                .create();
        this.repository = new JsonRepository<Partidas>("src/data/historialPartidas.json", Partidas.class, gson);
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
