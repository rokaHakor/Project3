package main.gui;

import main.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogIn {
    public JPanel mainPanel;
    private JTextField userText;
    private JButton loginButton;
    private JPasswordField passwordField1;
    private JButton forgotPasswordButton;
    private JButton createAccountButton;
    private JLabel errorLabel;

    public LogIn(JFrame frame) {
        errorLabel.setVisible(false);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = User.loginButtonClicked(userText.getText(), String.valueOf(passwordField1.getPassword()));
                if (value.equals("Good")) {
                    frame.getContentPane().remove(frame.getContentPane());
                    frame.setContentPane(new ProjectsPage().panel1);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    errorLabel.setText(value);
                    errorLabel.setVisible(true);
                }
            }
        });
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame createAccountForm = new JFrame();
                createAccountForm.setTitle("Create Account");
                createAccountForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                createAccountForm.setContentPane(new CreateAccountGUI(createAccountForm).panel1);
                createAccountForm.pack();
                createAccountForm.setVisible(true);
            }
        });
    }
}
