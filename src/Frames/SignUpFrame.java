package Frames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpFrame extends JFrame {

    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel userNameLabel;
    private JTextField userNameField;
    private JLabel dateOfBirthLabel;
    private JComboBox<String> monthBox;
    private JTextField dayField;
    private JTextField yearField;
    private JButton signUpButton;
    private JButton backButton;

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
        emailLabel = new JLabel("Email: ");
        userNameLabel = new JLabel("Name: ");
        dateOfBirthLabel = new JLabel("Date of birth: ");

        emailField = new JTextField(15);
        emailField.setMaximumSize(emailField.getPreferredSize());
        userNameField = new JTextField(15);
        userNameField.setMaximumSize(userNameField.getPreferredSize());

        monthBox = new JComboBox<String>(months);
        monthBox.setMaximumSize(monthBox.getPreferredSize());
        dayField = new JTextField(15);
        dayField.setMaximumSize(dayField.getPreferredSize());
        yearField = new JTextField(15);
        yearField.setMaximumSize(yearField.getPreferredSize());

        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ButtonListener());
        backButton = new JButton("Back");
        backButton.addActionListener(new ButtonListener());

        JPanel signUpPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(signUpPanel, BoxLayout.Y_AXIS);
        signUpPanel.setLayout(boxLayout);
        signUpPanel.add(emailLabel);
        signUpPanel.add(emailField);
        signUpPanel.add(userNameLabel);
        signUpPanel.add(userNameField);
        signUpPanel.add(dateOfBirthLabel);
        signUpPanel.add(monthBox);
        signUpPanel.add(dayField);
        signUpPanel.add(yearField);
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
                

            }
        }
    }


}
