package Backend;

import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerMain {

    public static void main(String[] args) throws SocketException, UnknownHostException {
//        Dispatcher d = new Dispatcher();
//        SongDispatcher sd = new SongDispatcher();
//        d.registerObject(sd, "SongServices");
//        d.registerObject(sd, "SongServices");
        new MusicServer().start();
        Main.main(args);
    }
}
