package windowView;

import java.awt.Graphics2D;


public class PantallaAjustes extends Pantalla {

    private static final String[] NOMBRES_DE_TEMAS = { "default", "marino", "desierto" };

    private final VentanaGrafica ventana;
    private final BotonImagen[] miniaturasDeTableros = new BotonImagen[3];
    private final BotonImagen botonMusica;
    private final Interruptor interruptorGuardado;

    public PantallaAjustes(VentanaGrafica ventana) {
        super(1000, 900, "fondos/ajustes.png");
        this.ventana = ventana;

        
        int[][] medidas = { { 157, 255, 213, 214 }, { 388, 255, 218, 218 }, { 620, 255, 219, 219 } };
        for (int i = 0; i < 3; i++) {
            final int tema = i;
            String rutaOff = "tableros/mini_" + NOMBRES_DE_TEMAS[i] + "_off.png";
            String rutaOn = "tableros/mini_" + NOMBRES_DE_TEMAS[i] + "_on.png";
            this.miniaturasDeTableros[i] = this.agregar(new BotonImagen("", rutaOff, rutaOn, 0), medidas[i][0],
                    medidas[i][1], medidas[i][2], medidas[i][3]);
            this.miniaturasDeTableros[i].alHacerClic(evento -> this.elegirTema(tema));
        }
        this.marcarTema(0);

        
        this.botonMusica = this.agregar(new BotonImagen("", "botones/normal.png", "botones/resaltado.png", 16), 560, 535, 330, 84);
        this.actualizarTextoMusica();
        this.botonMusica.alHacerClic(evento -> this.cambiarMusica());

        
        this.interruptorGuardado = this.agregar(new Interruptor(false), 700, 665, 150, 66);
        this.interruptorGuardado.setAlCambiar(encendido -> this.ventana.setGuardadoAutomatico(encendido));

        BotonImagen botonVolver = this.agregar(new BotonImagen("VOLVER", "botones/normal.png", "botones/resaltado.png", 16),
                40, 790, 250, 70);
        botonVolver.setSeleccionado(true);
        botonVolver.alHacerClic(evento -> this.ventana.cerrarSuperposicion());
    }

    
    @Override
    public void alMostrar() {
        this.interruptorGuardado.cambiar(this.ventana.isGuardadoAutomatico(), false);
        this.actualizarTextoMusica();
    }

    private void elegirTema(int tema) {
        this.marcarTema(tema);
        this.ventana.setTema(tema);
    }

    private void marcarTema(int tema) {
        for (int i = 0; i < this.miniaturasDeTableros.length; i++) {
            this.miniaturasDeTableros[i].setSeleccionado(i == tema);
        }
    }

    private void cambiarMusica() {
        Musica.setActivada(!Musica.isActivada());
        this.actualizarTextoMusica();
    }

    private void actualizarTextoMusica() {
        if (Musica.isActivada()) {
            this.botonMusica.setTexto("ACTIVADA");
        } else {
            this.botonMusica.setTexto("APAGADA");
        }
        this.botonMusica.setSeleccionado(Musica.isActivada());
    }

    @Override
    protected void dibujar(Graphics2D g) {
        this.textoCentrado(g, "AJUSTES", 500, 120, 70, Recursos.CREMA, 800);
        this.textoCentrado(g, "TABLEROS:", 500, 205, 36, Recursos.CREMA, 800);
        this.textoIzquierda(g, "MÚSICA", 110, 577, 36, Recursos.CREMA);
        this.textoIzquierda(g, "GUARDADO AUTOMÁTICO", 110, 698, 34, Recursos.CREMA);
    }
}
