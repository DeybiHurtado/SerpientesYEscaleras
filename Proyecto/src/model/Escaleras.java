package model;

import java.util.ArrayList;

public class Escaleras {
    private ArrayList<Escalera> lista;

    public Escaleras() {
        this.lista = new ArrayList<>();
    }

    public Escaleras(ArrayList<Escalera> lista) {
        this.lista = lista;
    }

    public ArrayList<Escalera> getLista() {
        return lista;
    }
}