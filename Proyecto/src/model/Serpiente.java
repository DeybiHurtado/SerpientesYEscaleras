package model;

public class Serpiente {
    private int cabeza;
    private int cola;
    private int poder;

    public Serpiente(int cabeza, int cola, int poder) {
        this.cabeza = cabeza;
        this.cola = cola;
        this.poder = poder;
    }

    public String getPoder(Jugador jugador, Dados dados) {
        switch (poder) {
            case 1:
                return quitaTurno(jugador);
            case 2:
                return veneno(jugador);
            case 3:
                return dobleONada(jugador, dados);
            default:
                return predeterminado(jugador);
        }
    }

    public String predeterminado(Jugador jugador) {
        jugador.setPosicion(cola);
        return "¡Oh no! Una serpiente normal te ha mordido. Baja desde la cabeza (" + cabeza + ") hasta la cola (" + cola + ").";
    }

    public String quitaTurno(Jugador jugador) {
        jugador.setPosicion(cola);
        jugador.setPierdeTurno(true);
        return "¡Serpiente Quita Turno! Te bajan a la casilla " + cola + " y, además, ¡perderás tu siguiente turno!";
    }

    public String veneno(Jugador jugador) {
        jugador.setPosicion(cola);
        jugador.setNivelVeneno(1);
        return "¡Serpiente Venenosa! Te bajan a la casilla " + cola + " y tus dados han quedado envenenados por los próximos 2 turnos.";
    }

    public String dobleONada(Jugador jugador, Dados dados) {
        int tiro = dados.lanzarDado();
        StringBuilder sb = new StringBuilder();
        sb.append("¡Serpiente Doble o Nada! Debes tirar el dado de nuevo. Salió: ").append(tiro).append(". ");

        if (tiro % 2 == 0) {
            sb.append("¡Cayó número PAR! Te salvaste, la serpiente no te devuelve y te quedas en la casilla ").append(cabeza);
            jugador.setPosicion(cabeza);
        } else {
            int diferencia = cabeza - cola;
            int nuevaPosicion = cabeza - (diferencia * 2);
            if (nuevaPosicion < 1) nuevaPosicion = 1;
            
            sb.append("¡Cayó número IMPAR! Te devuelven el doble, cayendo estrepitosamente hasta la casilla ").append(nuevaPosicion);
            jugador.setPosicion(nuevaPosicion);
        }
        return sb.toString();
    }

    public int getCabeza() { return cabeza; }
    public void setCabeza(int cabeza) { this.cabeza = cabeza; }
    public int getCola() { return cola; }
    public void setCola(int cola) { this.cola = cola; }
}