package model;

import java.util.ArrayList;

// Guarda todo lo que paso en una partida jugada en la parte grafica
public class RegistroPartida {
    private String fecha;
    private ArrayList<ResultadoJugador> jugadores;
    private ArrayList<String> escaleras;
    private ArrayList<String> serpientes;

    public RegistroPartida(String fecha, ArrayList<ResultadoJugador> jugadores, ArrayList<String> escaleras,
            ArrayList<String> serpientes) {
        this.fecha = fecha;
        this.jugadores = jugadores;
        this.escaleras = escaleras;
        this.serpientes = serpientes;
    }

    // El primero de la lista es el ganador, los demas van segun la casilla donde quedaron
    public ArrayList<ResultadoJugador> getOrdenLlegada() {
        ArrayList<ResultadoJugador> orden = new ArrayList<>(this.jugadores);
        orden.sort((a, b) -> b.getCasillaFinal() - a.getCasillaFinal());
        return orden;
    }

    // Busca un jugador cuyo nombre contenga el texto (sin importar mayusculas)
    public ResultadoJugador buscarJugador(String texto) {
        for (ResultadoJugador jugador : this.jugadores) {
            if (jugador.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                return jugador;
            }
        }
        return null;
    }

    public boolean ganoElJugador(ResultadoJugador jugador) {
        return this.getOrdenLlegada().get(0) == jugador;
    }

    public String getGanador() {
        return this.getOrdenLlegada().get(0).getNombre();
    }

    public int getJugadasDelGanador() {
        return this.getOrdenLlegada().get(0).getJugadas();
    }

    public String getFecha() {
        return this.fecha;
    }

    public ArrayList<String> getEscaleras() {
        return this.escaleras;
    }

    public ArrayList<String> getSerpientes() {
        return this.serpientes;
    }
}