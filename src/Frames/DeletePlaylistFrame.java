package Frames;

import Backend.ModifyUser;
import Backend.User;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DeletePlaylistFrame extends JFrame {

    private JLabel playlistNameLabel;
    private JTextField playListNameField;
    private JButton deleteButton;
    private JPanel playPanel;
    private User currUser;

    private static final int FRAME_WIDTH = 450;
    private static final int FRAME_HEIGHT = 200;


    public DeletePlaylistFrame(User user) {
        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Delete Playlist");
        currUser = user;
    }

    public void createComponents() {
        playlistNameLabel = new JLabel("Playlist name: ");

        playListNameField = new JTextField(10);
        playListNameField.addActionListener(new ButtonListener());

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ButtonListener());

        playPanel = new JPanel();
        playPanel.add(playlistNameLabel);
        playPanel.add(playListNameField);
        playPanel.add(deleteButton);
        this.add(playPanel);
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent click) {
            if (click.getSource() == deleteButton || click.getSource() == playListNameField) {
                ModifyUser mu = new ModifyUser(currUser.getUsername());
                try {
                    mu.deletePlaylist(playListNameField.getText().trim());
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
