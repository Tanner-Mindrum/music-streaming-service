package Frames;

import Backend.CommunicationModule;
import Backend.ModifyUser;
import Backend.ServerMain;
import Backend.User;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.UnknownHostException;

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
    private ImageIcon groove;

    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 450;

    private DatagramSocket socket;
    private CommunicationModule comm;


    public LoginFrame(DatagramSocket socket) throws IOException, ParseException {
        comm = new CommunicationModule();
        //System.out.println(comm.sendEcho("hello"));
//        comm.sendEcho("end");
        this.socket = socket;
        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Login");
    }

    public void createComponents() {
        groove = new ImageIcon("imgs/groove2.PNG");
        JLabel imgLabel = new JLabel(groove);
        imgLabel.setHorizontalTextPosition(JLabel.CENTER);

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
        loginPanel.add(Box.createRigidArea(new Dimension(2, 50)));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //loginPanel.add(titleLabel);
        loginPanel.add(imgLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        Box userDoesNotExistBox = Box.createHorizontalBox();
        userDoesNotExistBox.setPreferredSize(new Dimension(550, 20));
        userDoesNotExistBox.add(Box.createRigidArea(new Dimension(185, 20)));
        userDoesNotExistBox.add(userDoesNotExistLabel);
        userDoesNotExistLabel.setVisible(false);
        loginPanel.add(userDoesNotExistBox);

        Box userBox = Box.createHorizontalBox();
        userBox.setPreferredSize(new Dimension(550, 15));
        userBox.add(Box.createRigidArea(new Dimension(217, 0)));
        userBox.add(usernameLabel);
        loginPanel.add(userBox);

        Box userFieldBox = Box.createHorizontalBox();
        userFieldBox.setPreferredSize(new Dimension(550, 20));
        userFieldBox.add(Box.createRigidArea(new Dimension(195, 50)));
        userFieldBox.add(enterUsernameField);
        loginPanel.add(userFieldBox);

        Box noUserBox = Box.createHorizontalBox();
        noUserBox.setPreferredSize(new Dimension(550, 20));
        noUserBox.add(Box.createRigidArea(new Dimension(198, 20)));
        noUserBox.add(noUsernameEnteredLabel);
        noUsernameEnteredLabel.setVisible(false);
        loginPanel.add(noUserBox);

        Box passwordBox = Box.createHorizontalBox();
        passwordBox.setPreferredSize(new Dimension(550, 15));
        passwordBox.add(Box.createRigidArea(new Dimension(217, 0)));
        passwordBox.add(passwordLabel);
        loginPanel.add(passwordBox);

        Box passwordFieldBox = Box.createHorizontalBox();
        passwordFieldBox.setPreferredSize(new Dimension(550, 20));
        passwordFieldBox.add(Box.createRigidArea(new Dimension(195, 0)));
        passwordFieldBox.add(enterPasswordField);
        loginPanel.add(passwordFieldBox);

        Box noPasswordBox = Box.createHorizontalBox();
        noPasswordBox.setPreferredSize(new Dimension(550, 20));
        noPasswordBox.add(Box.createRigidArea(new Dimension(199, 20)));
        noPasswordBox.add(noPasswordEnteredLabel);
        noPasswordEnteredLabel.setVisible(false);
        loginPanel.add(noPasswordBox);

        Box loginBox = Box.createHorizontalBox();
        loginBox.setPreferredSize(new Dimension(550, 50));
        loginBox.add(Box.createRigidArea(new Dimension(244, 50)));
        loginBox.add(loginButton);
        loginPanel.add(loginBox);

        Box noAccBox = Box.createHorizontalBox();
        noAccBox.setPreferredSize(new Dimension(550, 20));
        noAccBox.add(Box.createRigidArea(new Dimension(213, 50)));
        noAccBox.add(noAccLabel);
        loginPanel.add(noAccBox);

        Box signUpBox = Box.createHorizontalBox();
        signUpBox.setPreferredSize(new Dimension(550, 30));
        signUpBox.add(Box.createRigidArea(new Dimension(240, 50)));
        signUpBox.add(signUpButton);
        loginPanel.add(signUpBox);

        this.add(loginPanel);
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent click) {
            if (click.getSource() == loginButton || click.getSource() == enterUsernameField || click.getSource() == enterPasswordField) {

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
                            noUsernameEnteredLabel.setVisible(false);
                            noPasswordEnteredLabel.setVisible(false);
                            userDoesNotExistLabel.setVisible(true);
                        } else {
                            MusicFrame musicFrame = new MusicFrame(enterUsernameField.getText().trim(), socket, comm);
                            setVisible(false);
                            musicFrame.setLocationRelativeTo(null);
                            musicFrame.setVisible(true);
                        }
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (click.getSource() == signUpButton) {
                // Open sign up frame
                SignUpFrame signUpFrame = null;
                try {
                    signUpFrame = new SignUpFrame(socket, comm);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                setVisible(false);
                signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                signUpFrame.setLocationRelativeTo(null);
                signUpFrame.setVisible(true);
            }
        }
    }
}
