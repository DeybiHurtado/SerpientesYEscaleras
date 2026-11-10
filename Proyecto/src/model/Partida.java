package model;

import java.util.ArrayList;

public class Partida {
    private String fecha;
    private Jugadores participantes;
    private Jugador currentJugador;
    private String ganador;
    private int turnos;
    private boolean contraConsola;

    public Partida(String fecha, Jugadores participantes, String ganador, int turnos, boolean contraConsola){

        this.fecha = fecha;
        this.participantes = participantes;
        this.ganador = ganador;
        this.turnos = turnos;
        this.contraConsola = contraConsola;
    }

    public String getFecha(){
        return fecha;
    }

    public Jugadores getParticipantes(){
        return participantes;
    }

    public String getGanador(){
        return ganador;
    }

    public int getTurnos(){
        return turnos;
    }

    public boolean contraConsola(){
        return contraConsola;
    }
}
