package Backend;

import Frames.LoginFrame;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {
    private DatagramSocket socket;
    private static DFS myDfs;

    public static void main(String[] args, DFS dfs) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        CommunicationModule cm = new CommunicationModule();
        Proxy proxy = new Proxy(cm);
        myDfs = dfs;

        // Spawn initial log in frame
        JFrame frame = new LoginFrame(socket, proxy, myDfs);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
