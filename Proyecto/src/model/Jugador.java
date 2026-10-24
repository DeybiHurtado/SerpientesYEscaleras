package model;

public class Jugador {

    private static int contadorId = 1;

    private int id;
    private String nombre;
    private int posicion;
    private int puntaje;
    private boolean pierdeTurno = false;
    private int nivelVeneno = 0;
    private boolean tieneTurnoExtra = false;

    public Jugador(String nombre) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.posicion = 1;
        this.puntaje = 0;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public boolean isPierdeTurno() {
        return pierdeTurno;
    }

    public void setPierdeTurno(boolean pierdeTurno) {
        this.pierdeTurno = pierdeTurno;
    }

    public int getNivelVeneno() {
        return nivelVeneno;
    }

    public void setNivelVeneno(int nivelVeneno) {
        this.nivelVeneno = nivelVeneno;
    }

    public boolean isTieneTurnoExtra() {
        return tieneTurnoExtra;
    }

    public void setTieneTurnoExtra(boolean tieneTurnoExtra) {
        this.tieneTurnoExtra = tieneTurnoExtra;
    }
      @Override 
    public String toString(){
        return "\nNombre "+nombre+"\nCodigo "+id+"\nPosicion "+posicion+"\nPuntaje "+puntaje+"\n__________________";
    }
}