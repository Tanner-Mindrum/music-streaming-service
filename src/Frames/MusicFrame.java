package Frames;

import Backend.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
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
    ArrayList<String> foundSongsStrings = new ArrayList<>();
    ArrayList<Songs> foundFinalSongs = new ArrayList<Songs>();
    ArrayList<String> foundFinalSongsStrings = new ArrayList<>();
    public MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent mouseEvent){
            JList<String> theList = (JList) mouseEvent.getSource();
            if (mouseEvent.getClickCount() == 2){
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0){
                    Object o = theList.getModel().getElementAt(index);
                    try {
                        foundFinalSongs.clear();
                        ArrayList<Songs> playlistSongs = modifyUser.getSongs(o.toString());
                        foundFinalSongs.addAll(playlistSongs);
                        DefaultListModel<String> model = new DefaultListModel<>();
                        for (Songs s : playlistSongs) {
                            model.addElement(s.getSongName() + " | " + s.getArtistName() + " | " + s.getAlbumName());
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
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                    // Refresh songList with playlist songs
                }
            }
        }
    };

    private DatagramSocket socket;
    private InetAddress address;
    private JButton serverTestButton;

    private Proxy proxy;
    private CommunicationModule comm;

    public MusicFrame(User user, DatagramSocket pSocket, CommunicationModule cm) throws IOException, ParseException {
        socket = pSocket;
        comm = cm;
        proxy = new Proxy(comm);
        address = InetAddress.getByName("localhost");
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

        serverTestButton = new JButton("Test server");
        serverTestButton.addActionListener(listener);

        // Playlist Display
        modifyUser = new ModifyUser(currUser.getUsername());
        playlists = modifyUser.getPlaylists();

        DefaultListModel<String> playListModel = new DefaultListModel<String>();
        for (String element : playlists){
            playListModel.addElement(element);
        }
        playListList = new JList<>(playListModel);
        playListList.addMouseListener(mouseListener);
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
        playlistBox.add(serverTestButton);
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
            /*
            When the user clicks the searchButton or presses enter on the searchField
            The song arraylist will be cleared and the found songs will be displayed
            into the list for the user */
            if (e.getSource() == searchButton || e.getSource() == searchField) {
                foundSongsStrings.clear();
                //foundSongs.clear();
                foundFinalSongsStrings.clear();
                //foundFinalSongs.clear();
                // Search for song
                try {
                    //foundSongsStrings.add()
                    JSONObject jsonReturn = proxy.synchExecution("findSong", searchField.getText().trim().toLowerCase());
                    String stringOfSongs = (String) jsonReturn.get("ret");
                    String[] tempSongs = (stringOfSongs).split(",, ");
                    //String[] tempSongs = findSongInfo.findSong(searchField.getText().trim().toLowerCase()).split(",, ");
                    foundSongsStrings.addAll(Arrays.asList(tempSongs));
                    //foundSongs.addAll(findSongInfo.findSong(searchField.getText().trim().toLowerCase()));
                } catch (IOException | ParseException | InterruptedException | java.text.ParseException ex) {
                    ex.printStackTrace();
                }

                // Loop through our array and add to model
                DefaultListModel<String> model = new DefaultListModel<>();
                Set<String> currSongs = new HashSet<String>();
                for (String s : foundSongsStrings) {
                    if (!currSongs.contains(s)) {
                        currSongs.add(s);
                        foundFinalSongsStrings.add(s);
                        //foundFinalSongs.add(s);
                        model.addElement(s.substring(0, s.length() - 19));
                    }
                }
//                for (Songs s : foundSongs) {
//                    if (!currSongs.contains(s.getSongID())) {
//                        currSongs.add(s.getSongID());
//                        foundFinalSongs.add(s);
//                        model.addElement(s.getSongName() + " | " + s.getArtistName() + " | " + s.getAlbumName());
//                    }
//                }
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
            //This opens up a createplaylist frame for users to add a playlist
            else if (e.getSource() == createPlaylistButton) {
                CreatePlaylistFrame2 pFrame = new CreatePlaylistFrame2(currUser);
                pFrame.setLocationRelativeTo(null);
                pFrame.setVisible(true);
            }
            //Allows the user to delete the playlist by clicking on an existing playlist
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
            //Adds a song to the playlist
            else if (e.getSource() == addToPlaylistButton) {
                if (songList != null) {
                    try {
                        //modifyUser.addToPlaylist(songList.getSelectedValue(), playListList.getSelectedValue());
                        Songs songToAdd = (foundFinalSongs.get(songList.getSelectedIndex()));
                        modifyUser.addToPlaylist(songToAdd, playListList.getSelectedValue());
                    } catch (IOException | ParseException ex) {
                        ex.printStackTrace();
                    }
                }
                MouseListener mouseListener = new MouseAdapter() {
                    public void mouseClicked(MouseEvent mouseEvent){
                        JList<String> theList = (JList) mouseEvent.getSource();
                        if (mouseEvent.getClickCount() == 2){
                            int index = theList.locationToIndex(mouseEvent.getPoint());
                            if (index >= 0){
                                Object o = theList.getModel().getElementAt(index);
                                System.out.println("Double clicked on: " + o.toString());

                                // Refresh songList with playlist songs
                            }
                        }
                    }
                };
            }
            //This plays the song when the user clicks playsong
            else if (e.getSource() == playSongButton) {

                if (songList != null && !songList.isSelectionEmpty()) {
                    String songID = foundFinalSongsStrings.get(songList.getSelectedIndex());
                    songID = songID.substring(songID.lastIndexOf(':') + 1);
                    System.out.println(songID);
                    System.out.println("SONGIDDDD: " + songID);
                    //String songID = foundFinalSongs.get(songList.getSelectedIndex()).getSongID();

                    try {
                        multithread = new Multithread(comm);
                    } catch (IOException | ParseException ex) {
                        ex.printStackTrace();
                    }
                    musicThread = new Thread(multithread);
                    multithread.setIdToPlay(songID);
                    musicThread.start();
                }
            }
            // This logs the user out of the musicframe
            else if (e.getSource() == m1) {
                LoginFrame loginFrame = null;
                try {
                    loginFrame = new LoginFrame(socket);
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }
                setVisible(false);
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            }
            else if (e.getSource() == stopButton) {
                if (songList != null && !songList.isSelectionEmpty()) {
                    multithread.stopSong();
                }
            }
            else if (e.getSource() == serverTestButton) {
                String echo = null;
                try {
                    echo = send("hello server");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println("test: " + echo);
                try {
                    echo = send("server is working");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println("test 2: " + echo);
            }
        } // end actionlistener method
    } // End listener class

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

    public void close() {
        socket.close();
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

    /**
     * Whenever the user adds a new playlist, it repaints the frame
     * to display the new added playlist
     * @throws IOException
     * @throws ParseException
     */
    public void refreshPlaylistList() throws IOException, ParseException {

        playlists.clear();
        playlists.addAll(modifyUser.getPlaylists());

        DefaultListModel<String> playListModel = new DefaultListModel<String>();
        for (String element : playlists){
            playListModel.addElement(element);
        }
        playListList = new JList<>(playListModel);
        playListList.addMouseListener(mouseListener);

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
