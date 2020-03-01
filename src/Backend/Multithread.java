package Backend;

import javazoom.jl.decoder.JavaLayerException;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Multithread implements Runnable {

    private SongInfo songInfo;
    private Player player;
    private String idToPlay;
    private boolean running;
    private Proxy proxy;
    private CommunicationModule comm;

    // Construct a default thread
    public Multithread(CommunicationModule cm) throws IOException, ParseException {
        songInfo = new SongInfo();
        comm = cm;
        player = new Player();
        idToPlay = "";
        running = false;
    }

    // Construct a thread given an ID
    public Multithread(String id, Proxy p) throws IOException, ParseException {
        songInfo = new SongInfo();
        player = new Player();
        idToPlay = id;
        running = false;
        proxy = p;
    }

    // Run the thread, and this particular thread will call the mp3 player
    @Override
    public void run() {
        try {
//            player = new Player("out/production/music-streaming-service/musicsrc/" +
//                    idToPlay + ".mp3");
            if (comm == null) {
                System.out.println("BLEEHH");
            }
            player = new Player(idToPlay, comm);
            player.mp3play();
        } catch (IOException | JavaLayerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Setters for specific fields
    public void setIdToPlay(String pID) {
        idToPlay = pID;
    }

    public void setIsRunning(boolean running) {
        this.running = running;
    }

    // Stop a running song
    public void stopSong() {
        player.mp3stop();
    }
}
