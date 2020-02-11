package Backend;

import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;

public class Multithread implements Runnable {

    private SongInfo songInfo;
    private Player player;
    private String idToPlay;
    private boolean running;

    public Multithread() {
        songInfo = new SongInfo();
        player = new Player();
        idToPlay = "";
        running = false;
    }

    public Multithread(String id) {
        songInfo = new SongInfo();
        player = new Player();
        idToPlay = id;
        running = false;
    }

    @Override
    public void run() {
        try {
            player = new Player("out/production/music-streaming-service/musicsrc/" +
                    idToPlay + ".mp3");
            player.mp3play();
        } catch (IOException | JavaLayerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setIdToPlay(String pID) {
        idToPlay = pID;
    }

    public void setIsRunning(boolean running) {
        this.running = running;
    }

    public void stopSong() {
        player.mp3stop();
    }
}
