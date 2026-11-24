package controller;

import java.util.ArrayList;

import model.Juego;
import model.PartidaGuardada;
import model.RegistroPartida;
import model.ResumenHistorialPartidas;
import model.Jugador;
import model.Tablero;
import windowView.View;

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
            String error = this.validarNombre(nombre, yaUsados);
            if (error.isEmpty()) {
                return nombre.trim();
            }
            view.mostrarMensaje(error);
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
        String nombreNormalizado = nombre == null ? "" : nombre.trim();
        if (nombreNormalizado.isEmpty()) {
            return "El nombre no puede estar vacío.";
        }
        if (nombreNormalizado.codePointCount(0, nombreNormalizado.length()) > 20) {
            return "El nombre no puede tener más de 20 caracteres.";
        }
        if (nombreNormalizado.regionMatches(true, 0, "consola", 0, "consola".length())
                || nombreNormalizado.equalsIgnoreCase("BOT")) {
            return "Ese nombre está reservado para la computadora.";
        }
        if (!nombreNormalizado.matches("[\\p{L}\\p{M}\\p{N} ]+")) {
            return "El nombre solo puede contener letras, números y espacios.";
        }
        if (yaUsados != null && this.contieneIgnorandoMayusculas(yaUsados, nombreNormalizado)) {
            return "Ese nombre ya está en uso en esta partida.";
        }
        return "";
    }

    public String iniciarPartidaIndividual(String nombre, int cantidadRivales) {
        if (cantidadRivales < 1 || cantidadRivales > 3) {
            throw new IllegalArgumentException("La cantidad de rivales debe estar entre 1 y 3.");
        }
        String nombreJugador = nombre == null ? "" : nombre.trim();
        String error = this.validarNombre(nombreJugador, new ArrayList<>());
        if (!error.isEmpty()) {
            return error;
        }
        this.juego.iniciarPartidaContraConsola(nombreJugador, cantidadRivales);
        return "";
    }

    public void iniciarPartidaMultijugador(ArrayList<String> nombres) {
        this.juego.iniciarPartidaMultijugador(nombres);
    }

    public String iniciarPartidaMultijugador(String[] nombres, boolean[] jugadoresActivos) {
        if (nombres == null || jugadoresActivos == null || nombres.length != jugadoresActivos.length) {
            throw new IllegalArgumentException("Los nombres y jugadores activos deben tener el mismo tamaño.");
        }
        ArrayList<String> participantes = new ArrayList<>();
        for (int i = 0; i < nombres.length; i++) {
            if (jugadoresActivos[i]) {
                String nombre = nombres[i] == null ? "" : nombres[i].trim();
                String error = this.validarNombre(nombre, participantes);
                if (!error.isEmpty()) {
                    return "Jugador " + (i + 1) + ": " + error;
                }
                participantes.add(nombre);
            }
        }
        if (participantes.size() < 2 || participantes.size() > 4) {
            throw new IllegalArgumentException("El modo multijugador requiere de 2 a 4 jugadores.");
        }
        this.juego.iniciarPartidaMultijugador(participantes);
        return "";
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

    public ResumenHistorialPartidas getResumenHistorialPartidas(String filtro, int cantidadMaxima) {
        return new ResumenHistorialPartidas(this.getRegistrosPartidas(), filtro, cantidadMaxima);
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

    public int getNivelVenenoJugador(int indice) {
        return this.juego.getJugadoresActuales().get(indice).getNivelVeneno();
    }

    public boolean esEscalera(int casilla) {
        return this.juego.getTablero().esEscalera(casilla);
    }

    public String getNombrePoder(int casilla) {
        return this.juego.getTablero().getNombrePoder(casilla);
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