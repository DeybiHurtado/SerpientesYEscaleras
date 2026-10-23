package model;
import java.util.Random;

public class Dado {

    private int numCaras = 6;

    public int lanzarDado(){

        Random generarNum = new Random();
        int num = generarNum.nextInt(numCaras) + 1;
        return num;
    }
}
