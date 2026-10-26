package controller;


import model.Jugador;
import model.Juego;
import windowView.View;
import model.PartidaGuardada;
import model.Tablero;
import java.util.ArrayList;

public class Control {
    private View view;
    private Juego juego;

    public Control() {
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
}