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
    private byte[] buf = new byte[512];
    private Dispatcher dispatcher;
    private SongDispatcher songDispatcher;
    private SongInfo songInfo;
    private User user;
    private ModifyUser modifyUser;
//    private CommandLine command;
    private DFS myDfs;

    public MusicServer() throws Exception {
        //this.command = new CommandLine(2001, 4445);
        this.myDfs = new DFS(2002);
        //myDfs.join("localhost", 4445);
        socket = new DatagramSocket(4445);
        this.dispatcher = new Dispatcher();
        this.songDispatcher = new SongDispatcher();
        this.songInfo = new SongInfo(myDfs);
        this.user = new User(myDfs);
        this.modifyUser = new ModifyUser(myDfs);
        //this.modifyUser = new ModifyUser();

        dispatcher.registerObject(user, "User");
        dispatcher.registerObject(songDispatcher, "SongServices");
        dispatcher.registerObject(songInfo, "SongInfo");
        dispatcher.registerObject(modifyUser, "ModifyUser");
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

            // Put this section in a thread to run the receive and process the input at the same time
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
            } catch (IOException | java.text.ParseException | ParseException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public DFS getMyDfs() {
        return myDfs;
    }
}
