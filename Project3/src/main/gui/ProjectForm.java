package main.gui;

import main.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectForm {
    public JPanel panel1;
    private JFrame mainFrame;
    private JTextField nameText;
    private JTextField descText;
    private JButton cancelButton;
    private JButton createButton;

    public ProjectForm(JFrame frame) {
        mainFrame = frame;

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.createNewProjectButtonClicked(1, nameText.getText(), descText.getText());
                mainFrame.dispose();
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
