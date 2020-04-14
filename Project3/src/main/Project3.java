package main;

import main.gui.Deliverables;

public class Project3 {

    public static void main(String[] args) {
        System.out.println("Hello World");
        try {
            Deliverables frame = new Deliverables();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
