package model;

import java.lang.reflect.Array;

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
