package Backend;

import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.text.ParseException;

public class CommunicationModule {

    private int port;
    private InetAddress address;
//    private Dispatcher dispatcher;
//    private SongDispatcher songDispatcher;
    DatagramSocket socket;

    public CommunicationModule() throws IOException, org.json.simple.parser.ParseException {
        port = 4445;
        address = InetAddress.getByName("localhost");
        socket = new DatagramSocket();
    }

    public void send(String msg) throws IOException {
        final DatagramSocket socket = new DatagramSocket();
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        socket.close();
    }

    public String sendEcho(String msg, String semantic) throws IOException {
        socket = new DatagramSocket();
        byte[] buf;
        byte[] buff = new byte[12228];
        int attemptCount = 0;
        System.out.println("MSG: " + msg);
        buf = msg.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
        DatagramPacket receivePacket = new DatagramPacket(buff, buff.length);
        System.out.println("Sending msg of size: " + sendPacket.getLength() + "to " + sendPacket.getSocketAddress());
        System.out.println("Listening...");
        socket.send(sendPacket);
        if (semantic.equals("at least once") || semantic.equals("at most once")) {
            while (attemptCount < 10) {
                System.out.println("in loop");
                try {
                    socket.receive(receivePacket);
                    break;
                } catch (SocketTimeoutException e) {
                    attemptCount++;
                    socket.send(sendPacket);
                }
            }
        }
        if (attemptCount == 10) {
            socket.close();
            return "Timed out. Too many attempts.";
        }

        String received = "";
        if (semantic.equals("maybe")) {
            socket.receive(receivePacket);
        }
        received = new String(
                receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println(receivePacket.getLength());
        System.out.println("Close...");
        socket.close();
        System.out.println("FINAL: " + received);
        return received;
    }

    public String receive(String request) throws ParseException, org.json.simple.parser.ParseException, IOException {
        //return dispatcher.dispatch(request);
        return "Nothin";
    }
}
