package Backend;

import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;

public class Player {

    /**
     * Play a given audio file.
     * @param file Path of the audio file.
     */
    public void mp3play(String file) {
        try {
            // It uses Backend.CECS327InputStream as InputStream to play the song
            InputStream is = new CECS327InputStream(file);
            javazoom.jl.player.Player mp3player = new javazoom.jl.player.Player(is);
            mp3player.play();
        }
        catch (JavaLayerException | IOException exception) {
            exception.printStackTrace();
        }
    }

}

