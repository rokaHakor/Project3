package main.gui;

import javax.swing.*;
import java.awt.*;

public class Deliverables extends JFrame {

    public Deliverables() {
        setTitle("Project Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screenSize.width*.8), (int)(screenSize.height*.8));
    }
}
