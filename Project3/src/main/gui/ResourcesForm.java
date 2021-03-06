package main.gui;

import main.Days;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

public class ResourcesForm {
    public JPanel panel1;
    private boolean editable;
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

    public ResourcesForm(JFrame frame, ProjectGUI gui) {
        mainFrame = frame;
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.setAddResource(resourceNameTextField.getText(), titleText.getText(), skillsText.getText(), Float.parseFloat(wageText.getText()));
                gui.refreshPanels();
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
        editable = edit;

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

        if (!editable) {
            resourceNameTextField.setEditable(false);
            titleText.setEditable(false);
            skillsText.setEditable(false);
            fromTime.setEditable(false);
            toTime.setEditable(false);
            wageText.setEditable(false);
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
                if (editable) {
                    mainFrame.dispose();
                } else {
                    editable = true;
                    resourceNameTextField.setEditable(true);
                    titleText.setEditable(true);
                    skillsText.setEditable(true);
                    fromTime.setEditable(true);
                    toTime.setEditable(true);
                    wageText.setEditable(true);
                    mondayCheckBox.setEnabled(true);
                    tuesdayCheckBox.setEnabled(true);
                    wednesdayCheckBox.setEnabled(true);
                    thursdayCheckBox.setEnabled(true);
                    fridayCheckBox.setEnabled(true);
                    saveButton.setText("Save");
                }
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
