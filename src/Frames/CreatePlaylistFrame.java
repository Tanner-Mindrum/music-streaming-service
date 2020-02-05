package Frames;

import Backend.ModifyUser;
import Backend.User;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CreatePlaylistFrame extends JFrame {

    private JLabel playlistNameLabel;
    private JTextField playListNameField;
    private JButton addButton;
    private JPanel playPanel;
    private User currUser;

    private static final int FRAME_WIDTH = 450;
    private static final int FRAME_HEIGHT = 200;


    public CreatePlaylistFrame(User user) {
        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Login");
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setVisible(false);
            }
        }

    }

}
