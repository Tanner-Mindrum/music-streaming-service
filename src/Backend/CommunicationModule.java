package Backend;

import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;

public class CommunicationModule {

    private int port;
    private InetAddress address;
//    private Dispatcher dispatcher;
//    private SongDispatcher songDispatcher;

    public CommunicationModule() throws IOException, org.json.simple.parser.ParseException {
        port = 4445;
        address = InetAddress.getByName("localhost");
    }

    public void send(String msg) throws IOException {
        final DatagramSocket socket = new DatagramSocket();
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        socket.close();
    }

    public String sendEcho(String msg) throws IOException {
        final DatagramSocket socket = new DatagramSocket();
        byte[] buf;
        byte[] buff = new byte[12228];
        System.out.println("MSG: " + msg);
        buf = msg.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
        DatagramPacket receivePacket = new DatagramPacket(buff, buff.length);
        System.out.println("Sending msg of size: " + sendPacket.getLength() + "to " + sendPacket.getSocketAddress());
        System.out.println("Listening...");
        socket.send(sendPacket);
        socket.receive(receivePacket);
        String received = new String(
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
