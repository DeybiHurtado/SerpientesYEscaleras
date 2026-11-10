package windowView;

import java.util.concurrent.Semaphore;


public class HiloJugador extends Thread {

    private final PantallaTablero tablero;
    private final int indiceJugador;
    private final Semaphore semaforoDeTurno = new Semaphore(0); 
    private volatile boolean activo = true;

    public HiloJugador(PantallaTablero tablero, int indiceJugador, String nombre) {
        super("Hilo-" + nombre);
        this.tablero = tablero;
        this.indiceJugador = indiceJugador;
        this.setDaemon(true); 
    }


    public void darTurno() {
        this.semaforoDeTurno.release();
    }

    public void detener() {
        this.activo = false;
        this.interrupt(); 
    }

    @Override
    public void run() {
        try {
            while (this.activo) {
                this.semaforoDeTurno.acquire();
                if (!this.activo) {
                    break;
                }
                boolean sigueLaPartida = this.tablero.jugarTurno(this.indiceJugador);
                if (!sigueLaPartida || !this.activo) {
                    break;
                }
                this.tablero.pasarTurno(this.indiceJugador);
            }
        } catch (InterruptedException error) {
         
        }
    }
}
