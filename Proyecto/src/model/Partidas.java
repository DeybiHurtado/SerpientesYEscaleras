package model;
<<<<<<< HEAD

=======
import java.lang.reflect.Array;
>>>>>>> 105ed8d2e7515c38353eb0c3c6471f8f1a877c69
import java.util.ArrayList;

public class Partidas {
    private ArrayList<Partida> lista;

    public Partidas(){
        this.lista = new ArrayList<>();
    }

    public ArrayList<Partida> getLista(){
        return lista;
    }

    public void agregar(Partida partida){
        this.lista.add(partida);
    }
}
