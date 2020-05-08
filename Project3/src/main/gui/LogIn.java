package main.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogIn {
    public JPanel mainPanel;
    private JTextField textField1;
    private JButton loginButton;
    private JPasswordField passwordField1;
    private JButton forgotPasswordButton;

    public LogIn(JFrame frame) {

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().remove(frame.getContentPane());
                frame.setContentPane(new ProjectGUI().panel1);
                frame.revalidate();
                frame.repaint();
            }
        });
    }
}
