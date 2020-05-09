package main.gui;

import main.Utils;
import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class ActionItemForm implements HasComboBoxes{
    public JPanel panel1;
    private boolean editable;
    private JFrame mainFrame;
    private JTextField descText;
    private JButton resourceButton;
    private JButton neliButton;
    private JButton jamesButton;
    private JButton bezanButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel expectedDatePanel;
    private JTextField actionItemNameText;
    private JPanel actualDatePanel;
    private JLabel createdDate;
    private JLabel updatedDate;
    private JComboBox statusCombo;
    private JButton statusButton;
    private JDatePicker expectedDatePicker;
    private JDatePicker actualDatePicker;


    public ActionItemForm(JFrame frame) {
        mainFrame = frame;

        expectedDatePicker = new JDateComponentFactory().createJDatePicker();
        expectedDatePicker.setTextEditable(true);
        expectedDatePicker.setShowYearButtons(true);

        actualDatePicker = new JDateComponentFactory().createJDatePicker();
        actualDatePicker.setTextEditable(true);
        actualDatePicker.setShowYearButtons(true);

        expectedDatePicker.getModel().setDate(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        expectedDatePicker.getModel().setSelected(true);

        actualDatePicker.getModel().setDate(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        actualDatePicker.getModel().setSelected(true);

        expectedDatePanel.add((JComponent) expectedDatePicker);
        actualDatePanel.add((JComponent) actualDatePicker);

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("d MMMM yyyy");
        createdDate.setText(format.format(now));
        updatedDate.setText(format.format(now));

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date expectedDate = Utils.getDateFromPicker(expectedDatePicker);
                Date actualDate = Utils.getDateFromPicker(actualDatePicker);
                mainFrame.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
            }
        });
        statusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame statusDefaults = new JFrame();
                statusDefaults.setTitle("Status Defaults");
                statusDefaults.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                statusDefaults.setContentPane(new DefaultsForm(statusDefaults, ActionItemForm.this, "Status").panel1);
                statusDefaults.pack();
                statusDefaults.setVisible(true);
            }
        });
    }

    public ActionItemForm(JFrame frame, boolean edit, String name, String description, String[] resources, String status, Date created, Date expected, Date actual, Date update) {
        mainFrame = frame;
        editable = edit;

        actionItemNameText.setText(name);
        descText.setText(description);
        //TODO implement resources
        statusCombo.setSelectedItem(status);

        expectedDatePicker = new JDateComponentFactory().createJDatePicker();
        actualDatePicker = new JDateComponentFactory().createJDatePicker();

        Calendar cal = Calendar.getInstance();

        cal.setTime(expected);
        expectedDatePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        expectedDatePicker.getModel().setSelected(true);

        cal.setTime(actual);
        actualDatePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        actualDatePicker.getModel().setSelected(true);

        expectedDatePanel.add((JComponent) expectedDatePicker);
        actualDatePanel.add((JComponent) actualDatePicker);

        SimpleDateFormat format = new SimpleDateFormat("d MMMM yyyy");
        createdDate.setText(format.format(created));
        updatedDate.setText(format.format(update));

        if (!editable) {
            actionItemNameText.setEditable(false);
            descText.setEditable(false);
            statusCombo.setEditable(false);
            statusButton.setEnabled(false);
            saveButton.setText("Edit");
            cancelButton.setText("Close");
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editable) {
                    Date expectedDate = Utils.getDateFromPicker(expectedDatePicker);
                    Date actualDate = Utils.getDateFromPicker(actualDatePicker);
                    mainFrame.dispose();
                } else {
                    editable = true;
                    actionItemNameText.setEditable(true);
                    descText.setEditable(true);
                    statusCombo.setEditable(true);
                    statusButton.setEnabled(true);
                    expectedDatePicker.setTextEditable(true);
                    expectedDatePicker.setShowYearButtons(true);
                    actualDatePicker.setTextEditable(true);
                    actualDatePicker.setShowYearButtons(true);
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
        statusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame statusDefaults = new JFrame();
                statusDefaults.setTitle("Status Defaults");
                statusDefaults.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                statusDefaults.setContentPane(new DefaultsForm(statusDefaults, ActionItemForm.this, "Status").panel1);
                statusDefaults.pack();
                statusDefaults.setVisible(true);
            }
        });
    }

    @Override
    public void renewComboBoxes() {

    }
}
