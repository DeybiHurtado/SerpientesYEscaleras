package model;

public class ResultadoJugador {
    private String nombre;
    private int jugadas;
    private int casillaFinal;

    public ResultadoJugador(String nombre, int jugadas, int casillaFinal) {
        this.nombre = nombre;
        this.jugadas = jugadas;
        this.casillaFinal = casillaFinal;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getJugadas() {
        return this.jugadas;
    }

    public int getCasillaFinal() {
        return this.casillaFinal;
    }
}
