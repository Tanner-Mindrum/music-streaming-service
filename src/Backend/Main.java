package Backend;

import Frames.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // Spawn initial log in frame
        JFrame frame = new LoginFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
