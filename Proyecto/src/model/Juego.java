package model;

import java.util.ArrayList;

import repository.JugadoresRepository;

public class Juego {
    private ArrayList<Jugador> jugadoresActuales;
    private JugadoresRepository jugadoresGuardados;
    private Tablero tablero;
    private Dados dado;
    private int indiceTurnoActual;
    private boolean juegoTerminado;
    private Jugador ganador;

    public Juego(){
        this.jugadoresGuardados = new JugadoresRepository();
        this.jugadoresActuales = new ArrayList<>();
    }

    public Juego(JugadoresRepository jugadores){
        this.jugadoresGuardados = jugadores;
    }
    
 public void registerJugador (String jugador){
  Jugador jugadorNuevo = new Jugador (jugador);
  this.jugadoresGuardados.guardarJugador(jugadorNuevo);
 }

 
    public JugadoresRepository getJugadoresGuardados() {
    return jugadoresGuardados;
}

 public void setJugadoresGuardados(JugadoresRepository jugadoresGuardados) {
    this.jugadoresGuardados = jugadoresGuardados;
 }

    public Juego(ArrayList<String> nombresJugadores) {
        this.jugadoresActuales = new ArrayList<>();
        for (String nombre : nombresJugadores) {
        //    this.jugadoresActuales.add(new Jugador(nombre));
             Jugador jugador = new Jugador(nombre);
        this.jugadoresActuales.add(jugador);
    
        }
        this.tablero = new Tablero();
        this.dado = new Dados();
        this.indiceTurnoActual = 0;
        this.juegoTerminado = false;
    }

    public String jugarTurno() {
        if (juegoTerminado) {
            return "El juego ya terminó.";
        }

        Jugador jugadorActual = jugadoresActuales.get(indiceTurnoActual);
        StringBuilder mensaje = new StringBuilder();

        if (jugadorActual.isPierdeTurno()) {
            mensaje.append("Turno de ").append(jugadorActual.getNombre())
                   .append(": Pierde este turno por efecto anterior.");
            jugadorActual.setPierdeTurno(false);
            indiceTurnoActual = (indiceTurnoActual + 1) % jugadoresActuales.size();
            return mensaje.toString();
        }

        mensaje.append("Turno de ").append(jugadorActual.getNombre())
               .append(" (Casilla actual: ").append(jugadorActual.getPosicion()).append("). ");

        int tiro = dado.lanzarDado()+dado.lanzarDado();
        int avanceReal = tiro;

        if (jugadorActual.getNivelVeneno() == 1) {
            avanceReal = tiro / 2;
            mensaje.append("¡Estás envenenado (Nivel 1)! Avanzas la mitad (").append(avanceReal).append("). ");
            jugadorActual.setNivelVeneno(2);
        } else if (jugadorActual.getNivelVeneno() == 2) {
            avanceReal = (int) Math.ceil(tiro * 3.0 / 4.0);
            mensaje.append("¡El veneno continúa (Nivel 2)! Avanzas 3/4 (").append(avanceReal).append("). ");
            jugadorActual.setNivelVeneno(0);
        } else {
            mensaje.append("Salió en el dado: ").append(tiro).append(". ");
        }

        int nuevaPosicion = jugadorActual.getPosicion() + avanceReal;

        if (nuevaPosicion >= tablero.getCasillaFinal()) {
            jugadorActual.setPosicion(tablero.getCasillaFinal());
            juegoTerminado = true;
            ganador = jugadorActual;
            mensaje.append("¡").append(jugadorActual.getNombre()).append(" ha llegado a 100 y ha ganado el juego!");
        } else {
            jugadorActual.setPosicion(nuevaPosicion);
            String mensajeCasilla = tablero.evaluarCasillaConPoderes(nuevaPosicion, jugadorActual, dado);
            if (mensajeCasilla != null && !mensajeCasilla.isEmpty()) {
                mensaje.append(" ").append(mensajeCasilla);
            }

            if (jugadorActual.getPosicion() >= tablero.getCasillaFinal()) {
                jugadorActual.setPosicion(tablero.getCasillaFinal());
                juegoTerminado = true;
                ganador = jugadorActual;
                mensaje.append(" ¡").append(jugadorActual.getNombre()).append(" llegó a la meta y ha ganado!");
            }
        }

        if (jugadorActual.isTieneTurnoExtra()) {
            jugadorActual.setTieneTurnoExtra(false);
            mensaje.append(" ¡Obtienes un turno extra!");
        } else {
            indiceTurnoActual = (indiceTurnoActual + 1) % jugadoresActuales.size();
        }

        return mensaje.toString();
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public Jugador getGanador() {
        return ganador;
    }
}