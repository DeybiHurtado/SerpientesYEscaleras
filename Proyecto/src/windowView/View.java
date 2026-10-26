package windowView;


import java.util.List;
import java.util.Scanner;

import model.Escalera;
import model.EstadisticaJugador;
import model.Jugador;
import model.Partida;
import model.PartidaGuardada;
import model.Serpiente;

public class View {
    private Scanner scanner;

    public View() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarBienvenida() {
        System.out.println("=== BIENVENIDO A SERPIENTES Y ESCALERAS ===");
    }

public int pedirNumeroJugadores() {
    return leerEntero("Ingrese el número de jugadores (de 2 a 4): ", 2, 4);
}
    public String pedirNombreJugador(int numero) {
        System.out.print("Ingrese el nombre del jugador " + numero + ": ");
        return scanner.nextLine().trim();
    }

    public void mostrarInicioJuego() {
        System.out.println("\n¡Comienza el juego!");
        System.out.println("En tu turno: ENTER = tirar el dado | G = guardar partida | M = volver al menú");
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
}

    private int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Opción no válida. Escribe un número entre " + min + " y " + max + ".");
        }
    }

    public boolean confirmar(String pregunta) {
        while (true) {
            System.out.print(pregunta + " (S/N): ");
            String r = scanner.nextLine().trim();
            if (r.equalsIgnoreCase("S")) return true;
            if (r.equalsIgnoreCase("N")) return false;
        }
    }

    public String pedirNombreUnico() {
        System.out.print("Ingrese tu nombre: ");
        return scanner.nextLine().trim();
    }


    public int mostrarMenuPrincipal(boolean hayPartidaGuardada) {
        System.out.println();
        System.out.println("========== MENÚ PRINCIPAL ==========");
        System.out.println("1. Iniciar juego (tú contra la consola)");
        System.out.println("2. Multijugador (2 a 4 jugadores)");
        System.out.println("3. Continuar partida guardada" + (hayPartidaGuardada ? "" : " (no hay ninguna)"));
        System.out.println("4. Ver tablero");
        System.out.println("5. Estadísticas");
        System.out.println("6. Info");
        System.out.println("7. Salir");
        System.out.println("====================================");
        return leerEntero("Elige una opción: ", 1, 7);
    }

    public void mostrarDespedida() {
        System.out.println("¡Hasta pronto!");
    }


    public String pedirAccionTurno(String nombre) {
        while (true) {
            System.out.print("[" + nombre + "] (ENTER / G / M): ");
            String r = scanner.nextLine().trim();
            if (r.isEmpty()) {
                return "";
            }
            if (r.equalsIgnoreCase("G") || r.equalsIgnoreCase("M")) {
                return r.toUpperCase();
            }
        }
    }

    public void mostrarPartidaGuardada(PartidaGuardada p) {
        System.out.println("Partida guardada el " + p.getFechaGuardado() + " | turnos jugados: " + p.getTurnos());
        for (Jugador j : p.getJugadores()) {
            System.out.println("  - " + j.getNombre() + " en la casilla " + j.getPosicion());
        }
    }


    public void mostrarTablero(List<Serpiente> serpientes, List<Escalera> escaleras, int casillaFinal) {
        System.out.println("\n=========== TABLERO ===========");
        System.out.println("Escaleras:");
        for (Escalera e : escaleras) {
            System.out.println("  Sube de " + e.getBase() + " a " + e.getCima()
                    + "  [" + e.getNombrePoder() + "]" + (e.isUsada() ? " (ya usada)" : ""));
        }
        System.out.println("Serpientes:");
        for (Serpiente s : serpientes) {
            System.out.println("  Baja de " + s.getCabeza() + " a " + s.getCola()
                    + "  [" + s.getNombrePoder() + "]");
        }

        System.out.println("\nMapa (E = base de escalera, e = cima, S = cabeza de serpiente, s = cola):\n");
        int columnas = 10;
        int filas = casillaFinal / columnas;
        for (int fila = filas - 1; fila >= 0; fila--) {
            StringBuilder linea = new StringBuilder();
            for (int col = 0; col < columnas; col++) {
                int numero;
                if (fila % 2 == 0) {
                    numero = fila * columnas + col + 1;
                } else {
                    numero = fila * columnas + (columnas - col);
                }
                linea.append(String.format("%4d%c", numero, marcaCasilla(numero, serpientes, escaleras)));
            }
            System.out.println(linea);
        }
    }

    private char marcaCasilla(int n, List<Serpiente> serpientes, List<Escalera> escaleras) {
        for (Escalera e : escaleras) {
            if (e.getBase() == n) return 'E';
            if (e.getCima() == n) return 'e';
        }
        for (Serpiente s : serpientes) {
            if (s.getCabeza() == n) return 'S';
            if (s.getCola() == n) return 's';
        }
        return ' ';
    }


    public void mostrarEstadisticas(List<EstadisticaJugador> estadisticas, List<Partida> partidas) {
        System.out.println("\n=========== ESTADÍSTICAS ===========");
        if (estadisticas.isEmpty()) {
            System.out.println("Todavía no hay partidas terminadas.");
            return;
        }
        System.out.println(String.format("%-15s %8s %8s %14s", "Jugador", "Jugadas", "Ganadas", "Casilla máx."));
        for (EstadisticaJugador e : estadisticas) {
            System.out.println(String.format("%-15s %8d %8d %14d",
                    e.getNombre(), e.getPartidasJugadas(), e.getPartidasGanadas(), e.getCasillaMasAlta()));
        }

        System.out.println("\nÚltimas partidas (máximo 10):");
        int desde = Math.max(0, partidas.size() - 10);
        for (int i = partidas.size() - 1; i >= desde; i--) {
            Partida p = partidas.get(i);
            System.out.println("  " + p.getFecha() + " | " + String.join(", ", p.getParticipantes())
                    + " | ganó: " + p.getGanador() + " | turnos: " + p.getTurnos());
        }
    }

    public void mostrarInfo() {
        System.out.println("\n=========== INFO ===========");
        System.out.println("OBJETIVO: llegar primero a la casilla 100.");
        System.out.println("Cada turno se lanzan dos dados y avanzas la suma.");
        System.out.println("Si caes en la base de una escalera subes; si caes en la cabeza de una serpiente bajas.");
        System.out.println("\nPODERES DE LAS ESCALERAS");
        System.out.println("  Normal        : subes a la cima.");
        System.out.println("  Un solo uso   : subes, pero la escalera desaparece y nadie más podrá usarla.");
        System.out.println("  Doble turno   : subes y tiras otra vez de inmediato.");
        System.out.println("  Doble o nada  : tiras un dado. Par: subes el doble de la distancia. Impar: te quedas en la base.");
        System.out.println("\nPODERES DE LAS SERPIENTES");
        System.out.println("  Normal        : bajas hasta la cola.");
        System.out.println("  Quita turno   : bajas y pierdes tu siguiente turno.");
        System.out.println("  Veneno        : bajas; en tus próximos 2 turnos avanzas la mitad y luego 3/4.");
        System.out.println("  Doble o nada  : tiras un dado. Par: te salvas. Impar: bajas el doble de la distancia.");
        System.out.println("\nDurante la partida puedes escribir G para guardarla y M para volver al menú.");
    }
}