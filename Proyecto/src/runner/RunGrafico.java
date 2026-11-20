package runner;

import javax.swing.SwingUtilities;

import controller.control;
import windowView.Musica;
import windowView.VentanaGrafica;


public class RunGrafico {

    public static void main(String[] args) {
        Musica.iniciar();
        control controlador = new control();
        SwingUtilities.invokeLater(() -> new VentanaGrafica(controlador).setVisible(true));
    }
}
