package windowView;

public class DatosPartida {

    private final String[] nombres;
    private final boolean[] sonComputadora;

    public DatosPartida(String[] nombres, boolean[] sonComputadora) {
        if (nombres == null || sonComputadora == null || nombres.length != sonComputadora.length) {
            throw new IllegalArgumentException("Los nombres y tipos de jugador deben tener el mismo tamaño.");
        }
        this.nombres = nombres.clone();
        this.sonComputadora = sonComputadora.clone();
    }

    public int getCantidad() {
        return this.nombres.length;
    }

    public String getNombre(int indice) {
        return this.nombres[indice];
    }

    public boolean esComputadora(int indice) {
        return this.sonComputadora[indice];
    }

}