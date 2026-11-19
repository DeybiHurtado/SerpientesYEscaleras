package windowView;

import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import model.RegistroPartida;
import model.ResultadoJugador;
import repository.RegistroPartidasRepository;

public class PantallaEstadisticas extends Pantalla {

    private static final int CANTIDAD_PARTIDAS = 10;

    private final VentanaGrafica ventana;
    private final BotonImagen[] botonesPartidas = new BotonImagen[CANTIDAD_PARTIDAS];
    private final CampoTexto campoBusqueda;
    private final String[] titulosCuadros = new String[4];
    private final String[] valoresCuadros = new String[4];

    private ArrayList<RegistroPartida> todasLasPartidas = new ArrayList<>();
    private ArrayList<RegistroPartida> registros = new ArrayList<>();
    private String textoBuscado = "";

    protected PantallaEstadisticas(VentanaGrafica ventana) {
        super(1440, 900, "fondos/estadisticas.png");
        this.ventana = ventana;

        BotonImagen botonVolver = this.agregar(this.crearBoton("VOLVER"), 60, 65, 230, 70);
        botonVolver.alHacerClic(evento -> this.ventana.mostrar("inicio"));

        // Un boton por cada partida, en dos columnas de cinco, todos iguales
        for (int i = 0; i < CANTIDAD_PARTIDAS; i++) {
            final int indice = i;
            int x = 100 + (i % 2) * 630;
            int y = 325 + (i / 2) * 72;
            BotonImagen boton = this.agregar(this.crearBoton(""), x, y, 610, 64);
            boton.setVisible(false);
            boton.alHacerClic(evento -> this.ventana.abrirDetallePartida(this.registros.get(indice)));
            this.botonesPartidas[i] = boton;
        }

        // El buscador cada vez que se suelta una tecla se filtra la lista
        this.campoBusqueda = this.agregar(new CampoTexto("Escribe el nombre de un jugador"), 450, 738, 520, 54);
        this.campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evento) {
                PantallaEstadisticas.this.filtrar();
            }
        });

        BotonImagen botonLimpiar = this.agregar(this.crearBoton("VER TODAS"), 1010, 735, 290, 60);
        botonLimpiar.alHacerClic(evento -> this.buscarJugador(""));

        // Cada vez que la pantalla se muestra, se vuelven a leer las partidas
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evento) {
                PantallaEstadisticas.this.actualizar();
            }
        });
        this.actualizar();
    }

    private BotonImagen crearBoton(String texto) {
        return new BotonImagen(texto, "botones/normal.png", "botones/resaltado.png", 16);
    }

    // Se llama cada vez que se entra a la pantalla, para leer las partidas nuevas
    public void actualizar() {
        RegistroPartidasRepository repositorio = new RegistroPartidasRepository();
        this.todasLasPartidas = repositorio.getTodas();
        this.buscarJugador("");
    }

    public void buscarJugador(String nombre) {
        this.campoBusqueda.setText(nombre);
        this.filtrar();
    }

    // Deja en la lista solo las ultimas 10 partidas del jugador buscado (o de todos si no hay busqueda)
    private void filtrar() {
        this.textoBuscado = this.campoBusqueda.getText().trim();
        this.registros = new ArrayList<>();
        for (RegistroPartida registro : this.todasLasPartidas) {
            boolean coincide = this.textoBuscado.isEmpty() || registro.buscarJugador(this.textoBuscado) != null;
            if (coincide && this.registros.size() < CANTIDAD_PARTIDAS) {
                this.registros.add(registro);
            }
        }
        this.mostrarBotones();
        if (this.textoBuscado.isEmpty()) {
            this.calcularResumenGeneral();
        } else {
            this.calcularResumenDelJugador();
        }
        this.repaint();
    }

    private void mostrarBotones() {
        for (int i = 0; i < CANTIDAD_PARTIDAS; i++) {
            boolean hayPartida = i < this.registros.size();
            this.botonesPartidas[i].setVisible(hayPartida);
            if (hayPartida) {
                this.botonesPartidas[i].setTexto(this.textoDeLaFila(this.registros.get(i)));
            }
        }
    }

    private String textoDeLaFila(RegistroPartida registro) {
        if (this.textoBuscado.isEmpty()) {
            return registro.getFecha() + "  |  Gana " + registro.getGanador() + "  |  "
                    + registro.getJugadasDelGanador() + " jugadas";
        }
        ResultadoJugador jugador = registro.buscarJugador(this.textoBuscado);
        String resultado = "Perdió";
        if (registro.ganoElJugador(jugador)) {
            resultado = "Ganó";
        }
        return registro.getFecha() + "  |  " + jugador.getNombre() + "  |  " + resultado + "  |  "
                + jugador.getJugadas() + " jugadas";
    }

    private void calcularResumenGeneral() {
        String promedio = "-";
        String mejor = "-";
        String masLarga = "-";
        if (!this.registros.isEmpty()) {
            int suma = 0;
            int menor = this.registros.get(0).getJugadasDelGanador();
            int mayor = menor;
            for (RegistroPartida registro : this.registros) {
                int jugadas = registro.getJugadasDelGanador();
                suma = suma + jugadas;
                menor = Math.min(menor, jugadas);
                mayor = Math.max(mayor, jugadas);
            }
            promedio = String.format("%.1f", (double) suma / this.registros.size());
            mejor = menor + " jugadas";
            masLarga = mayor + " jugadas";
        }
        this.ponerCuadros("PARTIDAS", String.valueOf(this.registros.size()), "PROMEDIO PARA GANAR", promedio,
                "MEJOR PARTIDA", mejor, "PARTIDA MÁS LARGA", masLarga);
    }

    private void calcularResumenDelJugador() {
        String promedio = "-";
        String mejorVictoria = "-";
        int ganadas = 0;
        if (!this.registros.isEmpty()) {
            int suma = 0;
            int menorVictoria = 0;
            for (RegistroPartida registro : this.registros) {
                ResultadoJugador jugador = registro.buscarJugador(this.textoBuscado);
                suma = suma + jugador.getJugadas();
                if (registro.ganoElJugador(jugador)) {
                    ganadas++;
                    if (menorVictoria == 0 || jugador.getJugadas() < menorVictoria) {
                        menorVictoria = jugador.getJugadas();
                    }
                }
            }
            promedio = String.format("%.1f", (double) suma / this.registros.size());
            if (menorVictoria > 0) {
                mejorVictoria = menorVictoria + " jugadas";
            }
        }
        this.ponerCuadros("PARTIDAS JUGADAS", String.valueOf(this.registros.size()), "VICTORIAS",
                String.valueOf(ganadas), "PROMEDIO DE JUGADAS", promedio, "MEJOR VICTORIA", mejorVictoria);
    }

    private void ponerCuadros(String titulo1, String valor1, String titulo2, String valor2, String titulo3,
            String valor3, String titulo4, String valor4) {
        this.titulosCuadros[0] = titulo1;
        this.valoresCuadros[0] = valor1;
        this.titulosCuadros[1] = titulo2;
        this.valoresCuadros[1] = valor2;
        this.titulosCuadros[2] = titulo3;
        this.valoresCuadros[2] = valor3;
        this.titulosCuadros[3] = titulo4;
        this.valoresCuadros[3] = valor4;
    }

    @Override
    protected void dibujar(Graphics2D g) {
        Recursos.dibujarEstirada(g, Recursos.imagen("botones/resaltado.png"), 370, 60, 700, 90, 16, 16);
        this.textoCentrado(g, "ESTADÍSTICAS", 720, 105, 60, Recursos.DORADO, 900);

        this.dibujarCuadros(g);
        this.dibujarTituloDeLaLista(g);
        this.dibujarBuscador(g);
    }

    private void dibujarCuadros(Graphics2D g) {
        for (int i = 0; i < 4; i++) {
            if (this.titulosCuadros[i] == null) {
                continue;
            }
            int x = 100 + i * 315;
            Recursos.dibujarEstirada(g, Recursos.imagen("botones/normal.png"), x, 165, 295, 110, 16, 16);
            this.textoCentrado(g, this.titulosCuadros[i], x + 147, 198, 20, Recursos.GRIS_AZUL, 260);
            this.textoCentrado(g, this.valoresCuadros[i], x + 147, 242, 40, Recursos.DORADO, 260);
        }
    }

    private void dibujarTituloDeLaLista(Graphics2D g) {
        String titulo = "ÚLTIMAS 10 PARTIDAS";
        if (!this.textoBuscado.isEmpty()) {
            titulo = "HISTORIAL DE " + this.textoBuscado.toUpperCase();
        }
        this.textoCentrado(g, titulo, 720, 302, 26, Recursos.CREMA, 1000);

        if (this.registros.isEmpty()) {
            String mensaje = "Aún no hay partidas jugadas";
            if (!this.textoBuscado.isEmpty()) {
                mensaje = "No hay partidas de \"" + this.textoBuscado + "\"";
            }
            this.textoCentrado(g, mensaje, 720, 480, 36, Recursos.CREMA, 900);
        }
    }

    private void dibujarBuscador(Graphics2D g) {
        Recursos.dibujarEstirada(g, Recursos.imagen("botones/normal.png"), 100, 715, 1240, 100, 16, 16);
        this.textoIzquierda(g, "BUSCAR JUGADOR", 140, 765, 28, Recursos.DORADO);
    }
}
