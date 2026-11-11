package windowView;

import java.awt.Graphics2D;

public class PantallaIndividual extends Pantalla {

    private final VentanaGrafica ventana;
    private final CampoTexto campoNombre;
    private final BotonImagen[] botonesRivales = new BotonImagen[3];
    private int cantidadRivales = 1;

    public PantallaIndividual(VentanaGrafica ventana) {
        super(1000, 900, "fondos/ajustes.png");
        this.ventana = ventana;

        this.campoNombre = this.agregar(new CampoTexto("Escribe tu nombre"), 290, 348, 420, 50);

        int[] posicionesX = { 150, 400, 650 };
        for (int i = 0; i < 3; i++) {
            final int rivales = i + 1;
            this.botonesRivales[i] = this.agregar(this.crearBoton(String.valueOf(rivales)), posicionesX[i], 560, 200, 91);
            this.botonesRivales[i].alHacerClic(evento -> this.elegirRivales(rivales));
        }
        this.elegirRivales(1);

        BotonImagen botonVolver = this.agregar(this.crearBoton("VOLVER"), 40, 790, 250, 70);
        BotonImagen botonComenzar = this.agregar(this.crearBoton("COMENZAR"), 710, 790, 250, 70);
        botonVolver.setSeleccionado(true);
        botonVolver.alHacerClic(evento -> this.ventana.abrirSuperposicion("modo"));
        botonComenzar.alHacerClic(evento -> this.comenzar());
    }

    private BotonImagen crearBoton(String texto) {
        return new BotonImagen(texto, "botones/normal.png", "botones/resaltado.png", 16);
    }

    private void elegirRivales(int rivales) {
        this.cantidadRivales = rivales;
        for (int i = 0; i < this.botonesRivales.length; i++) {
            this.botonesRivales[i].setSeleccionado(i + 1 == rivales);
        }
    }

    private void comenzar() {
        String nombre = this.campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            nombre = "Jugador 1";
        }
        int total = 1 + this.cantidadRivales;
        String[] nombres = new String[total];
        boolean[] sonComputadora = new boolean[total];
        nombres[0] = nombre;
        for (int i = 1; i < total; i++) {
            nombres[i] = "BOT" + i;
            sonComputadora[i] = true;
        }
        this.ventana.prepararPartida(new DatosPartida(nombres, sonComputadora));
    }

    @Override
    protected void dibujar(Graphics2D g) {
        this.textoCentrado(g, "MODO INDIVIDUAL", 500, 130, 60, Recursos.CREMA, 900);
        Recursos.dibujarEstirada(g, Recursos.imagen("botones/normal.png"), 250, 250, 500, 170, 16, 16);
        this.textoCentrado(g, "Tu nombre", 500, 300, 40, PanelJugador.COLORES_DE_TEXTO[0], 400);
        this.textoCentrado(g, "RIVALES (COMPUTADORA):", 500, 510, 34, Recursos.CREMA, 800);
    }
}
