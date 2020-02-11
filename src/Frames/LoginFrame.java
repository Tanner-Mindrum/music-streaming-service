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
    private JLabel noUsernameEnteredLabel;
    private JLabel passwordLabel;
    private JLabel noPasswordEnteredLabel;
    private JTextField enterUsernameField;
    private JTextField enterPasswordField;
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

        titleLabel = new JLabel("Groovee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        usernameLabel = new JLabel("Enter your username: ");
        passwordLabel = new JLabel("Enter your password: ");
        noAccLabel = new JLabel("Don't have an account?");
        userDoesNotExistLabel = new JLabel("<html><font color='red'>Incorrect username or password.</font></html>");
        noUsernameEnteredLabel = new JLabel("<html><font color='red'>Please enter your username.</font></html>");
        noPasswordEnteredLabel = new JLabel("<html><font color='red'>Please enter your password.</font></html>");

        enterUsernameField = new JTextField(15);
        enterUsernameField.setMaximumSize(enterUsernameField.getPreferredSize());
        enterUsernameField.addActionListener(new ButtonListener());
        enterPasswordField = new JTextField(15);
        enterPasswordField.setMaximumSize(enterPasswordField.getPreferredSize());
        enterPasswordField.addActionListener(new ButtonListener());

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
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        userDoesNotExistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(userDoesNotExistLabel);
        userDoesNotExistLabel.setVisible(false);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(usernameLabel);
        loginPanel.add(enterUsernameField);
        //noUsernameEnteredLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(noUsernameEnteredLabel);
        noUsernameEnteredLabel.setVisible(false);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(passwordLabel);
        loginPanel.add(enterPasswordField);
        //noPasswordEnteredLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(noPasswordEnteredLabel);
        noPasswordEnteredLabel.setVisible(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
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

                if (enterUsernameField.getText().trim().length() == 0 && enterPasswordField.getText().trim().length() == 0) {
                    userDoesNotExistLabel.setVisible(false);
                    noUsernameEnteredLabel.setVisible(true);
                    noPasswordEnteredLabel.setVisible(true);
                } else if (enterUsernameField.getText().trim().length() == 0) {
                    userDoesNotExistLabel.setVisible(false);
                    noPasswordEnteredLabel.setVisible(false);
                    noUsernameEnteredLabel.setVisible(true);
                } else if (enterPasswordField.getText().trim().length() == 0) {
                    userDoesNotExistLabel.setVisible(false);
                    noUsernameEnteredLabel.setVisible(false);
                    noPasswordEnteredLabel.setVisible(true);
                } else {
                    ModifyUser checkUser = new ModifyUser(enterUsernameField.getText().trim());
                    try {
                        if (!checkUser.checkUserExists(enterUsernameField.getText().trim(), enterPasswordField.getText().trim())) {
                            userDoesNotExistLabel.setVisible(true);
                        } else {
                            MusicFrame musicFrame = new MusicFrame(new User(enterUsernameField.getText().trim()));
                            setVisible(false);
                            musicFrame.setVisible(true);
                        }
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
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
