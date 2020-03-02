package Backend;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;

public class CommunicationModule {

    private int port;
    private InetAddress address;
    private Dispatcher dispatcher;
    private SongDispatcher songDispatcher;

    public CommunicationModule() throws IOException, org.json.simple.parser.ParseException {
        port = 4445;
        address = InetAddress.getByName("localhost");
        this.dispatcher = new Dispatcher();
        this.songDispatcher = new SongDispatcher();
        dispatcher.registerObject(songDispatcher, "SongServices");
    }

    public void send(String msg) throws IOException {
        final DatagramSocket socket = new DatagramSocket();
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        socket.close();
    }

    public String recieve(String request) throws ParseException, org.json.simple.parser.ParseException, IOException {
        return dispatcher.dispatch(request);
    }
}
