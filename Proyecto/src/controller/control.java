package controller;

import model.Juego;
import windowView.View;
import java.util.ArrayList;

public class Control {
    private View view;
    private Juego juego;

    public Control() {
        this.view = new View();
    }

    public void iniciarJuego() {
        view.mostrarBienvenida();

        int numJugadores = view.pedirNumeroJugadores();

        ArrayList<String> nombres = new ArrayList<>();
        for (int i = 1; i <= numJugadores; i++) {
            String nombre = view.pedirNombreJugador(i);
            nombres.add(nombre);
        }

        juego = new Juego(nombres);

        view.mostrarInicioJuego();

        while (!juego.isJuegoTerminado()) {
            view.esperarEnter();
            String resultadoTurno = juego.jugarTurno();
            view.mostrarMensaje(resultadoTurno);
            view.mostrarSeparador();
        }

        if (juego.getGanador() != null) {
            view.mostrarGanador(juego.getGanador().getNombre());
        }
    }
}