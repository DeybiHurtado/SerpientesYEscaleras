package windowView;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class PantallaTablero extends Pantalla {

    private static final String[] TEMAS = { "default", "marino", "desierto" };

    private static final int[][] POSICION_DE_PANELES = { { 24, 44 }, { 1131, 715 }, { 23, 714 }, { 1134, 36 } };

    private static final int[][] DESPLAZAMIENTO_FICHAS = { { -6, -23 }, { 27, 6 }, { -6, 7 }, { 25, -23 } };

    private static final int[][] POSICION_ESCALERAS = { { 474, 181 }, { 485, 666 }, { 806, 493 }, { 970, 154 } };
    private static final int[][] POSICION_SERPIENTES = { { 406, 408 }, { 722, 92 }, { 723, 626 }, { 967, 417 } };

    private final VentanaGrafica ventana;
    private final Random azar = new Random();
    private final CasillaBoton[][] casillas = new CasillaBoton[10][10]; 
    private final PanelJugador[] paneles = new PanelJugador[4];
    private final boolean[] esComputadora = new boolean[4];
    private final VentanaAviso ventanaAviso = new VentanaAviso();
    private final Semaphore semaforoLanzamiento = new Semaphore(0);

    private String temaActual = TEMAS[0];
    private int cantidadJugadores = 0;
    private HiloJugador[] hilosJugadores = new HiloJugador[0];
    private Timer temporizadorPresentacion;
    private CasillaBoton casillaIluminada;
    private volatile int turnoActual = -1;
    private volatile boolean partidaPausada;

    public PantallaTablero(VentanaGrafica ventana) {
        super(1440, 900, "fondos/default.png");
        this.ventana = ventana;
        this.crearMatrizDeCasillas();
        this.crearPanelesDeJugadores();

        BotonRedondo botonCasa = this.agregar(new BotonRedondo(BotonRedondo.Tipo.CASA, "Guardar y salir"), 30, 454, 52, 52);
        BotonRedondo botonInfo = this.agregar(new BotonRedondo(BotonRedondo.Tipo.INFO, "Reglas de juego"), 1358, 454, 52, 52);
        botonCasa.alHacerClic(evento -> this.ventana.abrirSuperposicion("guardar"));
        botonInfo.alHacerClic(evento -> this.ventana.abrirSuperposicion("reglas"));

   
        this.agregar(this.ventanaAviso, (1440 - VentanaAviso.ANCHO_DISENO) / 2, (900 - VentanaAviso.ALTO_DISENO) / 2,
                VentanaAviso.ANCHO_DISENO, VentanaAviso.ALTO_DISENO);
    }

    private void crearMatrizDeCasillas() {
        for (int fila = 0; fila < 10; fila++) {
            for (int columna = 0; columna < 10; columna++) {
                CasillaBoton casilla = new CasillaBoton(Casillas.numero(fila, columna));
                this.casillas[fila][columna] = casilla;
                int x = Casillas.TABLERO_X + (int) Math.round(columna * Casillas.ANCHO_CASILLA);
                int y = Casillas.TABLERO_Y + (int) Math.round(fila * Casillas.ALTO_CASILLA);
                this.agregar(casilla, x, y, (int) Math.ceil(Casillas.ANCHO_CASILLA), (int) Math.ceil(Casillas.ALTO_CASILLA));
            }
        }
    }

    private void crearPanelesDeJugadores() {
        for (int i = 0; i < 4; i++) {
            final int indice = i;
            PanelJugador panel = new PanelJugador(i, "Jugador " + (i + 1));
            panel.setAlPulsarDados(() -> this.alPulsarDados(indice));
            this.paneles[i] = panel;
            this.agregar(panel, POSICION_DE_PANELES[i][0], POSICION_DE_PANELES[i][1], panel.getAnchoImagen(),
                    panel.getAltoImagen());
        }
    }

    public void setTema(int indiceTema) {
        this.temaActual = TEMAS[indiceTema];
        this.setFondo("fondos/" + this.temaActual + ".png");
    }

    public void setNombreJugador(int indice, String nombre) {
        this.paneles[indice].setNombre(nombre);
    }

    public void iniciarPartida(DatosPartida datos) {
        this.detenerPartida();
        this.cantidadJugadores = datos.getCantidad();

        for (int i = 0; i < 4; i++) {
            boolean juega = i < this.cantidadJugadores;
            this.paneles[i].setVisible(juega);
            if (juega) {
                this.prepararPanel(i, datos);
            }
        }
        this.turnoActual = datos.getTurno();
        this.partidaPausada = false;
        this.semaforoLanzamiento.drainPermits();
        this.iluminarCasilla(0);
        this.mostrarPresentacion();

        this.hilosJugadores = new HiloJugador[this.cantidadJugadores];
        for (int i = 0; i < this.cantidadJugadores; i++) {
            this.hilosJugadores[i] = new HiloJugador(this, i, this.paneles[i].getNombre());
            this.hilosJugadores[i].start();
        }

        final int primero = Math.max(0, Math.min(datos.getTurno(), this.cantidadJugadores - 1));
        this.temporizadorPresentacion = new Timer(2800, evento -> {
            this.ventanaAviso.ocultar();
            this.hilosJugadores[primero].darTurno();
        });
        this.temporizadorPresentacion.setRepeats(false);
        this.temporizadorPresentacion.start();
        this.repaint();
    }

    private void prepararPanel(int indice, DatosPartida datos) {
        PanelJugador panel = this.paneles[indice];
        this.esComputadora[indice] = datos.esComputadora(indice);
        panel.setNombre(datos.getNombre(indice));
        panel.setPosicion(datos.getPosicion(indice));
        panel.setDados(1, 1);
        panel.setLanzando(false, 0);
        panel.setEsTurno(false);
        panel.setEsperandoLanzamiento(false);
        double[] centro = Casillas.centro(Math.max(1, datos.getPosicion(indice)));
        panel.setFicha(centro[0] + DESPLAZAMIENTO_FICHAS[indice][0], centro[1] + DESPLAZAMIENTO_FICHAS[indice][1]);
    }

    private void mostrarPresentacion() {
        String[] lineas = new String[this.cantidadJugadores + 1];
        Color[] colores = new Color[this.cantidadJugadores + 1];
        float[] tamanos = new float[this.cantidadJugadores + 1];
        lineas[0] = "¡COMIENZA LA PARTIDA!";
        colores[0] = Recursos.CREMA;
        tamanos[0] = 34;
        for (int i = 0; i < this.cantidadJugadores; i++) {
            lineas[i + 1] = this.paneles[i].getNombre();
            colores[i + 1] = this.paneles[i].getColorDelJugador();
            tamanos[i + 1] = 32;
        }
        this.ventanaAviso.mostrar(lineas, colores, tamanos);
    }

    public void detenerPartida() {
        if (this.temporizadorPresentacion != null) {
            this.temporizadorPresentacion.stop();
        }
        for (HiloJugador hilo : this.hilosJugadores) {
            hilo.detener();
        }
        this.hilosJugadores = new HiloJugador[0];
        this.turnoActual = -1;
        this.partidaPausada = false;
        this.ventanaAviso.ocultar();
        for (PanelJugador panel : this.paneles) {
            panel.setEsTurno(false);
            panel.setEsperandoLanzamiento(false);
        }
    }

    public void pausar() {
        this.partidaPausada = true;
    }

    public void reanudar() {
        this.partidaPausada = false;
    }

    public DatosPartida tomarFoto() {
        String[] nombres = new String[this.cantidadJugadores];
        boolean[] computadora = new boolean[this.cantidadJugadores];
        for (int i = 0; i < this.cantidadJugadores; i++) {
            nombres[i] = this.paneles[i].getNombre();
            computadora[i] = this.esComputadora[i];
        }
        DatosPartida datos = new DatosPartida(nombres, computadora);
        for (int i = 0; i < this.cantidadJugadores; i++) {
            datos.setPosicion(i, this.paneles[i].getPosicion());
        }
        datos.setTurno(Math.max(this.turnoActual, 0));
        return datos;
    }

    private void alPulsarDados(int indice) {
        PanelJugador panel = this.paneles[indice];
        boolean esSuTurno = (indice == this.turnoActual);
        if (esSuTurno && panel.isEsperandoLanzamiento() && !this.esComputadora[indice]) {
            panel.setEsperandoLanzamiento(false);
            this.semaforoLanzamiento.release(); 
        }
    }

    boolean jugarTurno(int indice) throws InterruptedException {
        PanelJugador panel = this.paneles[indice];
        this.turnoActual = indice;
        this.marcarPanelDelTurno(indice);

        this.mostrarAviso(1100, "TURNO DE", 28, Recursos.CREMA, panel.getNombre(), 56, panel.getColorDelJugador());

        if (this.esComputadora[indice]) {
            this.dormir(600);
        } else {
            panel.setEsperandoLanzamiento(true);
            this.semaforoLanzamiento.acquire(); 
        }

        this.lanzarDados(panel);
        this.dormir(350);
        int suma = panel.getDado1() + panel.getDado2();

        int casillaMeta = Math.min(Casillas.TOTAL, panel.getPosicion() + suma);
        while (panel.getPosicion() < casillaMeta) {
            this.moverFicha(panel, panel.getPosicion() + 1, 8, 18, 16);
        }
        if (panel.getPosicion() >= Casillas.TOTAL) {
            this.ganar(panel);
            return false;
        }

        this.revisarEscaleraOSerpiente(panel);


        panel.setEsTurno(false);
        this.turnoActual = (indice + 1) % this.cantidadJugadores;
        if (this.ventana != null && this.ventana.isGuardadoAutomatico()) {
            this.ventana.guardarPartida();
        }
        return true;
    }

    void pasarTurno(int indice) {
        int siguiente = (indice + 1) % this.hilosJugadores.length;
        this.hilosJugadores[siguiente].darTurno();
    }

    private void marcarPanelDelTurno(int indice) {
        for (int i = 0; i < 4; i++) {
            this.paneles[i].setEsTurno(i == indice);
        }
    }

    private void lanzarDados(PanelJugador panel) throws InterruptedException {
        for (int cuadro = 0; cuadro < 16; cuadro++) {
            double giro = (this.azar.nextDouble() - 0.5) * 0.6;
            panel.setDados(1 + this.azar.nextInt(6), 1 + this.azar.nextInt(6));
            panel.setLanzando(true, giro);
            this.dormir(70);
        }
        panel.setLanzando(false, 0);
    }

    private void moverFicha(PanelJugador panel, int casilla, int pasos, int milisegundosPorPaso, int salto)
            throws InterruptedException {
        double[] centro = Casillas.centro(casilla);
        int indiceColor = panel.getIndiceColor();
        double destinoX = centro[0] + DESPLAZAMIENTO_FICHAS[indiceColor][0];
        double destinoY = centro[1] + DESPLAZAMIENTO_FICHAS[indiceColor][1];
        double inicioX = panel.getXFicha();
        double inicioY = panel.getYFicha();

        for (int paso = 1; paso <= pasos; paso++) {
            double avance = (double) paso / pasos; 
            double x = inicioX + (destinoX - inicioX) * avance;
            double y = inicioY + (destinoY - inicioY) * avance - salto * Math.sin(Math.PI * avance);
            panel.setFicha(x, y);
            this.repaint();
            this.dormir(milisegundosPorPaso);
        }
        panel.setFicha(destinoX, destinoY);
        panel.setPosicion(casilla); 
        this.iluminarCasilla(casilla);
        this.repaint();
    }

    private void revisarEscaleraOSerpiente(PanelJugador panel) throws InterruptedException {
        int casillaActual = panel.getPosicion();
        int casillaDestino = Casillas.destino(casillaActual);
        if (casillaDestino == casillaActual) {
            return; 
        }
        boolean esEscalera = Casillas.esEscalera(casillaActual);
        String titulo = "¡SERPIENTE!";
        String verbo = "Bajas";
        Color color = new Color(0xE35D4B);
        if (esEscalera) {
            titulo = "¡ESCALERA!";
            verbo = "Subes";
            color = Recursos.DORADO;
        }
        this.dormir(250);
        this.mostrarAviso(1100, titulo, 44, color, verbo + " de la " + casillaActual + " a la " + casillaDestino, 28,
                Recursos.CREMA);
        this.moverFicha(panel, casillaDestino, 36, 22, 0);
        this.dormir(300);
    }

    private void ganar(PanelJugador panel) throws InterruptedException {
        this.mostrarAviso(1500, "¡LLEGÓ A LA META!", 40, Recursos.DORADO, panel.getNombre(), 56,
                panel.getColorDelJugador());
        final String nombreGanador = panel.getNombre();
        for (HiloJugador hilo : this.hilosJugadores) {
            hilo.detener();
        }
        SwingUtilities.invokeLater(() -> {
            if (this.ventana != null) {
                this.ventana.mostrarGanador(nombreGanador);
            }
        });
    }

    private void mostrarAviso(int milisegundos, String titulo, float tamanoTitulo, Color colorTitulo, String texto,
            float tamanoTexto, Color colorTexto) throws InterruptedException {
        this.ventanaAviso.mostrarDosLineas(titulo, tamanoTitulo, colorTitulo, texto, tamanoTexto, colorTexto);
        this.dormir(milisegundos);
        this.ventanaAviso.ocultar();
    }

    private void dormir(long milisegundos) throws InterruptedException {
        Thread.sleep(milisegundos);
        while (this.partidaPausada) {
            Thread.sleep(40);
        }
    }

    private void iluminarCasilla(int numero) {
        SwingUtilities.invokeLater(() -> {
            if (this.casillaIluminada != null) {
                this.casillaIluminada.setIluminada(false);
                this.casillaIluminada = null;
            }
            for (CasillaBoton[] fila : this.casillas) {
                for (CasillaBoton casilla : fila) {
                    if (casilla.getNumero() == numero) {
                        casilla.setIluminada(true);
                        this.casillaIluminada = casilla;
                    }
                }
            }
        });
    }

    
    private void dibujarIndicacionDeLanzamiento(Graphics2D g) {
        int turno = this.turnoActual;
        if (turno < 0 || turno >= this.cantidadJugadores) {
            return;
        }
        PanelJugador panel = this.paneles[turno];
        if (panel.isEsperandoLanzamiento()) {
            this.textoCentrado(g, panel.getNombre() + ", haz clic en tus dados para lanzar", 720, 874, 20,
                    Recursos.DORADO, 1000);
        }
    }

    @Override
    protected void dibujar(Graphics2D g) {
        g.drawImage(Recursos.imagen("tableros/" + this.temaActual + ".png"), Casillas.TABLERO_X, Casillas.TABLERO_Y, null);
        for (int i = 0; i < POSICION_ESCALERAS.length; i++) {
            BufferedImage escalera = Recursos.imagen("escaleras/escalera_" + (i + 1) + ".png");
            g.drawImage(escalera, POSICION_ESCALERAS[i][0], POSICION_ESCALERAS[i][1], null);
        }
        for (int i = 0; i < POSICION_SERPIENTES.length; i++) {
            BufferedImage serpiente = Recursos.imagen("serpientes/serpiente_" + (i + 1) + ".png");
            g.drawImage(serpiente, POSICION_SERPIENTES[i][0], POSICION_SERPIENTES[i][1], null);
        }
        this.dibujarIndicacionDeLanzamiento(g);
        
        for (int i = 0; i < this.cantidadJugadores; i++) {
            BufferedImage ficha = Recursos.imagen("jugadores/ficha_" + PanelJugador.COLORES[i] + ".png");
            int x = (int) Math.round(this.paneles[i].getXFicha() - ficha.getWidth() / 2.0);
            int y = (int) Math.round(this.paneles[i].getYFicha() - ficha.getHeight() / 2.0);
            g.drawImage(ficha, x, y, null);
        }
    }
}
