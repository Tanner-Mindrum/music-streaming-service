package Backend;

import Frames.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        /* Change path for testing
         *  Tanner's path: "C://CECS 327//music-streaming-service//imperial.mp3"
         */
        //player.mp3play("C://CECS 327//music-streaming-service//imperial.mp3");

        JFrame frame = new LoginFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
