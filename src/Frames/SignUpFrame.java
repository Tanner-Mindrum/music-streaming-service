package Frames;

import Backend.User;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SignUpFrame extends JFrame {

    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel emailErrorLabel;
    private JLabel userNameLabel;
    private JTextField userNameField;
    private JLabel userNameErrorLabel;
    private JLabel dateOfBirthLabel;
    private JComboBox<String> monthBox;
    private JTextField dayField;
    private JTextField yearField;
    private JButton signUpButton;
    private JButton backButton;
    private JPanel signUpPanel;
    private JLabel duplicateEmailLabel;
    private JLabel duplicateUsernameLabel;


    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 450;

    private final String[] months = {"January", "February", "March", "April", "May",
                                        "June", "July", "August", "September",
                                        "October", "November", "December"};

    public SignUpFrame() {
        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Create Account");
    }

    public void createComponents() {
        signUpPanel = new JPanel();

        emailLabel = new JLabel("Email: ");
        emailErrorLabel = new JLabel("Please enter your email.");
        userNameLabel = new JLabel("Name: ");
        userNameErrorLabel = new JLabel("Please enter your username");
        dateOfBirthLabel = new JLabel("Date of birth: ");
        duplicateEmailLabel = new JLabel("That email already exists!");
        duplicateUsernameLabel = new JLabel("That username already exists!");
        
        emailField = new JTextField(22);
        emailField.setMaximumSize(emailField.getPreferredSize());
        userNameField = new JTextField(22);
        userNameField.setMaximumSize(userNameField.getPreferredSize());

        monthBox = new JComboBox<String>(months);
        monthBox.setMaximumSize(monthBox.getPreferredSize());
        dayField = new JTextField(4);
        dayField.setMaximumSize(dayField.getPreferredSize());
        yearField = new JTextField(7);
        yearField.setMaximumSize(yearField.getPreferredSize());

        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ButtonListener());
        backButton = new JButton("Back");
        backButton.addActionListener(new ButtonListener());

        final int width = 150;

        Box emailBox = Box.createHorizontalBox();
        emailBox.setPreferredSize(new Dimension(550, 10));
        emailBox.add(Box.createRigidArea(new Dimension(width, 0)));
        emailBox.add(emailLabel);
        signUpPanel.add(emailBox);

        Box emailFieldBox = Box.createHorizontalBox();
        emailFieldBox.setPreferredSize(new Dimension(550, 25));
        //usernameLabelBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        emailFieldBox.add(Box.createRigidArea(new Dimension(width, 0)));
        emailFieldBox.add(emailField);
        signUpPanel.add(emailFieldBox);

        Box usernameBox = Box.createHorizontalBox();
        usernameBox.setPreferredSize(new Dimension(550, 10));
        //usernameBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        usernameBox.add(Box.createRigidArea(new Dimension(width, 0)));
        usernameBox.add(userNameLabel);
        signUpPanel.add(usernameBox);

        Box usernameFieldBox = Box.createHorizontalBox();
        usernameFieldBox.setPreferredSize(new Dimension(550, 25));
        //usernameLabelBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        usernameFieldBox.add(Box.createRigidArea(new Dimension(width, 0)));
        usernameFieldBox.add(userNameField);
        signUpPanel.add(usernameFieldBox);

        Box dobLabelBox = Box.createHorizontalBox();
        dobLabelBox.setPreferredSize(new Dimension(550, 10));
        //dobLabelBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        dobLabelBox.add(Box.createRigidArea(new Dimension(width, 0)));
        dobLabelBox.add(dateOfBirthLabel);
        signUpPanel.add(dobLabelBox);

        Box dateBox = Box.createHorizontalBox();
        dateBox.setPreferredSize(new Dimension(550, 30));
        //dateBox.setBorder(BorderFactory.createLineBorder(Color.RED));
        dateBox.add(Box.createRigidArea(new Dimension(width, 0)));
        dateBox.add(monthBox);
        dateBox.add(Box.createRigidArea(new Dimension(10, 0)));
        dateBox.add(dayField);
        dateBox.add(Box.createRigidArea(new Dimension(10, 0)));
        dateBox.add(yearField);
        signUpPanel.add(dateBox);

        signUpPanel.add(duplicateEmailLabel);
        duplicateEmailLabel.setVisible(false);
        signUpPanel.add(duplicateUsernameLabel);
        duplicateUsernameLabel.setVisible(false);
        signUpPanel.add(backButton);
        signUpPanel.add(signUpButton);
        this.add(signUpPanel);
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent click) {
            if (click.getSource() == backButton) {
                LoginFrame loginFrame = new LoginFrame();
                setVisible(false);
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setVisible(true);
            }
            else if (click.getSource() == signUpButton) {
                User checkUser = new User();
                try {
                    if (checkUser.checkDuplicateUser(emailField.getText().trim(),
                            userNameField.getText().trim()).equals("email")) {
                        duplicateUsernameLabel.setVisible(false);
                        duplicateEmailLabel.setVisible(true);

                    }
                    else if (checkUser.checkDuplicateUser(emailField.getText().trim(),
                            userNameField.getText().trim()).equals("username")) {
                        duplicateEmailLabel.setVisible(false);
                        duplicateUsernameLabel.setVisible(true);
                    }
                    else {
                        User newUser = new User(emailField.getText().trim(), userNameField.getText().trim(),
                                (String) monthBox.getSelectedItem(), dayField.getText().trim(),
                                yearField.getText().trim());
                        MusicFrame musicFrame = new MusicFrame(newUser);
                        setVisible(false);
                        musicFrame.setVisible(true);
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}