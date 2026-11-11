package windowView;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public final class Musica {

    private static Clip sonido;
    private static boolean activada = true;

    private Musica() {
    }

    public static void iniciar() {
        try {
            AudioInputStream audio;
            URL url = Musica.class.getResource("/resources/musica/tema.wav");
            if (url != null) {
                audio = AudioSystem.getAudioInputStream(url);
            } else {
                File archivo = new File("src/resources/musica/tema.wav");
                if (!archivo.exists()) {
                    archivo = new File("Proyecto/src/resources/musica/tema.wav");
                }
                audio = AudioSystem.getAudioInputStream(archivo);
            }
            sonido = AudioSystem.getClip();
            sonido.open(audio);
            if (sonido.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volumen = (FloatControl) sonido.getControl(FloatControl.Type.MASTER_GAIN);
                volumen.setValue(-8f); 
            }
            if (activada) {
                sonido.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception error) {
            sonido = null; 
        }
    }

    public static void setActivada(boolean activada) {
        Musica.activada = activada;
        if (sonido == null) {
            return;
        }
        if (activada) {
            sonido.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            sonido.stop();
        }
    }

    public static boolean isActivada() {
        return activada;
    }
}
