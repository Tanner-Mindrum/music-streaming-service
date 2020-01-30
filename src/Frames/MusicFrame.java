package Frames;

import Backend.Songs;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MusicFrame extends JFrame {
    private JLabel titleLabel;
    private JLabel searchLabel;
    private JButton searchButton;
    private JTextField searchField;
    private static final int FRAME_WIDTH = 1280;
    private static final int FRAME_HEIGHT = 720;
    private JPanel panel;
    private JLabel userNameLabel;
    private String usernameName;
    private JTextArea displaySongArea;

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

        // TODO: LIMIT NAME SIZE
        userNameLabel = new JLabel(String.format("<html><p>%s</p></html>", usernameName));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchField = new JTextField(20);
        searchField.setMaximumSize(searchField.getPreferredSize());
        searchField.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchField.addActionListener(listener);
        //panel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(listener);
        //panel.add(searchButton);

        Box topBox = Box.createHorizontalBox();
        topBox.setPreferredSize(new Dimension(1260, 40));
        topBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        topBox.add(titleLabel);
        topBox.add(Box.createRigidArea(new Dimension(370, 0)));
//        topBox.add(Box.createHorizontalGlue());
        topBox.add(searchField);
        topBox.add(searchButton);
        topBox.add(userNameLabel);
        panel.add(topBox);

        Box playlistBox = Box.createVerticalBox();
        playlistBox.add(new JTextField("Left"));
        playlistBox.setMaximumSize(playlistBox.getPreferredSize());
        playlistBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(playlistBox, BorderLayout.CENTER);

        displaySongArea = new JTextArea(20, 50);

        Box songsBox = Box.createHorizontalBox();
        songsBox.setPreferredSize(new Dimension(700, 400));
        //dobLabelBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        songsBox.add(Box.createRigidArea(new Dimension(150, 300)));
        songsBox.add(displaySongArea);
        panel.add(songsBox);


        this.add(panel);


    }

    class MusicFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == searchButton || e.getSource() == searchField) {
                String inputForSearch = searchField.getText().trim();
                Songs newSong = new Songs();
                try {
                    System.out.println(newSong.findSong(inputForSearch));
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

}
