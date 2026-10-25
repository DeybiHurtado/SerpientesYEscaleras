package repository;

import java.util.ArrayList;

import model.Jugador;
import model.Jugadores;
import persistance.JsonRepository;

public class JugadoresRepository {
      private JsonRepository<Jugadores> repository;
    private Jugadores misJugadores;

    public JugadoresRepository(){
        this.repository = new JsonRepository<>("C:\\Users\\ktunja\\OneDrive\\Escritorio\\PROGRAM 2026.2\\VS\\SerpientesYEscaleras\\SerpientesYEscaleras\\Proyecto\\src\\data\\jugadoresData.json",Jugadores.class);
        this.misJugadores= this.repository.readFile();
        if(this.misJugadores == null){
            this.misJugadores= new Jugadores();
        }
    
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
