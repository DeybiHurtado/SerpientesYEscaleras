package windowView;

public final class Casillas {

    public static final int TOTAL = 100;
    public static final int TABLERO_X = 310;
    public static final int TABLERO_Y = 39;
    public static final double ANCHO_CASILLA = 81.7;
    public static final double ALTO_CASILLA = 81.8;

    private Casillas() {
    }

    public static int numero(int fila, int columna) {
        int filaDesdeAbajo = 9 - fila;
        int posicionEnFila = columna;
        if (filaDesdeAbajo % 2 == 1) {
            posicionEnFila = 9 - columna;
        }
        return filaDesdeAbajo * 10 + posicionEnFila + 1;
    }

    public static double[] centro(int casilla) {
        int indice = Math.max(1, Math.min(TOTAL, casilla)) - 1;
        int filaDesdeAbajo = indice / 10;
        int posicionEnFila = indice % 10;
        int columna = posicionEnFila;
        if (filaDesdeAbajo % 2 == 1) {
            columna = 9 - posicionEnFila;
        }
        double x = TABLERO_X + (columna + 0.5) * ANCHO_CASILLA;
        double y = TABLERO_Y + 818 - (filaDesdeAbajo + 0.5) * ALTO_CASILLA;
        return new double[] { x, y };
    }

}