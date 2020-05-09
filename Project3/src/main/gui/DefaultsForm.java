package main.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefaultsForm {
    public JPanel panel1;
    private JTextArea textArea;
    private JButton cancelButton;
    private JButton saveButton;
    private JLabel description;
    private JFrame frame;

    public DefaultsForm(JFrame frame,HasComboBoxes boxes, String type) {
        description.setText(type + " Defaults (One line per Item):");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO Save to DB
                getDefaults();
                boxes.renewComboBoxes();
                frame.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    private String getDefaults(){
        return textArea.getText().replaceAll("\n", ", ");
    }
}
