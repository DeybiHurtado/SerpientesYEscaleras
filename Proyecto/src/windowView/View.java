package windowView;

import java.util.Scanner;

public class View {
    private Scanner scanner;

    public View() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarBienvenida() {
        System.out.println("=== BIENVENIDO A SERPIENTES Y ESCALERAS ===");
    }

    public int pedirNumeroJugadores() {
        int numJugadores = 0;
        while (numJugadores < 2 || numJugadores > 4) {
            System.out.print("Ingrese el número de jugadores (de 2 a 4): ");
            if (scanner.hasNextInt()) {
                numJugadores = scanner.nextInt();
            }
            scanner.nextLine();
        }
        return numJugadores;
    }

    public String pedirNombreJugador(int numero) {
        System.out.print("Ingrese el nombre del jugador " + numero + ": ");
        return scanner.nextLine();
    }

    public void mostrarInicioJuego() {
        System.out.println("\n¡Comienza el juego! Presiona ENTER para tirar el dado en cada turno.");
    }

    public void esperarEnter() {
        scanner.nextLine();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarSeparador() {
        System.out.println("--------------------------------------------------");
    }

    public void mostrarGanador(String nombreGanador) {
        System.out.println("\n¡FIN DEL JUEGO! El ganador es: " + nombreGanador);
        scanner.close();
    }
}