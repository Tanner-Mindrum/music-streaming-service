package Backend;

import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;

public class Player {

    private javazoom.jl.player.advanced.AdvancedPlayer mp3player;
    private InputStream is;

    /**
     * Play a given audio file.
     * @param file Path of the audio file.
     */
    public void mp3play(String file) {
        try {
            System.out.println(file);
            // It uses Backend.CECS327InputStream as InputStream to play the song
            is = new CECS327InputStream(file);
            mp3player = new javazoom.jl.player.advanced.AdvancedPlayer(is);
            mp3player.play();

        }
        catch (JavaLayerException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public void mp3stop() {
        mp3player.stop();
    }

}

