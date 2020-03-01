package Backend;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerMain {

    public static void main(String[] args) throws IOException, ParseException {
//        Dispatcher d = new Dispatcher();
//        SongDispatcher sd = new SongDispatcher();
//        d.registerObject(sd, "SongServices");
//        d.registerObject(sd, "SongServices");
        new MusicServer().start();
        Main.main(args);
    }
}
