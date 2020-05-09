package main.gui;

import main.Days;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

public class ResourcesForm {
    public JPanel panel1;
    private JFrame mainFrame;
    private JTextField resourceNameTextField;
    private JTextField titleText;
    private JTextField skillsText;
    private JComboBox fromTime;
    private JComboBox toTime;
    private JCheckBox mondayCheckBox;
    private JCheckBox tuesdayCheckBox;
    private JCheckBox wednesdayCheckBox;
    private JCheckBox thursdayCheckBox;
    private JCheckBox fridayCheckBox;
    private JTextField wageText;
    private JButton cancelButton;
    private JButton saveButton;

    public ResourcesForm(JFrame frame) {
        mainFrame = frame;
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

    public ResourcesForm(JFrame frame, boolean edit, String name, String title, String skills, int from, int to, EnumSet<Days> availability, float wage) {
        mainFrame = frame;
        resourceNameTextField.setText(name);
        titleText.setText(title);
        skillsText.setText(skills);
        fromTime.setSelectedIndex(from);
        toTime.setSelectedIndex(to);
        wageText.setText("" + wage);
        mondayCheckBox.setSelected(availability.contains(Days.MONDAY));
        tuesdayCheckBox.setSelected(availability.contains(Days.TUESDAY));
        wednesdayCheckBox.setSelected(availability.contains(Days.WEDNESDAY));
        thursdayCheckBox.setSelected(availability.contains(Days.THURSDAY));
        fridayCheckBox.setSelected(availability.contains(Days.FRIDAY));

        if (!edit) {
            resourceNameTextField.setEnabled(false);
            titleText.setEnabled(false);
            skillsText.setEnabled(false);
            fromTime.setEnabled(false);
            toTime.setEnabled(false);
            wageText.setEnabled(false);
            mondayCheckBox.setEnabled(false);
            tuesdayCheckBox.setEnabled(false);
            wednesdayCheckBox.setEnabled(false);
            thursdayCheckBox.setEnabled(false);
            fridayCheckBox.setEnabled(false);
            saveButton.setText("Edit");
            cancelButton.setText("Close");
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
