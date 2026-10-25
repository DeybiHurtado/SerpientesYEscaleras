package model;

import java.util.ArrayList;

public class Serpientes {
    private ArrayList<Serpiente> lista;

    public Serpientes() {
        this.lista = new ArrayList<>();
    }

    public Serpientes(ArrayList<Serpiente> lista) {
        this.lista = lista;
    }

    public ArrayList<Serpiente> getLista() {
        return lista;
    }
}