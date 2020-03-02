package Backend;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Player {

    private javazoom.jl.player.advanced.AdvancedPlayer mp3player;
    private InputStream is;
    private Proxy proxy;
    private CommunicationModule comm;

    // Constructs a null input stream and advanced player object
    public Player() throws IOException, ParseException {
        is = null;
        mp3player = null;
    }

    public Player (String path, CommunicationModule cm) throws IOException, JavaLayerException {
        try {
            comm = cm;
            proxy = new Proxy(comm);
            is = new CECS327RemoteInputStream(path, proxy);
            mp3player = new AdvancedPlayer(is);
        }
        catch (FileNotFoundException | ParseException | InterruptedException | java.text.ParseException f) {
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

