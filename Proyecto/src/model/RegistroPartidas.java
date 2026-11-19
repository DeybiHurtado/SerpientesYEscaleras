package model;

import java.util.ArrayList;

public class RegistroPartidas {
    private ArrayList<RegistroPartida> lista;

    public RegistroPartidas() {
        this.lista = new ArrayList<>();
    }

    public void agregar(RegistroPartida registro) {
        this.lista.add(registro);
    }

    // Devuelve las ultimas partidas, la mas reciente primero
    public ArrayList<RegistroPartida> ultimas(int cantidad) {
        ArrayList<RegistroPartida> resultado = new ArrayList<>();
        for (int i = this.lista.size() - 1; i >= 0 && resultado.size() < cantidad; i--) {
            resultado.add(this.lista.get(i));
        }
        return resultado;
    }
}
