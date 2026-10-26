package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import repository.JugadoresRepository;
import repository.PartidaGuardadaRepository;
import repository.PartidasRepository;
import repository.EstadisticasRepository;

public class Juego {
    private ArrayList<Jugador> jugadoresActuales;
    private JugadoresRepository jugadoresGuardados;
     private PartidaGuardadaRepository partidaGuardadaRepo;
    private PartidasRepository partidasRepo;
    private EstadisticasRepository estadisticasRepo;
    private Tablero tablero;
    private Dados dados;
    private int indiceTurnoActual;
    private boolean juegoTerminado;
    private Jugador ganador;
    private int turnos;
    private boolean vieneDeGuardada;

    public Juego(){
        this.jugadoresGuardados = new JugadoresRepository();
        this.jugadoresActuales = new ArrayList<>();
         this.partidaGuardadaRepo = new PartidaGuardadaRepository();
        this.partidasRepo = new PartidasRepository();
        this.estadisticasRepo = new EstadisticasRepository();
    }

    public void iniciarPartidaContraConsola(String nombreJugador) {
        this.jugadoresActuales = new ArrayList<>();
        this.jugadoresActuales.add(registerJugador(nombreJugador));
        this.jugadoresActuales.add(new Jugador("Consola", true));
        prepararPartida();
        this.vieneDeGuardada = false;
    }
    public void iniciarPartidaMultijugador(ArrayList<String> nombresJugadores) {
        this.jugadoresActuales = new ArrayList<>();
        for (String nombre : nombresJugadores) {
            this.jugadoresActuales.add(registerJugador(nombre));
        }
        prepararPartida();
        this.vieneDeGuardada = false;
    }

    public boolean continuarPartidaGuardada() {
        PartidaGuardada partida = partidaGuardadaRepo.cargar();
        if (partida == null) {
            return false;
        }
        this.jugadoresActuales = new ArrayList<>(partida.getJugadores());
        int mayorId = 0;
        for (Jugador j : this.jugadoresActuales) {
            mayorId = Math.max(mayorId, j.getId());
        }
        Jugador.ajustarContador(mayorId + 1);
        this.tablero = new Tablero(partida.getSerpientes(), partida.getEscaleras());
        this.dados = new Dados();
        this.indiceTurnoActual = partida.getIndiceTurnoActual();
        this.turnos = partida.getTurnos();
        this.juegoTerminado = false;
        this.ganador = null;
        this.vieneDeGuardada = true;
        return true;
    }

    private void prepararPartida() {
        this.tablero = new Tablero();
        this.dados = new Dados();
        this.indiceTurnoActual = 0;
        this.turnos = 0;
        this.juegoTerminado = false;
        this.ganador = null;
    }
    
 public Jugador registerJugador (String jugador){
  Jugador jugadorNuevo = new Jugador (jugador);
  this.jugadoresGuardados.guardarJugador(jugadorNuevo);
   return jugadorNuevo;
 }

public boolean hayPartidaGuardada() {
        return partidaGuardadaRepo.hayPartida();
    }

    public PartidaGuardada getPartidaGuardada() {
        return partidaGuardadaRepo.cargar();
    }
 public void setJugadoresGuardados(JugadoresRepository jugadoresGuardados) {
    this.jugadoresGuardados = jugadoresGuardados;
 }

  public PartidaGuardada crearPartidaGuardada() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return new PartidaGuardada(fecha,
                new ArrayList<>(jugadoresActuales),
                new ArrayList<>(tablero.getSerpientes()),
                new ArrayList<>(tablero.getEscaleras()),
                indiceTurnoActual, turnos);
    }

    public Juego(ArrayList<String> nombresJugadores) {
        this.jugadoresActuales = new ArrayList<>();
        for (String nombre : nombresJugadores) {
             Jugador jugador = new Jugador(nombre);
        this.jugadoresActuales.add(jugador);
    
        }
        this.tablero = new Tablero();
        this.dados = new Dados();
        this.indiceTurnoActual = 0;
        this.juegoTerminado = false;
    }
public Jugador getJugadorActual() {
        return jugadoresActuales.get(indiceTurnoActual);
    }

    public ArrayList<Jugador> getJugadoresActuales() {
        return jugadoresActuales;
    }

     public boolean isContraConsola() {
        for (Jugador j : jugadoresActuales) {
            if (j.isEsConsola()) {
                return true;
            }
        }
        return false;
    }
 public int getTurnos() {
        return turnos;
    }

    public void guardarPartida() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        PartidaGuardada partida = new PartidaGuardada(fecha,
                new ArrayList<>(jugadoresActuales),
                new ArrayList<>(tablero.getSerpientes()),
                new ArrayList<>(tablero.getEscaleras()),
                indiceTurnoActual, turnos);
        partidaGuardadaRepo.guardar(partida);
    }

    public void terminarPartida() {
        if (ganador == null) {
            return;
        }
        ArrayList<String> participantes = new ArrayList<>();
        for (Jugador j : jugadoresActuales) {
            participantes.add(j.getNombre());
        }
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        partidasRepo.guardarPartida(new Partida(fecha, participantes, ganador.getNombre(), turnos, isContraConsola()));
        estadisticasRepo.registrarPartida(jugadoresActuales, ganador.getNombre());

        // Si era la partida guardada y ya terminó, no tiene sentido seguir guardándola
        if (vieneDeGuardada) {
            partidaGuardadaRepo.borrar();
            vieneDeGuardada = false;
        }
    }

    public String jugarTurno() {
        if (juegoTerminado) {
            return "El juego ya terminó.";
        }
 turnos++;
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

        int tiro = dados.lanzarDado();
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
            String mensajeCasilla = tablero.evaluarCasillaConPoderes(nuevaPosicion, jugadorActual, dados);
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
    
    public Tablero getTablero() {
        return tablero;
    }
     public Tablero cargarTablero() {
        return new Tablero();
    }

    public ArrayList<EstadisticaJugador> getEstadisticas() {
        return estadisticasRepo.getEstadisticas();
    }

    public ArrayList<Partida> getPartidas() {
        return partidasRepo.getPartidas();
    }

    public JugadoresRepository getJugadoresGuardados() {
        return jugadoresGuardados;
    }
  
}