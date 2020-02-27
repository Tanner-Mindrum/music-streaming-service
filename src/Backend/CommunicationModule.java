package Backend;

import java.io.IOException;
import java.net.DatagramPacket;

public class CommunicationModule {

    public CommunicationModule() {
    }

    public String send(String msg) throws IOException {
        byte[] buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }


}
