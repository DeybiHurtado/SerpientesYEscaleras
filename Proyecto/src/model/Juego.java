package model;

import java.util.ArrayList;

public class Juego {
    private ArrayList<Jugador> jugadoresActuales;
    private Tablero tablero;
    private Dado dado;
    private int indiceTurnoActual;
    private boolean juegoTerminado;
    private Jugador ganador;

    public Juego(ArrayList<String> nombresJugadores) {
        this.jugadoresActuales = new ArrayList<>();
        for (String nombre : nombresJugadores) {
            this.jugadoresActuales.add(new Jugador(nombre));
        }
        this.tablero = new Tablero();
        this.dado = new Dado();
        this.indiceTurnoActual = 0;
        this.juegoTerminado = false;
    }

    public String jugarTurno() {
        if (juegoTerminado) return "El juego ya terminó.";

        Jugador jugadorActual = jugadoresActuales.get(indiceTurnoActual);
        int tirada = dado.lanzarDado()+dado.lanzarDado();
        int nuevaPosicion = jugadorActual.getPosicion() + tirada;

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Turno de ").append(jugadorActual.getNombre())
               .append(" (Casilla ").append(jugadorActual.getPosicion())
               .append(") - Salió en el dado: ").append(tirada);

        if (nuevaPosicion >= tablero.getCasillaFinal()) {
            jugadorActual.setPosicion(tablero.getCasillaFinal());
            juegoTerminado = true;
            ganador = jugadorActual;
            mensaje.append("\n¡").append(jugadorActual.getNombre()).append(" ha llegado a 100 y ha ganado el juego!");
        } else {
            int posicionConEfecto = tablero.evaluarCasilla(nuevaPosicion);
            jugadorActual.setPosicion(posicionConEfecto);
            mensaje.append("\nPosición final tras el movimiento: ").append(posicionConEfecto);
        }

        indiceTurnoActual = (indiceTurnoActual + 1) % jugadoresActuales.size();
        return mensaje.toString();
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public Jugador getGanador() {
        return ganador;
    }
}