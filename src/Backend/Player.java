package Backend;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.io.InputStream;

public class Player {

    private javazoom.jl.player.advanced.AdvancedPlayer mp3player;
    private InputStream is;

    public Player() {
        is = null;
        mp3player = null;
    }

    public Player (String path) throws IOException, JavaLayerException {
        is = new CECS327InputStream(path);
        mp3player = new AdvancedPlayer(is);
    }

    /**
     * Play a given audio file.
     */
    public void mp3play() throws JavaLayerException {
        if (mp3player != null) { mp3player.play(); }
    }

    public void mp3stop() {
        if (mp3player != null) { mp3player.close(); }
    }
}

