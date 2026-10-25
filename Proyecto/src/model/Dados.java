package model;

public class Dados {

    private Dado dado1;
    private Dado dado2;

    private int resultadoDado1;
    private int resultadoDado2;

    public Dados(){
        this.dado1 = new Dado();
        this.dado2 = new Dado();
    }

    public int lanzarDado(){
        this.resultadoDado1 = dado1.lanzarDado();
        this.resultadoDado2 = dado2.lanzarDado();

        return resultadoDado1 + resultadoDado2;
    }

    public int getResultadoDado1(){
        return resultadoDado1;
    }

    public int getResultadoDado2(){
        return resultadoDado2;
    }
}
