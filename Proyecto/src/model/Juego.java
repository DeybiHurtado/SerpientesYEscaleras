package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import repository.JugadoresRepository;
import repository.PartidaGuardadaRepository;
import repository.PartidasRepository;
import repository.EstadisticasRepository;
import repository.RegistroPartidasRepository;

public class Juego {
    private ArrayList<Jugador> jugadoresActuales;
    private JugadoresRepository jugadoresGuardados;
     private PartidaGuardadaRepository partidaGuardadaRepo;
    private PartidasRepository partidasRepo;
    private EstadisticasRepository estadisticasRepo;
      private RegistroPartidasRepository registroPartidasRepo;
    private Tablero tablero;
    private Dados dados;
    private int indiceTurnoActual;
    private boolean juegoTerminado;
    private Jugador ganador;
    private int turnos;
    private boolean vieneDeGuardada;
    private boolean turnoExtraPorParEnCurso;

    // Datos del turno que se está jugando (la vista los lee para dibujar lo mismo que pasó en el modelo)
     private int dado1;
    private int dado2;
    private int avance;
    private int casillaOrigen;
    private int casillaTrasDados;
    private int casillaFinalTurno;
    private String mensajeTurno;
    private String mensajeCasilla;

    public Juego(){
        this.jugadoresGuardados = new JugadoresRepository();
        this.jugadoresActuales = new ArrayList<>();
         this.partidaGuardadaRepo = new PartidaGuardadaRepository();
        this.partidasRepo = new PartidasRepository();
        this.estadisticasRepo = new EstadisticasRepository();
      this.registroPartidasRepo = new RegistroPartidasRepository();
        this.mensajeTurno = "";
        this.mensajeCasilla = "";
    }

    // INICIAR PARTIDAS

    public void iniciarPartidaContraConsola(String nombreJugador) {
        this.iniciarPartidaContraConsola(nombreJugador, 1);
    }
 
    /*metodo evalua crea la partida individual contra la consola recibe el nombre del jugador
    y evalua si se juega con más de un oponente (consola)*/
    public void iniciarPartidaContraConsola(String nombreJugador,int cantidadRivales) {
        this.jugadoresActuales = new ArrayList<>();
        this.jugadoresActuales.add(registerJugador(nombreJugador));
        for ( int i =1; i<= cantidadRivales; i++){
            String nombre ="BOT";
            if(cantidadRivales>1){
                nombre= "BOT "+ i;
            }
            this.jugadoresActuales.add(new Jugador(nombre,true));
        }
        prepararPartida();
        this.vieneDeGuardada = false;
    }

    // Opcion de multijugador, recibe una lista de nombres y por cada uno crea un jugador
    public void iniciarPartidaMultijugador(ArrayList<String> nombresJugadores) {
        this.jugadoresActuales = new ArrayList<>();
        for (String nombre : nombresJugadores) {
            this.jugadoresActuales.add(registerJugador(nombre));
        }
        prepararPartida();
        this.vieneDeGuardada = false;
    }

        // Metodo para "Volver a jugar": mismos jugadores, todos de nuevo en la casilla 1 
    public void reiniciarPartida() {
        ArrayList<Jugador> nuevos = new ArrayList<>();
        for (Jugador jugador : this.jugadoresActuales) {
            nuevos.add(new Jugador(jugador.getNombre(), jugador.isEsConsola()));
        }
        this.jugadoresActuales = nuevos;
        this.prepararPartida();
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
        this.turnoExtraPorParEnCurso = partida.isTurnoExtraPorParEnCurso();
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
        this.turnoExtraPorParEnCurso = false;
    }
    
 public Jugador registerJugador (String jugador){
  Jugador jugadorNuevo = new Jugador (jugador);
  this.jugadoresGuardados.guardarJugador(jugadorNuevo);
   return jugadorNuevo;
 }

 // Para una Partida Guardada

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
    // String fecha -> sirve para guardar la fecha u hora en que se guardo el juego
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return new PartidaGuardada(fecha,
                new ArrayList<>(jugadoresActuales),
                new ArrayList<>(tablero.getSerpientes()),
                new ArrayList<>(tablero.getEscaleras()),
                this.indiceTurnoActual, this.turnos, this.turnoExtraPorParEnCurso);
    }

    public void guardarPartida() {
        partidaGuardadaRepo.guardar(this.crearPartidaGuardada());
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

    //Cuando termina una partida

    public void terminarPartida() {
        if (ganador == null) {
            return;
        }
        Jugadores participantes = new Jugadores(new ArrayList<>(jugadoresActuales));
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        partidasRepo.guardarPartida(new Partida(fecha, participantes, ganador.getNombre(), turnos, isContraConsola()));
        estadisticasRepo.registrarPartida(jugadoresActuales, ganador.getNombre());

        this.registroPartidasRepo.guardarRegistro(this.crearRegistroPartida(fecha));

        if (vieneDeGuardada) {
            partidaGuardadaRepo.borrar();
            vieneDeGuardada = false;
        }
    }

    /*  Detalles guardados de la partida, guarda en el repositorio RegistroPartida
       jugadas de cada jugador, las serpientes y escaleras su poscicion y poderes    */
//-> revisar
        private RegistroPartida crearRegistroPartida(String fecha) {
        ArrayList<ResultadoJugador> resultados = new ArrayList<>();
        for (Jugador jugador : this.jugadoresActuales) {
            resultados.add(new ResultadoJugador(jugador.getNombre(), jugador.getJugadas(), jugador.getPosicion()));
        }
        ArrayList<Escalera> escaleras = new ArrayList<>(this.tablero.getEscaleras());
        escaleras.sort((a, b) -> Integer.compare(a.getBase(), b.getBase()));
        ArrayList<String> textosEscaleras = new ArrayList<>();
        for (int i = 0; i < escaleras.size(); i++) {
            textosEscaleras.add((i + 1) + ". " + escaleras.get(i).getNombrePoder());
        }
        ArrayList<Serpiente> serpientes = new ArrayList<>(this.tablero.getSerpientes());
        serpientes.sort((a, b) -> Integer.compare(a.getCabeza(), b.getCabeza()));
        ArrayList<String> textosSerpientes = new ArrayList<>();
        for (int i = 0; i < serpientes.size(); i++) {
            textosSerpientes.add((i + 1) + ". " + serpientes.get(i).getNombrePoder());
        }
        return new RegistroPartida(fecha, resultados, textosEscaleras, textosSerpientes);
    }

    //TURNOS
// -> 1) empieza el turno y devuelve false si el jugador pierde el turno
    public boolean empezarTurno() {
        this.turnos++;
        Jugador jugadorActual = this.getJugadorActual();
        this.avance = 0;
        this.mensajeCasilla = "";
        this.casillaOrigen = jugadorActual.getPosicion();
        this.casillaTrasDados = this.casillaOrigen;
        this.casillaFinalTurno = this.casillaOrigen;

        if (jugadorActual.isPierdeTurno()) {
            this.mensajeTurno = "Turno de " + jugadorActual.getNombre()
                    + ": Pierde este turno por efecto anterior.";
            jugadorActual.setPierdeTurno(false);
            this.turnoExtraPorParEnCurso = false;
            this.indiceTurnoActual = (this.indiceTurnoActual + 1) % this.jugadoresActuales.size();
            return false;
        }

        jugadorActual.sumarJugada();
        this.mensajeTurno = "Turno de " + jugadorActual.getNombre()
                + " (Casilla actual: " + jugadorActual.getPosicion() + "). ";
        return true;
    }

    //-> 2) Lanza los dados y devuelve cuantas casillas avanza (si el jugador tiene veneno lo reduce)
    public int lanzarDados() {
        Jugador jugadorActual = this.getJugadorActual();
        int tiro = this.dados.lanzarDado();
        this.dado1 = this.dados.getResultadoDado1();
        this.dado2 = this.dados.getResultadoDado2();
        this.avance = tiro;

        if (jugadorActual.getNivelVeneno() == 1) {
            this.avance = tiro / 2;
            this.mensajeTurno += "¡Estás envenenado (Nivel 1)! Avanzas la mitad (" + this.avance + "). ";
            jugadorActual.setNivelVeneno(2);
        } else if (jugadorActual.getNivelVeneno() == 2) {
            this.avance = (int) Math.ceil(tiro * 3.0 / 4.0);
            this.mensajeTurno += "¡El veneno continúa (Nivel 2)! Avanzas 3/4 (" + this.avance + "). ";
            jugadorActual.setNivelVeneno(0);
        } else {
            this.mensajeTurno += "Salió en el dado: " + tiro + ". ";
        }
        return this.avance;
    }
  
   // -> 3) El jugador avanza lo que salio en los dados
    public void avanzarJugador() {
        Jugador jugadorActual = this.getJugadorActual();
        int nuevaPosicion = jugadorActual.getPosicion() + this.avance;

        if (nuevaPosicion >= this.tablero.getCasillaFinal()) {
            jugadorActual.setPosicion(this.tablero.getCasillaFinal());
            this.casillaTrasDados = this.tablero.getCasillaFinal();
            this.casillaFinalTurno = this.tablero.getCasillaFinal();
            this.juegoTerminado = true;
            this.ganador = jugadorActual;
            this.mensajeTurno += "¡" + jugadorActual.getNombre() + " ha llegado a 100 y ha ganado el juego!";
        } else {
            jugadorActual.setPosicion(nuevaPosicion);
            this.casillaTrasDados = nuevaPosicion;
            this.casillaFinalTurno = nuevaPosicion;
        }
    }

    // -> 4) si el jugador cayó en una escalera o serpiente, se aplica sus poderes
    public void aplicarCasilla() {
        if (this.juegoTerminado) {
            return;
        }
        Jugador jugadorActual = this.getJugadorActual();
        String mensaje = this.tablero.evaluarCasillaConPoderes(jugadorActual.getPosicion(), jugadorActual, this.dados);
        if (mensaje != null && !mensaje.isEmpty()) {
            this.mensajeCasilla = mensaje;
            this.mensajeTurno += " " + mensaje;
        }
        this.casillaFinalTurno = jugadorActual.getPosicion();

        if (jugadorActual.getPosicion() >= this.tablero.getCasillaFinal()) {
            jugadorActual.setPosicion(this.tablero.getCasillaFinal());
            this.casillaFinalTurno = this.tablero.getCasillaFinal();
            this.juegoTerminado = true;
            this.ganador = jugadorActual;
            this.mensajeTurno += " ¡" + jugadorActual.getNombre() + " llegó a la meta y ha ganado!";
        }
    }

    //-> 5) pasa el turno siguiente al jugador o repite si hay turno extra

    public void pasarTurno() {
        Jugador jugadorActual = this.getJugadorActual();
        boolean turnoExtraPorPoder = jugadorActual.isTieneTurnoExtra();
        jugadorActual.setTieneTurnoExtra(false);
        boolean turnoExtraPorPar = this.dado1 == this.dado2 && !this.turnoExtraPorParEnCurso;
        this.turnoExtraPorParEnCurso = turnoExtraPorPar
                || (this.turnoExtraPorParEnCurso && turnoExtraPorPoder);

        if (turnoExtraPorPar) {
            this.mensajeTurno += " ¡Sacaste un par y tienes un turno extra!";
        }
        if (turnoExtraPorPoder || turnoExtraPorPar) {
            if (turnoExtraPorPoder) {
                this.mensajeTurno += " ¡Obtienes un turno extra!";
            }
        } else {
            this.turnoExtraPorParEnCurso = false;
            this.indiceTurnoActual = (this.indiceTurnoActual + 1) % this.jugadoresActuales.size();
        }
    }

    // Orquesta todo lo que hace el turno de cada jugador
     public String jugarTurno() {
        if (juegoTerminado) {
            return "El juego ya terminó.";
        }
 if (!this.empezarTurno()) {
            return this.mensajeTurno;
        }
        this.lanzarDados();
        this.avanzarJugador();
        this.aplicarCasilla();
        this.pasarTurno();
        return this.mensajeTurno;
        }


        // GETTERS
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

     public Jugador getGanador() {
        return ganador;
    }
    
    public Tablero getTablero() {
        return tablero;
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

    
   

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

   
     public Tablero cargarTablero() {
        return new Tablero();
    }

    

    public void setJugadoresActuales(ArrayList<Jugador> jugadoresActuales) {
        this.jugadoresActuales = jugadoresActuales;
    }

    public PartidaGuardadaRepository getPartidaGuardadaRepo() {
        return partidaGuardadaRepo;
    }

    public void setPartidaGuardadaRepo(PartidaGuardadaRepository partidaGuardadaRepo) {
        this.partidaGuardadaRepo = partidaGuardadaRepo;
    }

    public PartidasRepository getPartidasRepo() {
        return partidasRepo;
    }

    public void setPartidasRepo(PartidasRepository partidasRepo) {
        this.partidasRepo = partidasRepo;
    }

    public EstadisticasRepository getEstadisticasRepo() {
        return estadisticasRepo;
    }

    public void setEstadisticasRepo(EstadisticasRepository estadisticasRepo) {
        this.estadisticasRepo = estadisticasRepo;
    }

    public RegistroPartidasRepository getRegistroPartidasRepo() {
        return registroPartidasRepo;
    }

    public void setRegistroPartidasRepo(RegistroPartidasRepository registroPartidasRepo) {
        this.registroPartidasRepo = registroPartidasRepo;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Dados getDados() {
        return dados;
    }

    public void setDados(Dados dados) {
        this.dados = dados;
    }

    public int getIndiceTurnoActual() {
        return indiceTurnoActual;
    }

    public void setIndiceTurnoActual(int indiceTurnoActual) {
        this.indiceTurnoActual = indiceTurnoActual;
    }

    public void setJuegoTerminado(boolean juegoTerminado) {
        this.juegoTerminado = juegoTerminado;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }

    public void setTurnos(int turnos) {
        this.turnos = turnos;
    }

    public boolean isVieneDeGuardada() {
        return vieneDeGuardada;
    }

    public void setVieneDeGuardada(boolean vieneDeGuardada) {
        this.vieneDeGuardada = vieneDeGuardada;
    }

    public int getDado1() {
        return dado1;
    }

    public void setDado1(int dado1) {
        this.dado1 = dado1;
    }

    public int getDado2() {
        return dado2;
    }

    public void setDado2(int dado2) {
        this.dado2 = dado2;
    }

    public int getAvance() {
        return avance;
    }

     public boolean huboSalto() {
        return this.casillaTrasDados != this.casillaFinalTurno;
    }
    public void setAvance(int avance) {
        this.avance = avance;
    }

    public int getCasillaOrigen() {
        return casillaOrigen;
    }

    public void setCasillaOrigen(int casillaOrigen) {
        this.casillaOrigen = casillaOrigen;
    }

    public int getCasillaTrasDados() {
        return casillaTrasDados;
    }

    public void setCasillaTrasDados(int casillaTrasDados) {
        this.casillaTrasDados = casillaTrasDados;
    }

    public int getCasillaFinalTurno() {
        return casillaFinalTurno;
    }

    public void setCasillaFinalTurno(int casillaFinalTurno) {
        this.casillaFinalTurno = casillaFinalTurno;
    }

    public String getMensajeTurno() {
        return mensajeTurno;
    }

    public void setMensajeTurno(String mensajeTurno) {
        this.mensajeTurno = mensajeTurno;
    }

    public String getMensajeCasilla() {
        return mensajeCasilla;
    }

    public void setMensajeCasilla(String mensajeCasilla) {
        this.mensajeCasilla = mensajeCasilla;
    }
    
  
}