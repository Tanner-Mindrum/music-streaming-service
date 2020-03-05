package Backend;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class MusicServer extends Thread {

    private DatagramSocket socket;
    private byte[] buf = new byte[256];
    private Dispatcher dispatcher;
    private SongDispatcher songDispatcher;
    private SongInfo songInfo;
    private User user;
    private ModifyUser modifyUser;

    public MusicServer() throws IOException, ParseException {
        socket = new DatagramSocket(4445);
        this.dispatcher = new Dispatcher();
        this.songDispatcher = new SongDispatcher();
        this.songInfo = new SongInfo();
        this.user = new User();
        this.modifyUser = new ModifyUser();

        dispatcher.registerObject(user, "User");
        dispatcher.registerObject(songDispatcher, "SongServices");
        dispatcher.registerObject(songInfo, "SongInfo");
        dispatcher.registerObject(modifyUser, "ModifyUser");
        System.out.println("SERVER STARTED");
    }

    public void run() {
        boolean running = true;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String received = new String(packet.getData(), 0, packet.getLength());

            System.out.println("SERVER RECEIVE: " + received);

            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            try {
                String ret = dispatcher.dispatch(received);
                byte[] buf;
                buf = ret.getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);
                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                socket.send(packet);
            }
            catch (IOException | java.text.ParseException | ParseException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
