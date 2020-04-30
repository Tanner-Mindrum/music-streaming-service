package Backend;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerMain {

    private static CommandLine cl;

    public static void main(String[] args) throws Exception {
        MusicServer ms = new MusicServer();
        ms.start();
        cl = ms.getCommandLine();
        Main.main(args, cl);
    }
}
