package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicFrame extends JFrame {
    private JLabel titleLabel;
    private JLabel searchLabel;
    private JButton searchButton;
    private JTextField searchField;
    private static final int FRAME_WIDTH = 1280;
    private static final int FRAME_HEIGHT = 720;
    private JPanel panel;

    public MusicFrame() {

        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Home Page");
    }

    public void createComponents() {
        MusicFrameListener listener = new MusicFrameListener();
        panel = new JPanel();

        //BoxLayout boxLayout = new BoxLayout(panel, new FlowLayout());
        panel.setLayout(new FlowLayout());

        titleLabel = new JLabel("Ghetto Spotify");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //panel.add(titleLabel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchField = new JTextField(20);
        searchField.setMaximumSize(searchField.getPreferredSize());
        searchField.setAlignmentX(Component.CENTER_ALIGNMENT);
        //panel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(listener);
        //panel.add(searchButton);

        Box topBox = Box.createHorizontalBox();
        topBox.setPreferredSize(new Dimension(1260, 80));
        topBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        topBox.add(titleLabel);
        topBox.add(searchField);
        topBox.add(searchButton);
        panel.add(topBox);

        Box playlistBox = Box.createVerticalBox();
        playlistBox.add(new JTextField("Left"));
        playlistBox.setMaximumSize(playlistBox.getPreferredSize());
        playlistBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(playlistBox, BorderLayout.CENTER);

        this.add(panel);


    }

    class MusicFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == searchButton) {
                System.out.println("Search button was clicked");
            }
        }
    }

}
