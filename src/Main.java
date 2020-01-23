import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;

public class Main {

    /**
     * Play a given audio file.
     * @param file Path of the audio file.
     */
    void mp3play(String file) {
        try {
            // It uses CECS327InputStream as InputStream to play the song
            InputStream is = new CECS327InputStream(file);
            javazoom.jl.player.Player mp3player = new javazoom.jl.player.Player(is);
            mp3player.play();
        }
        catch (JavaLayerException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Integer i;
        Main player = new Main();
        /* Change path for testing
        *  Tanner's path: "C://CECS 327//music-streaming-service//imperial.mp3"
        */
        player.mp3play("C://CECS 327//music-streaming-service//imperial.mp3");
    }

}

