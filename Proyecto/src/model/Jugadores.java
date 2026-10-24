package model;

import java.util.ArrayList;

public class Jugadores {
      private Jugador jugador;
     private ArrayList<Jugador> ArrayJugadores;

     public Jugadores(){
         this.ArrayJugadores= new ArrayList<>();
     }

     public Jugadores(Jugador jugador){
        this.jugador= jugador;
         this.ArrayJugadores= new ArrayList<>();

     }

     public Jugador getJugador() {
         return jugador;
     }

     public void setJugador(Jugador jugador) {
         this.jugador = jugador;
     }

     public ArrayList<Jugador> getArrayJugadores() {
         return ArrayJugadores;
     }

     public void setArrayJugadores(ArrayList<Jugador> arrayJugadores) {
         ArrayJugadores = arrayJugadores;
     }
     
     public void eliminarJugador(int id){
            if(this.ArrayJugadores.isEmpty()){
               this.ArrayJugadores = new ArrayList<>();
            }else if(this.ArrayJugadores.contains(buscarJugador(id))){
                  this.ArrayJugadores.remove(buscarJugador(id));
            }
     }
     public Jugador buscarJugador(int id){
        Jugador jugadorEncontrado=null;
          for(Jugador j: this.ArrayJugadores){
               if(j.getId()== id){
                jugadorEncontrado = j;                
               return jugadorEncontrado;
               }
          }
         return null;
     }
     public void anadirJugador(Jugador jugador){
                this.ArrayJugadores.add(jugador);
            
     }

}
