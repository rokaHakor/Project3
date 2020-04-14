package main.gui;

import javax.swing.*;
import java.awt.*;

public class Deliverables extends JFrame {

    public Deliverables() {
        setTitle("Project Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.add(Box.createVerticalGlue());
        startPanel.setPreferredSize(new Dimension(500, 600));
        setContentPane(startPanel);
        pack();
    }
}
