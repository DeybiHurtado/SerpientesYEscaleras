package windowView;

import java.awt.Graphics2D;

public class PantallaGanador extends Pantalla {

    private final VentanaGrafica ventana;
    private String nombreGanador = "JUGADOR n";

    public PantallaGanador(VentanaGrafica ventana) {
        super(1200, 900, "fondos/reglas.png");
        this.ventana = ventana;

        BotonImagen botonMenu = this.agregar(this.crearBoton("MENÚ PRINCIPAL"), 394, 625, 412, 91);
        BotonImagen botonOtraVez = this.agregar(this.crearBoton("VOLVER A JUGAR"), 394, 735, 412, 91);
        botonOtraVez.setSeleccionado(true);

        botonMenu.alHacerClic(evento -> this.ventana.volverAlMenu());
        botonOtraVez.alHacerClic(evento -> this.ventana.reiniciarPartida());
    }

    private BotonImagen crearBoton(String texto) {
        return new BotonImagen(texto, "botones/normal.png", "botones/resaltado.png", 16);
    }

    public void setNombreGanador(String nombreGanador) {
        this.nombreGanador = nombreGanador;
        this.repaint();
    }

    @Override
    protected void dibujar(Graphics2D g) {
        this.textoCentrado(g, "GANADOR", 600, 230, 170, Recursos.CREMA, 1000);
        this.textoCentrado(g, "HAS LLEGADO A LA META", 600, 390, 40, Recursos.CREMA, 900);
        Recursos.dibujarEstirada(g, Recursos.imagen("botones/normal.png"), 344, 440, 512, 110, 16, 16);
        this.textoCentrado(g, this.nombreGanador, 600, 495, 48, Recursos.CREMA, 460);
    }
}
