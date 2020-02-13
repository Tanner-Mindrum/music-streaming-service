package Backend;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Player {

    private javazoom.jl.player.advanced.AdvancedPlayer mp3player;
    private InputStream is;

    // Constructs a null input stream and advanced player object
    public Player() {
        is = null;
        mp3player = null;
    }

    public Player (String path) throws IOException, JavaLayerException {
        try {
            is = new CECS327InputStream(path);
            mp3player = new AdvancedPlayer(is);
        }
        catch (FileNotFoundException f) {
            System.out.println("No data for song");
        }
        //mp3player = new AdvancedPlayer(is);
    }

    /**
     * Play a given audio file.
     */
    public void mp3play() throws JavaLayerException, InterruptedException {
        if (mp3player != null) {
            mp3player.play();
        }
    }

    // Close MP3 player
    public void mp3stop() {
        if (mp3player != null) { mp3player.close(); }
    }
}

