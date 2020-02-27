package Backend;

import Frames.LoginFrame;

import javax.swing.*;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Main {
    private DatagramSocket socket;

    public static void main(String[] args) throws SocketException {
        DatagramSocket socket = new DatagramSocket();

        // Spawn initial log in frame
        JFrame frame = new LoginFrame(socket, dispatcher);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
