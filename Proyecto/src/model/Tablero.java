package model;

import java.util.ArrayList;
import java.util.List;

public class Tablero {
    private List<Serpiente> serpientes;
    private List<Escalera> escaleras;
    private final int casillaFinal = 100;

    public Tablero() {
        this.serpientes = new ArrayList<>();
        this.escaleras = new ArrayList<>();
        inicializarTablero();
    }

    private void inicializarTablero() {
        escaleras.add(new Escalera(4, 14, (int) (Math.random() * 4) + 1));
        escaleras.add(new Escalera(9, 31, (int) (Math.random() * 4) + 1));
        escaleras.add(new Escalera(20, 38, (int) (Math.random() * 4) + 1));
        escaleras.add(new Escalera(28, 84, (int) (Math.random() * 4) + 1));

        serpientes.add(new Serpiente(17, 7, (int) (Math.random() * 4) + 1));
        serpientes.add(new Serpiente(54, 34, (int) (Math.random() * 4) + 1));
        serpientes.add(new Serpiente(62, 19, (int) (Math.random() * 4) + 1));
        serpientes.add(new Serpiente(98, 79, (int) (Math.random() * 4) + 1));
    }

    public int evaluarCasilla(int posicionActual) {
        // Verificar si cayó en una escalera
        for (Escalera escalera : escaleras) {
            if (escalera.getBase() == posicionActual) {
                System.out.println("¡Has subido por una escalera!");
                return escalera.getCima();
            }
        }
        
        // Verificar si cayó en una serpiente
        for (Serpiente serpiente : serpientes) {
            if (serpiente.getCabeza() == posicionActual) {
                System.out.println("¡Oh no! Una serpiente te ha mordido.");
                return serpiente.getCola();
            }
        }
        
        return posicionActual; // Si no hay nada, se queda en la misma
    }

    public int getCasillaFinal() {
        return casillaFinal;
    }
}