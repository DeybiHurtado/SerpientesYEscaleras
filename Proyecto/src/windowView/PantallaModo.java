package windowView;

import java.awt.Graphics2D;


public class PantallaModo extends Pantalla {

    private final VentanaGrafica ventana;

    public PantallaModo(VentanaGrafica ventana) {
        super(1000, 900, "fondos/ajustes.png");
        this.ventana = ventana;

        BotonImagen botonIndividual = this.agregar(this.crearBoton("INDIVIDUAL"), 250, 300, 500, 110);
        BotonImagen botonMultijugador = this.agregar(this.crearBoton("MULTIJUGADOR"), 250, 470, 500, 110);
        BotonImagen botonVolver = this.agregar(this.crearBoton("VOLVER"), 40, 790, 250, 70);
        botonVolver.setSeleccionado(true);

        botonIndividual.alHacerClic(evento -> this.ventana.abrirSuperposicion("individual"));
        botonMultijugador.alHacerClic(evento -> this.ventana.abrirSuperposicion("jugadores"));
        botonVolver.alHacerClic(evento -> this.ventana.cerrarSuperposicion());
    }

    private BotonImagen crearBoton(String texto) {
        return new BotonImagen(texto, "botones/normal.png", "botones/resaltado.png", 16);
    }

    @Override
    protected void dibujar(Graphics2D g) {
        this.textoCentrado(g, "MODO DE JUEGO", 500, 150, 62, Recursos.CREMA, 900);
        this.textoCentrado(g, "Juegas contra la computadora", 500, 440, 26, Recursos.CREMA, 800);
        this.textoCentrado(g, "Juegas con amigos (de 2 a 4 jugadores)", 500, 610, 26, Recursos.CREMA, 800);
    }
}
