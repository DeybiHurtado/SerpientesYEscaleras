package controller;


import model.Jugador;
import model.Juego;
import model.RegistroPartida;
import model.PartidaGuardada;
import model.Tablero;
import windowView.View;
import java.util.ArrayList;

public class control {
    private View view;
    private Juego juego;

    public control() {
        this.view = new View();
        juego = new Juego();
    }

    public void iniciarJuego() {
        view.mostrarBienvenida();

       boolean salir = false;
        while (!salir) {
            int opcion = view.mostrarMenuPrincipal(juego.hayPartidaGuardada());
            switch (opcion) {
                case 1:
                    jugarContraConsola();
                    break;
                case 2:
                    jugarMultijugador();
                    break;
                case 3:
                    continuarPartida();
                    break;
                case 4:
                    verTablero();
                    break;
                case 5:
                    view.mostrarEstadisticas(juego.getEstadisticas(), juego.getPartidas());
                    view.esperarEnter();
                    break;
                case 6:
                    view.mostrarInfo();
                    break;
                case 7:
                    salir = true;
                    view.mostrarDespedida();
                    break;
            }
        }
}

 private void jugarContraConsola() {
        String nombre = pedirNombreValido(view.pedirNombreUnico(), new ArrayList<>(), 0);
        juego.iniciarPartidaContraConsola(nombre);
        jugarPartida();
    }

     private void jugarMultijugador() {
        int numJugadores = view.pedirNumeroJugadores();
        ArrayList<String> nombres = new ArrayList<>();
        for (int i = 1; i <= numJugadores; i++) {
            String nombre = pedirNombreValido(view.pedirNombreJugador(i), nombres, i);
            nombres.add(nombre);
        }
        juego.iniciarPartidaMultijugador(nombres);
        jugarPartida();
    }

    private void continuarPartida() {
        PartidaGuardada guardada = juego.getPartidaGuardada();
        if (guardada == null) {
            view.mostrarMensaje("No hay ninguna partida guardada.");
            return;
        }
        view.mostrarPartidaGuardada(guardada);
        juego.continuarPartidaGuardada();
        jugarPartida();
    }

    private void verTablero() {
        Tablero tablero = juego.cargarTablero();
        view.mostrarTablero(tablero.getSerpientes(), tablero.getEscaleras(), tablero.getCasillaFinal());
    }

    private void jugarPartida() {
        view.mostrarInicioJuego();

        while (!juego.isJuegoTerminado()) {
            Jugador actual = juego.getJugadorActual();

            if (!actual.isEsConsola()) {
                String accion = view.pedirAccionTurno(actual.getNombre());
                if (accion.equals("G")) {
                    juego.guardarPartida();
                    view.mostrarMensaje("Partida guardada. Puedes continuar jugando.");
                    continue;
                }
                if (accion.equals("M")) {
                    if (view.confirmar("¿Volver al menú? Lo que no hayas guardado se perderá")) {
                        return;
                    }
                    continue;
                }
            }

            String resultadoTurno = juego.jugarTurno();
            view.mostrarMensaje(resultadoTurno);
            view.mostrarSeparador();
        }

        view.mostrarGanador(juego.getGanador().getNombre());
        juego.terminarPartida();

        view.mostrarMensaje("\nPresiona ENTER para volver al menú...");
        view.esperarEnter();
    }
private String pedirNombreValido(String nombre, ArrayList<String> yaUsados, int numero) {
        while (true) {
            if (nombre.isEmpty()) {
                view.mostrarMensaje("El nombre no puede estar vacío.");
            } else if (nombre.equalsIgnoreCase("Consola")) {
                view.mostrarMensaje("Ese nombre está reservado para la computadora.");
            } else if (contieneIgnorandoMayusculas(yaUsados, nombre)) {
                view.mostrarMensaje("Ese nombre ya está en uso en esta partida.");
            } else {
                return nombre;
            }
            nombre = (numero == 0) ? view.pedirNombreUnico() : view.pedirNombreJugador(numero);
        }
    }
     private boolean contieneIgnorandoMayusculas(ArrayList<String> lista, String nombre) {
        for (String s : lista) {
            if (s.equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    // La parte grafica, las ventanas llamas a estos metodos y el controlador le pide todo al modelo para que la vista no toque el modelo
    // muestra un mensaje de error si el nombre esta mal o esta vacio 
    public String validarNombre(String nombre, ArrayList<String> yaUsados) {
        if (nombre.isEmpty()) {
            return "El nombre no puede estar vacío.";
        }
        if (nombre.toLowerCase().startsWith("consola")) {
            return "Ese nombre está reservado para la computadora.";
        }
        if (this.contieneIgnorandoMayusculas(yaUsados, nombre)) {
            return "Ese nombre ya está en uso en esta partida.";
        }
        return "";
    }

    public void iniciarPartidaIndividual(String nombre, int cantidadRivales) {
        this.juego.iniciarPartidaContraConsola(nombre, cantidadRivales);
    }

    public void iniciarPartidaMultijugador(ArrayList<String> nombres) {
        this.juego.iniciarPartidaMultijugador(nombres);
    }

    public boolean continuarPartidaGuardada() {
        return this.juego.continuarPartidaGuardada();
    }

    public boolean hayPartidaGuardada() {
        return this.juego.hayPartidaGuardada();
    }

    public ArrayList<RegistroPartida> getRegistrosPartidas() {
        return this.juego.getRegistroPartidasRepo().getTodas();
    }

    public void reiniciarPartida() {
        this.juego.reiniciarPartida();
    }

    public void guardarPartida() {
        this.juego.guardarPartida();
    }

    public void terminarPartida() {
        this.juego.terminarPartida();
    }

    // El turno, paso a paso
    public boolean empezarTurno() {
        return this.juego.empezarTurno();
    }

    public int lanzarDados() {
        return this.juego.lanzarDados();
    }

    public void avanzarJugador() {
        this.juego.avanzarJugador();
    }

    public void aplicarCasilla() {
        this.juego.aplicarCasilla();
    }

    public void pasarTurno() {
        this.juego.pasarTurno();
    }

    // Lo que la vista necesita para dibujar
    public ArrayList<Jugador> getJugadores() {
        return this.juego.getJugadoresActuales();
    }

    public int getIndiceTurno() {
        return this.juego.getIndiceTurnoActual();
    }

    public int getDado1() {
        return this.juego.getDado1();
    }

    public int getDado2() {
        return this.juego.getDado2();
    }

    public int getAvance() {
        return this.juego.getAvance();
    }

    public int getCasillaTrasDados() {
        return this.juego.getCasillaTrasDados();
    }

    public int getCasillaFinalTurno() {
        return this.juego.getCasillaFinalTurno();
    }

    public boolean huboSalto() {
        return this.juego.huboSalto();
    }

    public String getMensajeTurno() {
        return this.juego.getMensajeTurno();
    }

    public String getMensajeCasilla() {
        return this.juego.getMensajeCasilla();
    }

    public boolean isJuegoTerminado() {
        return this.juego.isJuegoTerminado();
    }

    public Jugador getGanador() {
        return this.juego.getGanador();
    }

    public Tablero getTablero() {
        return this.juego.getTablero();
    }
}