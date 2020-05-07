package main;

import main.gui.MainFrame;

//Base class that will be run
public class Project3 {

    public static void main(String[] args) {
        System.out.println("Hello World");
        try {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
