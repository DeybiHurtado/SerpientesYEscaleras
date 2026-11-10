package windowView;

import java.awt.Graphics2D;

public class PantallaInicio extends Pantalla {

    private final VentanaGrafica ventana;

    public PantallaInicio(VentanaGrafica ventana) {
        super(1440, 900, "fondos/inicio.png");
        this.ventana = ventana;

        BotonImagen botonJugar = this.agregar(this.crearBoton("JUGAR"), 572, 503, 378, 99);
        BotonImagen botonAjustes = this.agregar(this.crearBoton("AJUSTES"), 572, 612, 378, 99);
        BotonImagen botonSalir = this.agregar(this.crearBoton("SALIR"), 572, 721, 378, 99);
        BotonRedondo botonInfo = this.agregar(new BotonRedondo(BotonRedondo.Tipo.INFO, "Reglas de juego"), 37, 820, 50, 50);

        botonJugar.setSeleccionado(true);

        botonJugar.alHacerClic(evento -> this.ventana.abrirSuperposicion("modo"));
        botonAjustes.alHacerClic(evento -> this.ventana.abrirSuperposicion("ajustes"));
        botonSalir.alHacerClic(evento -> this.ventana.abrirSuperposicion("salir"));
        botonInfo.alHacerClic(evento -> this.ventana.abrirSuperposicion("reglas"));
    }

    private BotonImagen crearBoton(String texto) {
        return new BotonImagen(texto, "botones/hexagono_normal.png", "botones/hexagono_resaltado.png", 0);
    }

    @Override
    protected void dibujar(Graphics2D g) {
        g.drawImage(Recursos.imagen("fondos/titulo.png"), 268, 120, null);
    }
}

