package model;

import java.util.ArrayList;
import java.util.List;
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
        this.serpientes.addAll(serpientesRepo.getSerpientes());
        this.escaleras.addAll(escalerasRepo.getEscaleras());
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
        escaleras.add(new Escalera(4, 14, (int) (Math.random() * 4) + 1));
        escaleras.add(new Escalera(9, 31, (int) (Math.random() * 4) + 1));
        escaleras.add(new Escalera(20, 38, (int) (Math.random() * 4) + 1));
        escaleras.add(new Escalera(28, 84, (int) (Math.random() * 4) + 1));

        serpientes.add(new Serpiente(17, 7, (int) (Math.random() * 4) + 1));
        serpientes.add(new Serpiente(54, 34, (int) (Math.random() * 4) + 1));
        serpientes.add(new Serpiente(62, 19, (int) (Math.random() * 4) + 1));
        serpientes.add(new Serpiente(98, 79, (int) (Math.random() * 4) + 1));
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