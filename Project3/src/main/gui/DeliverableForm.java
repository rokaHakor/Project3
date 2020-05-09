package main.gui;

import main.Utils;
import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class DeliverableForm {
    public JPanel panel1;
    private boolean editable;
    private JFrame mainFrame;
    private JTextField descText;
    private JButton button1;
    private JButton req1Button;
    private JButton req2Button;
    private JButton req3Button;
    private JButton button2;
    private JButton task1Button;
    private JButton task2Button;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField deliverableNameText;
    private JButton task3Button;
    private JPanel datePanel;
    private JDatePicker dueDatePicker;

    public DeliverableForm(JFrame frame, ProjectGUI gui) {
        mainFrame = frame;

        dueDatePicker = new JDateComponentFactory().createJDatePicker();
        dueDatePicker.setTextEditable(true);
        dueDatePicker.setShowYearButtons(true);

        dueDatePicker.getModel().setDate(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dueDatePicker.getModel().setSelected(true);

        datePanel.add((JComponent) dueDatePicker);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date dueDate = Utils.getDateFromPicker(dueDatePicker);
                gui.setAddDeliverable(deliverableNameText.getText(), descText.getText(), dueDate);
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

    public DeliverableForm(JFrame frame, boolean edit, String name, String desc, String[] reqs, String[] tasks, Date due) {
        mainFrame = frame;
        editable = edit;

        deliverableNameText.setText(name);
        descText.setText(desc);
        //TODO implement Reqs and Tasks

        dueDatePicker = new JDateComponentFactory().createJDatePicker();
        Calendar cal = Calendar.getInstance();

        cal.setTime(due);
        dueDatePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dueDatePicker.getModel().setSelected(true);

        datePanel.add((JComponent) dueDatePicker);

        if (!editable) {
            deliverableNameText.setEditable(false);
            descText.setEditable(false);
            saveButton.setText("Edit");
            cancelButton.setText("Close");
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editable) {
                    Date dueDate = Utils.getDateFromPicker(dueDatePicker);
                    mainFrame.dispose();
                } else {
                    editable = true;
                    deliverableNameText.setEditable(true);
                    descText.setEditable(true);
                    dueDatePicker.setTextEditable(true);
                    dueDatePicker.setShowYearButtons(true);
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
