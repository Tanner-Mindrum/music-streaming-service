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

    public static void main(String[] args, CommandLine cl) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        CommunicationModule cm = new CommunicationModule();
        Proxy proxy = new Proxy(cm);
        myDfs = cl.dfs;
        CommandLine dfs1 = new CommandLine(2002, 2001);
        CommandLine dfs2 = new CommandLine(2003, 2001);
        CommandLine dfs3 = new CommandLine(2004, 2001);
        cl.start();

        // Spawn initial log in frame
        JFrame frame = new LoginFrame(socket, proxy, myDfs);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
