package model;

import java.util.ArrayList;

public class ResumenHistorialPartidas {

    private final ArrayList<RegistroPartida> registros;
    private final boolean filtrado;
    private final int victorias;
    private final double promedioJugadas;
    private final int mejorResultado;
    private final int partidaMasLarga;

    public ResumenHistorialPartidas(ArrayList<RegistroPartida> todasLasPartidas, String nombreJugador,
            int cantidadMaxima) {
        String filtro = nombreJugador == null ? "" : nombreJugador.trim();
        this.filtrado = !filtro.isEmpty();
        this.registros = new ArrayList<>();

        for (RegistroPartida registro : todasLasPartidas) {
            if ((!this.filtrado || registro.buscarJugador(filtro) != null)
                    && this.registros.size() < cantidadMaxima) {
                this.registros.add(registro);
            }
        }

        int sumaJugadas = 0;
        int ganadas = 0;
        int mejor = 0;
        int masLarga = 0;
        for (RegistroPartida registro : this.registros) {
            if (this.filtrado) {
                ResultadoJugador jugador = registro.buscarJugador(filtro);
                sumaJugadas += jugador.getJugadas();
                if (registro.ganoElJugador(jugador)) {
                    ganadas++;
                    if (mejor == 0 || jugador.getJugadas() < mejor) {
                        mejor = jugador.getJugadas();
                    }
                }
            } else {
                int jugadasGanador = registro.getJugadasDelGanador();
                sumaJugadas += jugadasGanador;
                mejor = mejor == 0 ? jugadasGanador : Math.min(mejor, jugadasGanador);
                masLarga = Math.max(masLarga, jugadasGanador);
            }
        }

        this.victorias = ganadas;
        this.promedioJugadas = this.registros.isEmpty() ? 0 : (double) sumaJugadas / this.registros.size();
        this.mejorResultado = mejor;
        this.partidaMasLarga = masLarga;
    }

    public ArrayList<RegistroPartida> getRegistros() {
        return new ArrayList<>(this.registros);
    }

    public boolean isFiltrado() {
        return this.filtrado;
    }

    public int getCantidadPartidas() {
        return this.registros.size();
    }

    public int getVictorias() {
        return this.victorias;
    }

    public double getPromedioJugadas() {
        return this.promedioJugadas;
    }

    public int getMejorResultado() {
        return this.mejorResultado;
    }

    public int getPartidaMasLarga() {
        return this.partidaMasLarga;
    }
}
