package Frames;

import Backend.*;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

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
    private static Box songsBox;
    private static JList<String> songList;
    private static JScrollPane scrollPane;
    private static JScrollPane playListPane;
    private JButton createPlaylistButton;
    private JButton deletePlaylistButton;
    private JButton addToPlaylistButton;
    private JButton playSongButton;
    private User currUser;
    private Thread musicThread;
    private Multithread multithread;
    private ArrayList<Songs> foundSongs;
    private SongInfo findSongInfo;

    public MusicFrame(User user) {
        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Home Page");
        currUser = user;
    }

    public void createComponents() {
        MusicFrameListener listener = new MusicFrameListener();
        panel = new JPanel();

       foundSongs = new ArrayList<Songs>();
       findSongInfo = new SongInfo();

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

        createPlaylistButton = new JButton("Create Playlist");
        createPlaylistButton.addActionListener(listener);

        deletePlaylistButton = new JButton("Delete Playlist");
        deletePlaylistButton.addActionListener(listener);

        addToPlaylistButton = new JButton("Add");
        addToPlaylistButton.addActionListener(listener);

        playSongButton = new JButton("Play Song");
        playSongButton.addActionListener(listener);

        // TODO: PREFILL PLAYLIST
        DefaultListModel<String> playListModel = new DefaultListModel<String>();
        JList<String> playListList = new JList<>(playListModel);
        playListPane = new JScrollPane(playListList);

        DefaultListModel<String> songModel = new DefaultListModel<String>();
        JList<String> songsList = new JList<>(songModel);
        scrollPane = new JScrollPane(songsList);

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
        playlistBox.setMaximumSize(playlistBox.getPreferredSize());
        playlistBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        playlistBox.add(playListPane);
        playlistBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        playlistBox.add(createPlaylistButton);
        playlistBox.add(deletePlaylistButton);
        panel.add(playlistBox, BorderLayout.CENTER);

        displaySongArea = new JTextArea(20, 50);

        songsBox = Box.createHorizontalBox();
        songsBox.setPreferredSize(new Dimension(700, 400));
        songsBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        songsBox.add(Box.createRigidArea(new Dimension(150, 300)));
        songsBox.add(scrollPane);
        songsBox.add(addToPlaylistButton);
        songsBox.add(playSongButton);
        panel.add(songsBox);

        this.add(panel);
    }

    class MusicFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == searchButton || e.getSource() == searchField) {
                // Search for song
                try {
                    foundSongs.addAll(findSongInfo.findSong(searchField.getText().trim().toLowerCase()));
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }

                // Loop through our array and add to model
                DefaultListModel<String> model = new DefaultListModel<>();
                for (Songs s : foundSongs) {
                    model.addElement(s.getSongName());
                }
                songList = new JList<>(model);

                songsBox.remove(scrollPane);
                repaint();
                validate();
                scrollPane = new JScrollPane(songList);
                songsBox.add(scrollPane);
                validate();
                repaint();
            }
            else if (e.getSource() == songList) {
                System.out.println("hello");
            }
            else if (e.getSource() == createPlaylistButton) {
                CreatePlaylistFrame pFrame = new CreatePlaylistFrame(currUser);
                pFrame.setVisible(true);
            }
            else if (e.getSource() == addToPlaylistButton) {
                if (songList.isSelectionEmpty()) {
                    System.out.println("bleehh");
                }
                System.out.println(songList.getSelectedValue());
            }
            else if (e.getSource() == playSongButton) {

                if (!songList.isSelectionEmpty()) {
                    String songID = foundSongs.get(songList.getSelectedIndex()).getSongID();

                    multithread = new Multithread();
                    musicThread = new Thread(multithread);
                    multithread.setIdToPlay(songID);
                    musicThread.start();
                }
                else {
                    System.out.println("nothing");
                }
            }
        }
    }
}
