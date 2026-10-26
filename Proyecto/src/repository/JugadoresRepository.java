package repository;

import java.util.ArrayList;

import model.Jugador;
import model.Jugadores;
import persistance.JsonRepository;
import persistance.Rutas;

public class JugadoresRepository {
      private JsonRepository<Jugadores> repository;
    private Jugadores misJugadores;

    public JugadoresRepository(){
        this.repository = new JsonRepository<>(Rutas.data("jugadoresData.json"), Jugadores.class);
        this.misJugadores= this.repository.readFile();
        if(this.misJugadores == null){
            this.misJugadores= new Jugadores();
        }
          int mayorId = 0;
        for (Jugador j : this.misJugadores.getArrayJugadores()) {
            if (j.getId() > mayorId) {
                mayorId = j.getId();
            }
        }
        Jugador.ajustarContador(mayorId + 1);
    
    }
    public void guardarJugador(Jugador jugador){
        this.misJugadores.anadirJugador(jugador);
        repository.writeFile(this.misJugadores);
    }

    public Jugadores subirJugadores(){
        return this.misJugadores;

    }

public ArrayList<Jugador> getPlayersNames(){
    return this.misJugadores.getArrayJugadores();
}
}
