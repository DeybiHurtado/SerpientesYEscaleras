package model;

import java.util.ArrayList;

public class PartidaGuardada {

    private String fechaGuardado;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Serpiente> serpientes;
    private ArrayList<Escalera> escaleras;
    private int indiceTurnoActual;
    private int turnos;
    private boolean turnoExtraPorParEnCurso;

    public PartidaGuardada(String fechaGuardado, ArrayList<Jugador> jugadores, ArrayList<Serpiente> serpientes,ArrayList<Escalera> escaleras,int indiceTurnoActual,int turnos){
        this(fechaGuardado, jugadores, serpientes, escaleras, indiceTurnoActual, turnos, false);
    }

    public PartidaGuardada(String fechaGuardado, ArrayList<Jugador> jugadores, ArrayList<Serpiente> serpientes,
            ArrayList<Escalera> escaleras, int indiceTurnoActual, int turnos, boolean turnoExtraPorParEnCurso) {

        this.fechaGuardado = fechaGuardado;
        this.jugadores = jugadores;
        this.serpientes = serpientes;
        this.escaleras = escaleras;
        this.indiceTurnoActual = indiceTurnoActual;
        this.turnos = turnos;
        this.turnoExtraPorParEnCurso = turnoExtraPorParEnCurso;

    }

    public String getFechaGuardado(){
        return fechaGuardado;
    }

    public ArrayList<Jugador> getJugadores(){
        return jugadores;
    }

    public ArrayList<Serpiente> getSerpientes(){
        return serpientes;
    }

    public ArrayList<Escalera> getEscaleras(){
        return escaleras;
    }

    public int getIndiceTurnoActual(){
        return indiceTurnoActual;
    }

    public int getTurnos(){
        return turnos;
    }

    public boolean isTurnoExtraPorParEnCurso() {
        return this.turnoExtraPorParEnCurso;
    }
    
}
