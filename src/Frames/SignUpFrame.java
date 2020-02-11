package Frames;

import Backend.ModifyUser;
import Backend.User;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class SignUpFrame extends JFrame {

    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel userNameLabel;
    private JTextField userNameField;
    private JLabel dateOfBirthLabel;
    private JComboBox<String> monthBox;
    private JComboBox<Integer> dayBox;
    private JComboBox<Integer> yearBox;
    private JButton signUpButton;
    private JButton backButton;
    private JPanel signUpPanel;
    private JLabel duplicateEmailLabel;
    private JLabel duplicateUsernameLabel;
    private JLabel noEmailEnteredLabel;
    private JLabel noUsernameEnteredLabel;
    private JLabel noPasswordEnteredLabel;
    private JLabel passwordLabel;
    private JTextField passwordField;


    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 450;

    private final String[] months = {"January", "February", "March", "April", "May",
                                        "June", "July", "August", "September",
                                        "October", "November", "December"};

    private final Integer[] days = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,
                                        20,21,22,23,24,25,26,27,28,29,30,31};

    private ArrayList<Integer> years = new ArrayList<>();

    public SignUpFrame() {
        for (int i = 2020; i >= 0; i--) {
            years.add(i);
        }
        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Create Account");
    }

    public void createComponents() {
        signUpPanel = new JPanel();

        emailLabel = new JLabel("Email: ");
        userNameLabel = new JLabel("Name: ");
        passwordLabel = new JLabel("Password: ");
        dateOfBirthLabel = new JLabel("Date of birth: ");
        duplicateEmailLabel = new JLabel("<html><font color='red'>Email already in use!</font></html>");
        duplicateUsernameLabel = new JLabel("<html><font color='red'>Username already in use!</font></html>");
        noEmailEnteredLabel = new JLabel("<html><font color='red'>Please enter an email.</font></html>");
        noUsernameEnteredLabel = new JLabel("<html><font color='red'>Please enter a username.</font></html>");
        noPasswordEnteredLabel = new JLabel("<html><font color='red'>Please enter a password.</font></html>");

        emailField = new JTextField(22);
        emailField.setMaximumSize(emailField.getPreferredSize());
        userNameField = new JTextField(22);
        userNameField.setMaximumSize(userNameField.getPreferredSize());
        passwordField = new JTextField(22);
        passwordField.setMaximumSize(passwordField.getPreferredSize());

        monthBox = new JComboBox<String>(months);
        monthBox.setMaximumSize(monthBox.getPreferredSize());

        dayBox = new JComboBox<Integer>(days);
        dayBox.setMaximumSize(dayBox.getPreferredSize());

        yearBox = new JComboBox<Integer>();
        yearBox.setModel(new DefaultComboBoxModel<Integer>(years.toArray(new Integer[0])));
        yearBox.setMaximumSize(yearBox.getPreferredSize());

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
        emailFieldBox.add(Box.createRigidArea(new Dimension(width, 0)));
        emailFieldBox.add(emailField);
        signUpPanel.add(emailFieldBox);

        Box emailErrorLabelBox = Box.createHorizontalBox();
        emailErrorLabelBox.setPreferredSize(new Dimension(550, 15));
        emailErrorLabelBox.add(Box.createRigidArea(new Dimension(width, 0)));
        emailErrorLabelBox.add(duplicateEmailLabel);
        emailErrorLabelBox.add(noEmailEnteredLabel);
        duplicateEmailLabel.setVisible(false);
        noEmailEnteredLabel.setVisible(false);
        signUpPanel.add(emailErrorLabelBox);

        Box usernameBox = Box.createHorizontalBox();
        usernameBox.setPreferredSize(new Dimension(550, 10));
        usernameBox.add(Box.createRigidArea(new Dimension(width, 0)));
        usernameBox.add(userNameLabel);
        signUpPanel.add(usernameBox);

        Box usernameFieldBox = Box.createHorizontalBox();
        usernameFieldBox.setPreferredSize(new Dimension(550, 25));
        usernameFieldBox.add(Box.createRigidArea(new Dimension(width, 0)));
        usernameFieldBox.add(userNameField);
        usernameFieldBox.add(Box.createRigidArea(new Dimension(width, 0)));
        signUpPanel.add(usernameFieldBox);

        Box usernameErrorLabelBox = Box.createHorizontalBox();
        usernameErrorLabelBox.setPreferredSize(new Dimension(550, 18));
        usernameErrorLabelBox.add(Box.createRigidArea(new Dimension(width, 0)));
        usernameErrorLabelBox.add(duplicateUsernameLabel);
        usernameErrorLabelBox.add(noUsernameEnteredLabel);
        duplicateUsernameLabel.setVisible(false);
        noUsernameEnteredLabel.setVisible(false);
        signUpPanel.add(usernameErrorLabelBox);

        Box passwordBox = Box.createHorizontalBox();
        passwordBox.setPreferredSize(new Dimension(550, 10));
        passwordBox.add(Box.createRigidArea(new Dimension(width, 0)));
        passwordBox.add(passwordLabel);
        signUpPanel.add(passwordBox);

        Box passwordFieldBox = Box.createHorizontalBox();
        passwordFieldBox.setPreferredSize(new Dimension(550, 25));
        passwordFieldBox.add(Box.createRigidArea(new Dimension(width, 0)));
        passwordFieldBox.add(passwordField);
        passwordFieldBox.add(Box.createRigidArea(new Dimension(width, 0)));
        signUpPanel.add(passwordFieldBox);

        Box passwordErrorLabelBox = Box.createHorizontalBox();
        passwordErrorLabelBox.setPreferredSize(new Dimension(550, 18));
        passwordErrorLabelBox.add(Box.createRigidArea(new Dimension(width, 0)));
        passwordErrorLabelBox.add(noPasswordEnteredLabel);
        noPasswordEnteredLabel.setVisible(false);
        signUpPanel.add(passwordErrorLabelBox);

        Box dobLabelBox = Box.createHorizontalBox();
        dobLabelBox.setPreferredSize(new Dimension(550, 10));
        dobLabelBox.add(Box.createRigidArea(new Dimension(width, 0)));
        dobLabelBox.add(dateOfBirthLabel);
        signUpPanel.add(dobLabelBox);

        Box dateBox = Box.createHorizontalBox();
        dateBox.setPreferredSize(new Dimension(550, 28));
        dateBox.add(Box.createRigidArea(new Dimension(width+3, 0)));
        dateBox.add(monthBox);
        dateBox.add(Box.createRigidArea(new Dimension(25, 0)));
        dateBox.add(dayBox);
        dateBox.add(Box.createRigidArea(new Dimension(25, 0)));
        dateBox.add(yearBox);
        signUpPanel.add(dateBox);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.setPreferredSize(new Dimension(550, 30));
        buttonBox.add(Box.createRigidArea(new Dimension(width, 20)));
        buttonBox.add(Box.createRigidArea(new Dimension(50, 0)));
        buttonBox.add(backButton);
        buttonBox.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonBox.add(signUpButton);
        signUpPanel.add(buttonBox);

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
                ModifyUser checkUser = new ModifyUser(userNameField.getText().trim());
                try {
                    if (emailField.getText().trim().length() == 0 && userNameField.getText().trim().length() == 0
                            && passwordField.getText().trim().length() == 0) {
                        duplicateEmailLabel.setVisible(false);
                        duplicateUsernameLabel.setVisible(false);
                        noUsernameEnteredLabel.setVisible(true);
                        noEmailEnteredLabel.setVisible(true);
                        noPasswordEnteredLabel.setVisible(true);
                    }
                    else if (emailField.getText().trim().length() == 0 && userNameField.getText().trim().length() == 0) {
                        duplicateEmailLabel.setVisible(false);
                        duplicateUsernameLabel.setVisible(false);
                        noUsernameEnteredLabel.setVisible(true);
                        noEmailEnteredLabel.setVisible(true);
                        noPasswordEnteredLabel.setVisible(false);
                    }
                    else if (userNameField.getText().trim().length() == 0 && passwordField.getText().trim().length() == 0) {
                        duplicateEmailLabel.setVisible(false);
                        duplicateUsernameLabel.setVisible(false);
                        noUsernameEnteredLabel.setVisible(true);
                        noEmailEnteredLabel.setVisible(false);
                        noPasswordEnteredLabel.setVisible(true);
                    }
                    else if (emailField.getText().trim().length() == 0 && passwordField.getText().trim().length() == 0) {
                        duplicateEmailLabel.setVisible(false);
                        duplicateUsernameLabel.setVisible(false);
                        noUsernameEnteredLabel.setVisible(false);
                        noEmailEnteredLabel.setVisible(true);
                        noPasswordEnteredLabel.setVisible(true);
                    }
                    else if (emailField.getText().trim().length() == 0) {
                        duplicateEmailLabel.setVisible(false);
                        duplicateUsernameLabel.setVisible(false);
                        noUsernameEnteredLabel.setVisible(false);
                        noPasswordEnteredLabel.setVisible(false);
                        noEmailEnteredLabel.setVisible(true);
                    }
                    else if (userNameField.getText().trim().length() == 0) {
                        duplicateUsernameLabel.setVisible(false);
                        duplicateEmailLabel.setVisible(false);
                        noEmailEnteredLabel.setVisible(false);
                        noPasswordEnteredLabel.setVisible(false);
                        noUsernameEnteredLabel.setVisible(true);
                    }
                    else if (passwordField.getText().trim().length() == 0) {
                        duplicateUsernameLabel.setVisible(false);
                        duplicateEmailLabel.setVisible(false);
                        noEmailEnteredLabel.setVisible(false);
                        noUsernameEnteredLabel.setVisible(false);
                    }
                    else if (checkUser.checkDuplicateUser(emailField.getText().trim(),
                            userNameField.getText().trim()).equals("email")) {
                        duplicateUsernameLabel.setVisible(false);
                        noEmailEnteredLabel.setVisible(false);
                        noUsernameEnteredLabel.setVisible(false);
                        duplicateEmailLabel.setVisible(true);

                    }
                    else if (checkUser.checkDuplicateUser(emailField.getText().trim(),
                            userNameField.getText().trim()).equals("username")) {
                        duplicateEmailLabel.setVisible(false);
                        noUsernameEnteredLabel.setVisible(false);
                        noEmailEnteredLabel.setVisible(false);
                        duplicateUsernameLabel.setVisible(true);
                    }
                    else {
                        User newUser = new User(emailField.getText().trim(), userNameField.getText().trim(), passwordField.getText().trim(),
                                (String) monthBox.getSelectedItem(), Integer.toString((int) dayBox.getSelectedItem()),
                                Integer.toString((int)yearBox.getSelectedItem()));
                        MusicFrame musicFrame = new MusicFrame(newUser);
                        setVisible(false);
                        musicFrame.setVisible(true);
                    }
                }
                catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}