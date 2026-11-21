package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import repository.EscalerasRepository;
import repository.SerpientesRepository;

public class Tablero {
    private List<Serpiente> serpientes;
    private List<Escalera> escaleras;
    private final int casillaFinal = 100;

    public Tablero() {
    SerpientesRepository serpientesRepo = new SerpientesRepository();
    EscalerasRepository escalerasRepo = new EscalerasRepository();

    this.serpientes = new ArrayList<>();
    this.escaleras = new ArrayList<>();

    if (serpientesRepo.hayDatos() && escalerasRepo.hayDatos()) {
        for (Serpiente serpiente : serpientesRepo.getSerpientes()) {
            this.serpientes.add(new Serpiente(serpiente.getCabeza(), serpiente.getCola(), poderAleatorio()));
        }
        for (Escalera escalera : escalerasRepo.getEscaleras()) {
            this.escaleras.add(new Escalera(escalera.getBase(), escalera.getCima(), poderAleatorio()));
        }
    } else {
        inicializarTablero();
        serpientesRepo.guardarSerpientes(this.serpientes);
        escalerasRepo.guardarEscaleras(this.escaleras);
      }
    
    }

    public Tablero(List<Serpiente> serpientes, List<Escalera> escaleras){

        this.serpientes = new ArrayList<>(serpientes);
        this.escaleras = new ArrayList<>(escaleras);
    }

    private void inicializarTablero() {
        escaleras.add(new Escalera(44,83, poderAleatorio()));
        escaleras.add(new Escalera(3, 24, poderAleatorio()));
        escaleras.add(new Escalera(14, 48, poderAleatorio()));
        escaleras.add(new Escalera(72, 89, poderAleatorio()));

        serpientes.add(new Serpiente(58,22, poderAleatorio()));
        serpientes.add(new Serpiente(94, 55, poderAleatorio()));
        serpientes.add(new Serpiente(26, 7, poderAleatorio()));
        serpientes.add(new Serpiente(52, 32, poderAleatorio()));
    }

    private static int poderAleatorio() {
        return ThreadLocalRandom.current().nextInt(1, 5);
    }

    public String evaluarCasillaConPoderes(int posicionActual, Jugador jugador, Dados dados) {
        for (Escalera escalera : escaleras) {
            if (escalera.getBase() == posicionActual) {
                return escalera.getPoder(jugador, dados);
            }
        }
        
        for (Serpiente serpiente : serpientes) {
            if (serpiente.getCabeza() == posicionActual) {
                return serpiente.getPoder(jugador, dados);
            }
        }
        
        return "";
    }

    public boolean esEscalera(int casilla) {
        for (Escalera escalera : this.escaleras) {
            if (escalera.getBase() == casilla) {
                return true;
            }
        }
        return false;
    }

    public String getNombrePoder(int casilla) {
        for (Escalera escalera : this.escaleras) {
            if (escalera.getBase() == casilla) {
                return escalera.getNombrePoder();
            }
        }
        for (Serpiente serpiente : this.serpientes) {
            if (serpiente.getCabeza() == casilla) {
                return serpiente.getNombrePoder();
            }
        }
        throw new IllegalArgumentException("La casilla no tiene un poder: " + casilla);
    }

    public int getCasillaFinal() {
        return casillaFinal;
    }

    public List<Serpiente> getSerpientes(){
        return serpientes;
    }

    public List<Escalera> getEscaleras(){
        return escaleras;
    }
}