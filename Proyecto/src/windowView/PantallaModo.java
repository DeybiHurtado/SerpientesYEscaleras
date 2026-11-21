package windowView;

import java.awt.Graphics2D;


public class PantallaModo extends Pantalla {

    private final VentanaGrafica ventana;
    private final BotonImagen botonContinuar;
    private final BotonImagen botonIndividual;

    public PantallaModo(VentanaGrafica ventana) {
        super(1000, 900, "fondos/ajustes.png");
        this.ventana = ventana;

        botonIndividual = this.agregar(this.crearBoton("INDIVIDUAL"), 250, 300, 500, 110);
        BotonImagen botonMultijugador = this.agregar(this.crearBoton("MULTIJUGADOR"), 250, 470, 500, 110);
        this.botonContinuar = this.agregar(this.crearBoton("CONTINUAR PARTIDA"), 250, 660, 500, 90);
        BotonImagen botonVolver = this.agregar(this.crearBoton("VOLVER"), 40, 790, 250, 70);
        botonIndividual.setSeleccionado(true);

        botonIndividual.alHacerClic(evento -> this.ventana.abrirSuperposicion("individual"));
        botonMultijugador.alHacerClic(evento -> this.ventana.abrirSuperposicion("jugadores"));
        this.botonContinuar.alHacerClic(evento -> this.ventana.continuarPartida());
        botonVolver.alHacerClic(evento -> this.ventana.cerrarSuperposicion());
        this.actualizarDisponibilidad();
    }

    public void actualizarDisponibilidad() {
       boolean hayPartida = this.ventana.getControlador().hayPartidaGuardada();
        this.botonContinuar.setVisible(hayPartida);
        this.botonContinuar.setSeleccionado(hayPartida);
        this.botonIndividual.setSeleccionado(!hayPartida);
        
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
