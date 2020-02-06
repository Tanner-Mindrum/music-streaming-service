package Frames;

import Backend.ModifyUser;
import Backend.User;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginFrame extends JFrame {

    private JLabel noAccLabel;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JTextField enterUsernameField;
    private JButton loginButton;
    private JButton signUpButton;
    private JLabel userDoesNotExistLabel;

    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 450;


    public LoginFrame() {
        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Login");
    }

    public void createComponents() {

        titleLabel = new JLabel("Music Streaming Service!");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameLabel = new JLabel("Enter your username: ");
        noAccLabel = new JLabel("Don't have an account?");
        userDoesNotExistLabel = new JLabel("Incorrect username.");

        enterUsernameField = new JTextField(15);
        enterUsernameField.setMaximumSize(enterUsernameField.getPreferredSize());
        enterUsernameField.addActionListener(new ButtonListener());

        loginButton = new JButton("Log in");
        loginButton.addActionListener(new ButtonListener());
        signUpButton = new JButton("Sign up");
        signUpButton.addActionListener(new ButtonListener());

        JPanel loginPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(loginPanel, BoxLayout.Y_AXIS);
        loginPanel.setLayout(boxLayout);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        userDoesNotExistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(userDoesNotExistLabel);
        userDoesNotExistLabel.setVisible(false);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(usernameLabel);
        loginPanel.add(enterUsernameField);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        noAccLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(noAccLabel);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(signUpButton);

        this.add(loginPanel);
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent click) {
            if (click.getSource() == loginButton || click.getSource() == enterUsernameField) {

                ModifyUser checkUser = new ModifyUser(enterUsernameField.getText().trim());
                try {
                    if (!checkUser.checkUserExists(enterUsernameField.getText().trim())) {
                        userDoesNotExistLabel.setVisible(true);
                    }
                    else {
                        MusicFrame musicFrame = new MusicFrame(new User(enterUsernameField.getText().trim()));
                        setVisible(false);
                        musicFrame.setVisible(true);
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }
            else if (click.getSource() == signUpButton) {
                // Open sign up frame
                SignUpFrame signUpFrame = new SignUpFrame();
                setVisible(false);
                signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                signUpFrame.setVisible(true);
            }
        }
    }
}
