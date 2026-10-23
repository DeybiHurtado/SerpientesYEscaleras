package model;

public class Dados {

    private Dado dado1;
    private Dado dado2;

    private int resultadoDado1;
    private int resultadoDado2;

    public Dados(){
        this.dado1 = dado1;
        this.dado2 = dado2;
    }

    public int lanzarDado(){
        this.resultadoDado1 = resultadoDado1;
        this.resultadoDado2 = resultadoDado2;

        return resultadoDado1 + resultadoDado2;
    }

    public int getResultadoDado1(){
        return resultadoDado1;
    }

    public int getResultadoDado2(){
        return resultadoDado2;
    }
}
