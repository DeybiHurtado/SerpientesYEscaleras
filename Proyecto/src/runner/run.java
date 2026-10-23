package runner;

import model.Juego;
import java.util.ArrayList;
import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== BIENVENIDO A SERPIENTES Y ESCALERAS ===");
        
        int numJugadores = 0;
        while (numJugadores < 2 || numJugadores > 4) {
            System.out.print("Ingrese el número de jugadores (de 2 a 4): ");
            if (scanner.hasNextInt()) {
                numJugadores = scanner.nextInt();
            }
            scanner.nextLine(); // limpiar buffer
        }

        ArrayList<String> nombres = new ArrayList<>();
        for (int i = 1; i <= numJugadores; i++) {
            System.out.print("Ingrese el nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            nombres.add(nombre);
        }

        Juego juego = new Juego(nombres);

        System.out.println("\n¡Comienza el juego! Presiona ENTER para tirar el dado en cada turno.");
        while (!juego.isJuegoTerminado()) {
            scanner.nextLine(); // Espera el ENTER del usuario
            String resultadoTurno = juego.jugarTurno();
            System.out.println(resultadoTurno);
            System.out.println("--------------------------------------------------");
        }

        scanner.close();
    }
}