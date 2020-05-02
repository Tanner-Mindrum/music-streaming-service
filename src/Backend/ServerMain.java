package Backend;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerMain {

    private static DFS myDfs;

    public static void main(String[] args) throws Exception {
        MusicServer ms = new MusicServer();
        ms.start();
        myDfs = ms.getMyDfs();
        Main.main(args, myDfs);
    }
}
