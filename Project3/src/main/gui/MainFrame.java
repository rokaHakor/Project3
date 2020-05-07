package main.gui;

import javax.swing.*;
import java.awt.*;
//Example GUI class that will be expanded upon with additional classes
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Project Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screenSize.width*.6), (int)(screenSize.height*.6));
        setContentPane(new LogIn(this).mainPanel);
    }
}
