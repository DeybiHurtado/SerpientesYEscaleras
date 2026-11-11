package runner;

import javax.swing.SwingUtilities;

import windowView.Musica;
import windowView.VentanaGrafica;


public class RunGrafico {

    public static void main(String[] args) {
        Musica.iniciar();
        SwingUtilities.invokeLater(() -> new VentanaGrafica().setVisible(true));
    }
}
