package model;

public class EstadisticaJugador {
    private String nombre;
    private int partidasJugadas;
    private int partidasGanadas;
    private int casillaMasAlta;

    public EstadisticaJugador(String nombre) {
        this.nombre = nombre;
        this.partidasJugadas = 0;
        this.partidasGanadas = 0;
        this.casillaMasAlta = 1;
    }

    public void registrarPartida(boolean gano, int casillaAlcanzada) {
        this.partidasJugadas++;
        if (gano) {
            this.partidasGanadas++;
        }
        if (casillaAlcanzada > this.casillaMasAlta) {
            this.casillaMasAlta = casillaAlcanzada;
        }
    }

    public String getNombre() { return nombre; }
    public int getPartidasJugadas() { return partidasJugadas; }
    public int getPartidasGanadas() { return partidasGanadas; }
    public int getCasillaMasAlta() { return casillaMasAlta; }
}
