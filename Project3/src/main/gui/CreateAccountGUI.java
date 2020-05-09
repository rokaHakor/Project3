package main.gui;

import main.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class CreateAccountGUI {
    public JPanel panel1;
    private JFrame mainFrame;
    private JTextField emailText;
    private JTextField usernameText;
    private JButton createButton;
    private JButton cancelButton;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JLabel errorLabel;

    public CreateAccountGUI(JFrame frame) {
        mainFrame = frame;
        errorLabel.setVisible(false);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Arrays.equals(passwordField1.getPassword(), passwordField2.getPassword())) {
                    String s = User.createUserButtonClicked(usernameText.getText(), String.valueOf(passwordField1.getPassword()), emailText.getText());
                    if (s.equals("Good")) {
                        mainFrame.dispose();
                    } else {
                        errorLabel.setText(s);
                        errorLabel.setVisible(true);
                    }
                } else {
                    errorLabel.setVisible(true);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
            }
        });
    }
}
