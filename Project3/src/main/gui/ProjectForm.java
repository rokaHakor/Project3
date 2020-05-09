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
                User.createNewProjectButtonClicked(User.getUserID(), nameText.getText(), descText.getText());
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

    public ProjectForm(JFrame frame, JFrame otherFrame) {
        mainFrame = frame;

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otherFrame.getContentPane().remove(otherFrame.getContentPane());
                ProjectGUI proj = new ProjectGUI(otherFrame);
                proj.setProjectName(nameText.getText());
                otherFrame.setContentPane(proj.panel1);
                otherFrame.revalidate();
                otherFrame.repaint();
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
