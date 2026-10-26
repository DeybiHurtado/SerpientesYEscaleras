package model;

public class Escalera {
    private int base;
    private int cima;
    private int poder;
    private transient boolean usada = false;

    public Escalera(int base, int cima, int poder) {
        this.base = base;
        this.cima = cima;
        this.poder = poder;
    }

    public String getPoder(Jugador jugador, Dados dados) {
        switch (poder) {
            case 1:
                return unTurno(jugador);
            case 2:
                return dobleTurno(jugador);
            case 3:
                return dobleONada(jugador, dados);
            default:
                return predeterminado(jugador);
        }
    }

    public String predeterminado(Jugador jugador) {
        jugador.setPosicion(cima);
        return "¡Escalera normal! Subes desde la base (" + base + ") hasta la cima (" + cima + ").";
    }

    public String unTurno(Jugador jugador) {
        if (usada) {
            return "¡Llegaste a una escalera de un solo uso, pero ya fue utilizada antes y desapareció! Te quedas en la casilla " + base;
        } else {
            usada = true; 
            jugador.setPosicion(cima);
            return "¡Escalera de Un Solo Uso! Subes a la cima (" + cima + "). ¡La escalera se ha desintegrado y ya no se podrá volver a usar!";
        }
    }

    public String dobleTurno(Jugador jugador) {
        jugador.setPosicion(cima);
        jugador.setTieneTurnoExtra(true);
        return "¡Escalera de Doble Turno! Subes a la cima (" + cima + ") y obtienes un turno extra para tirar de inmediato.";
    }

    public String dobleONada(Jugador jugador, Dados dados) {
        int tiro = dados.lanzarDado();
        StringBuilder sb = new StringBuilder();
        sb.append("¡Escalera Doble o Nada! Debes tirar el dado. Salió: ").append(tiro).append(". ");

        if (tiro % 2 != 0) {
            sb.append("¡Cayó número IMPAR! No avanzas nada y te quedas estancado en la base (").append(base).append(").");
            jugador.setPosicion(base);
        } else {
            int diferencia = cima - base;
            int nuevaPosicion = base + (diferencia * 2);
            if (nuevaPosicion > 100) nuevaPosicion = 100;

            sb.append("¡Cayó número PAR! Avanzas el doble de la diferencia, impulsándote hasta la gran casilla ").append(nuevaPosicion);
            jugador.setPosicion(nuevaPosicion);
        }
        return sb.toString();
    }

    public int getBase() { return base; }
    public void setBase(int base) { this.base = base; }
    public int getCima() { return cima; }
    public void setCima(int cima) { this.cima = cima; }
     public int getNumeroPoder() { return poder; }
    public boolean isUsada() { return usada; }

    public String getNombrePoder() {
        switch (poder) {
            case 1: return "Un solo uso";
            case 2: return "Doble turno";
            case 3: return "Doble o nada";
            default: return "Normal";
        }
    }
}