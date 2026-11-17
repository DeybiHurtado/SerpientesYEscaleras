package windowView;

public class PantallaEstadisticas extends Pantalla{
    private final VentanaGrafica ventana;

    protected PantallaEstadisticas(VentanaGrafica ventana) {

        super(1440, 900, "fondos/estadisticas.png");
        this.ventana = ventana;
       
        BotonImagen botonVolver = this.agregar(this.crearBoton("VOLVER"), 40, 790, 250, 70);
        botonVolver.alHacerClic(evento -> this.ventana.mostrar("inicio"));
    }

     private BotonImagen crearBoton(String texto) {
        return new BotonImagen(texto, "botones/normal.png", "botones/resaltado.png", 16);
    }
 

}
