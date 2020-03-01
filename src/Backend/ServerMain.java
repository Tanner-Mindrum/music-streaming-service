package Backend;

import java.net.SocketException;

public class ServerMain {

    public static void main(String[] args) throws SocketException {
        Dispatcher dispatcher = new Dispatcher();
        SongDispatcher sd = new SongDispatcher();
        //dispatcher.registerObject("getSongChunk", "SongServices");
        //dispatcher.registerObject("getFileSize", "SongServices");
        dispatcher.registerObject("getSongChunk", "SongServices");

        new MusicServer().start();
        Main.main(args);
    }
}
