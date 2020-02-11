package Frames;

import Backend.*;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private static Box playlistBox;
    private static JList<String> songList;
    private static JList<String> playListList;
    private static JScrollPane scrollPane;
    private static JScrollPane playListPane;
    private JButton createPlaylistButton;
    private JButton deletePlaylistButton;
    private JButton addToPlaylistButton;
    private JButton playSongButton;
    private User currUser;
    private Thread musicThread;
    private Multithread multithread;
    private ModifyUser modifyUser;
    private ArrayList<String> playlists;
    DefaultListModel<String> songModel;
    private JMenuBar userMenuBar;
    private JMenu userMenu;
    private JMenuItem m1;
    private JButton stopButton;
    ArrayList<Songs> foundSongs = new ArrayList<Songs>();
    ArrayList<Songs> foundFinalSongs = new ArrayList<Songs>();

    public MusicFrame(User user) throws IOException, ParseException {
        currUser = user;
        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Home Page");
    }

    public void createComponents() throws IOException, ParseException {
        MusicFrameListener listener = new MusicFrameListener();
        panel = new JPanel();

//       foundSongs = new ArrayList<Songs>();

        //BoxLayout boxLayout = new BoxLayout(panel, new FlowLayout());
        panel.setLayout(new FlowLayout());

        titleLabel = new JLabel("Groovee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //panel.add(titleLabel);

        // TODO: LIMIT NAME SIZE & Format in top bar
        userNameLabel = new JLabel(String.format("<html><p>%s</p></html>", currUser.getUsername()));

        userMenuBar = new JMenuBar();
        userMenu = new JMenu(String.format("<html><p>%s</p></html>", currUser.getUsername()));
        m1 = new JMenuItem("Log out");
        m1.addActionListener(listener);
        userMenu.add(m1);
        userMenuBar.add(userMenu);
        userMenuBar.setBorderPainted(false);

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

        stopButton = new JButton("Stop Song");
        stopButton.addActionListener(listener);

        // TODO: Re-Fill Playlists after adding or deleting one
        // Playlist Display
        modifyUser = new ModifyUser(currUser.getUsername());
        playlists = modifyUser.getPlaylists();

        DefaultListModel<String> playListModel = new DefaultListModel<String>();
        for (String element : playlists){
            playListModel.addElement(element);
        }
        playListList = new JList<>(playListModel);
        playListPane = new JScrollPane(playListList);

        songModel = new DefaultListModel<String>();
        JList<String> songsList = new JList<>(songModel);
        scrollPane = new JScrollPane(songsList);

        Box topBox = Box.createHorizontalBox();
        topBox.setPreferredSize(new Dimension(1260, 40));
        //topBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        topBox.add(Box.createRigidArea(new Dimension(25, 0)));
        topBox.add(titleLabel);
        topBox.add(Box.createRigidArea(new Dimension(400, 0)));
        topBox.add(searchField);
        topBox.add(searchButton);
        topBox.add(Box.createRigidArea(new Dimension(330, 0)));
        topBox.add(userMenuBar);
        panel.add(topBox);

        playlistBox = Box.createVerticalBox();
        playlistBox.setPreferredSize(new Dimension(300, 500));
        playlistBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        playlistBox.add(playListPane);
        //playlistBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        playlistBox.add(createPlaylistButton);
        playlistBox.add(deletePlaylistButton);
        panel.add(playlistBox, BorderLayout.CENTER);

        displaySongArea = new JTextArea(20, 50);

        songsBox = Box.createHorizontalBox();
        songsBox.setPreferredSize(new Dimension(800, 500));
        //songsBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        songsBox.add(Box.createRigidArea(new Dimension(150, 300)));
        songsBox.add(scrollPane);
        songsBox.add(addToPlaylistButton);
        songsBox.add(playSongButton);
        songsBox.add(stopButton);
        panel.add(songsBox);

        this.add(panel);
    }

    class MusicFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SongInfo findSongInfo = new SongInfo();
            if (e.getSource() == searchButton || e.getSource() == searchField) {
                foundSongs.clear();
                foundFinalSongs.clear();
                // Search for song
                try {
                    foundSongs.addAll(findSongInfo.findSong(searchField.getText().trim().toLowerCase()));
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }

                // Loop through our array and add to model
                DefaultListModel<String> model = new DefaultListModel<>();
                Set<String> currSongs = new HashSet<String>();
                for (Songs s : foundSongs) {
                    if (!currSongs.contains(s.getSongName())) {
                        currSongs.add(s.getSongName());
                        foundFinalSongs.add(s);
                        model.addElement(s.getSongName());
                    }
                }
                songList = new JList<>(model);

                songsBox.remove(scrollPane);
                repaint();
                validate();
                scrollPane = new JScrollPane(songList);
                songsBox.add(scrollPane);
                songsBox.add(addToPlaylistButton);
                songsBox.add(playSongButton);
                songsBox.add(stopButton);
                validate();
                repaint();
            }
            else if (e.getSource() == songList) {
                System.out.println("hello");
            }
            else if (e.getSource() == createPlaylistButton) {
                CreatePlaylistFrame2 pFrame = new CreatePlaylistFrame2(currUser);
                pFrame.setVisible(true);
            }
            else if (e.getSource() == deletePlaylistButton){
                if (!playListList.isSelectionEmpty()) {
                    try {
                        modifyUser.deletePlaylist(playListList.getSelectedValue());
                        refreshPlaylistList();
                    } catch (IOException | ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            else if (e.getSource() == addToPlaylistButton) {
                if (songList == null) {
                }
            }
            else if (e.getSource() == playSongButton) {

                if (songList != null && !songList.isSelectionEmpty()) {
                    String songID = foundFinalSongs.get(songList.getSelectedIndex()).getSongID();

                    multithread = new Multithread();
                    musicThread = new Thread(multithread);
                    multithread.setIdToPlay(songID);
                    musicThread.start();
                }
            }
            else if (e.getSource() == m1) {
                LoginFrame loginFrame = new LoginFrame();
                setVisible(false);
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setVisible(true);
            }
            else if (e.getSource() == stopButton) {
                if (songList != null && !songList.isSelectionEmpty()) {
                    multithread.stopSong();
                }
            }
        }
    }

    class CreatePlaylistFrame2 extends JFrame {
        private JLabel playlistNameLabel;
        private JTextField playListNameField;
        private JButton addButton;
        private JPanel playPanel;
        private User currUser;

        private static final int FRAME_WIDTH = 450;
        private static final int FRAME_HEIGHT = 200;


        public CreatePlaylistFrame2(User user) {
            createComponents();
            setSize(FRAME_WIDTH, FRAME_HEIGHT);
            this.setTitle("Add playlist");
            currUser = user;
        }

        public void createComponents() {


            playlistNameLabel = new JLabel("Playlist name: ");

            playListNameField = new JTextField(10);
            playListNameField.addActionListener(new ButtonListener());

            addButton = new JButton("Add");
            addButton.addActionListener(new ButtonListener());

            playPanel = new JPanel();
            playPanel.add(playlistNameLabel);
            playPanel.add(playListNameField);
            playPanel.add(addButton);
            this.add(playPanel);
        }

        class ButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent click) {
                if (click.getSource() == addButton || click.getSource() == playListNameField) {
                    ModifyUser mu = new ModifyUser(currUser.getUsername());
                    try {
                        mu.createPlaylist(playListNameField.getText().trim());
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        refreshPlaylistList();
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                    setVisible(false);
                }
            }
        }
    }

    public void refreshPlaylistList() throws IOException, ParseException {

        playlists.clear();
        playlists.addAll(modifyUser.getPlaylists());

        DefaultListModel<String> playListModel = new DefaultListModel<String>();
        for (String element : playlists){
            playListModel.addElement(element);
        }
        playListList = new JList<>(playListModel);

        playlistBox.remove(playListPane);
        repaint();
        validate();
        playListPane = new JScrollPane(playListList);
        playlistBox.add(playListPane);
        playlistBox.add(createPlaylistButton);
        playlistBox.add(deletePlaylistButton);
        validate();
        repaint();
    }
}
