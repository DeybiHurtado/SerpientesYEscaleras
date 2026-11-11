package windowView;

import java.awt.Graphics2D;

public class PantallaJugadores extends Pantalla {

    private static final int[][] CAJAS = { { 74, 256, 410, 170 }, { 520, 256, 410, 170 }, { 74, 462, 410, 170 },
            { 520, 462, 410, 170 } };

    private final VentanaGrafica ventana;
    private final boolean[] jugadorActivo = { true, true, false, false };
    private final CampoTexto[] camposDeNombre = new CampoTexto[4];
    private final BotonImagen[] botonesAnadir = new BotonImagen[4];
    private final BotonImagen[] botonesQuitar = new BotonImagen[4];

    public PantallaJugadores(VentanaGrafica ventana) {
        super(1000, 900, "fondos/ajustes.png");
        this.ventana = ventana;

        for (int i = 0; i < 4; i++) {
            final int indice = i;
            int x = CAJAS[i][0];
            int y = CAJAS[i][1];
            this.camposDeNombre[i] = this.agregar(new CampoTexto("Escribe tu nombre"), x + 40, y + 98, 330, 50);

            this.botonesAnadir[i] = this.agregar(this.crearBoton("AÑADIR JUGADOR"), x, y, 410, 170);
            this.botonesAnadir[i].setSimbolo("+");
            this.botonesAnadir[i].alHacerClic(evento -> this.cambiarJugador(indice, true));

            this.botonesQuitar[i] = this.agregar(this.crearBoton("X"), x + 350, y + 14, 46, 46);
            this.botonesQuitar[i].alHacerClic(evento -> this.cambiarJugador(indice, false));
        }
        this.actualizarVisibilidad();

        BotonImagen botonVolver = this.agregar(this.crearBoton("VOLVER"), 40, 790, 250, 70);
        BotonImagen botonComenzar = this.agregar(this.crearBoton("COMENZAR"), 710, 790, 250, 70);
        botonVolver.setSeleccionado(true);
        botonVolver.alHacerClic(evento -> this.ventana.abrirSuperposicion("modo"));
        botonComenzar.alHacerClic(evento -> this.comenzar());
    }

    private BotonImagen crearBoton(String texto) {
        return new BotonImagen(texto, "botones/normal.png", "botones/resaltado.png", 16);
    }

    private void cambiarJugador(int indice, boolean activo) {
        this.jugadorActivo[indice] = activo;
        this.actualizarVisibilidad();
    }

    private void actualizarVisibilidad() {
        for (int i = 0; i < 4; i++) {
            this.camposDeNombre[i].setVisible(this.jugadorActivo[i]);
            this.botonesAnadir[i].setVisible(!this.jugadorActivo[i]);
            this.botonesQuitar[i].setVisible(this.jugadorActivo[i] && i >= 2);
        }
        this.repaint();
    }

    private void comenzar() {
        int total = 0;
        for (boolean activo : this.jugadorActivo) {
            if (activo) {
                total++;
            }
        }
        String[] nombres = new String[total];
        boolean[] sonComputadora = new boolean[total];
        int posicion = 0;
        for (int i = 0; i < 4; i++) {
            if (this.jugadorActivo[i]) {
                String nombre = this.camposDeNombre[i].getText().trim();
                if (nombre.isEmpty()) {
                    nombre = "Jugador " + (i + 1);
                }
                nombres[posicion] = nombre;
                posicion++;
            }
        }
        this.ventana.prepararPartida(new DatosPartida(nombres, sonComputadora));
    }

    @Override
    protected void dibujar(Graphics2D g) {
        this.textoCentrado(g, "MODO MULTIJUGADOR", 500, 150, 56, Recursos.CREMA, 900);
        for (int i = 0; i < 4; i++) {
            if (this.jugadorActivo[i]) {
                int[] caja = CAJAS[i];
                Recursos.dibujarEstirada(g, Recursos.imagen("botones/normal.png"), caja[0], caja[1], caja[2], caja[3], 16, 16);
                this.textoCentrado(g, "Jugador " + (i + 1), caja[0] + caja[2] / 2, caja[1] + 52, 42,
                        PanelJugador.COLORES_DE_TEXTO[i], 300);
            }
        }
    }
}