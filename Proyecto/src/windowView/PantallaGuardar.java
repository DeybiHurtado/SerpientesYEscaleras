package windowView;

import java.awt.Graphics2D;


public class PantallaGuardar extends Pantalla {

    private final VentanaGrafica ventana;

    public PantallaGuardar(VentanaGrafica ventana) {
        super(1000, 900, "fondos/ajustes.png");
        this.ventana = ventana;

        BotonImagen botonGuardar = this.agregar(this.crearBoton("GUARDAR Y SALIR"), 250, 360, 500, 100);
        BotonImagen botonSalirSinGuardar = this.agregar(this.crearBoton("SALIR SIN GUARDAR"), 250, 490, 500, 100);
        BotonImagen botonCancelar = this.agregar(this.crearBoton("CANCELAR"), 250, 620, 500, 100);
        botonGuardar.setSeleccionado(true);

        botonGuardar.alHacerClic(evento -> {
            this.ventana.guardarPartida();
            this.ventana.volverAlMenu();
        });
        botonSalirSinGuardar.alHacerClic(evento -> this.ventana.volverAlMenu());
        botonCancelar.alHacerClic(evento -> this.ventana.cerrarSuperposicion());
    }

    private BotonImagen crearBoton(String texto) {
        return new BotonImagen(texto, "botones/normal.png", "botones/resaltado.png", 16);
    }

    @Override
    protected void dibujar(Graphics2D g) {
        this.textoCentrado(g, "¿DESEAS GUARDAR", 500, 150, 60, Recursos.CREMA, 900);
        this.textoCentrado(g, "LA PARTIDA?", 500, 225, 60, Recursos.CREMA, 900);
        this.textoCentrado(g, "Volverás al menú principal", 500, 300, 26, Recursos.GRIS_AZUL, 800);
    }
}

