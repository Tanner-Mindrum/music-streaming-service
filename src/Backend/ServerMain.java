package Backend;

import java.net.SocketException;

public class ServerMain {

    public static void main(String[] args) throws SocketException {
        new MusicServer().start();
    }
}
