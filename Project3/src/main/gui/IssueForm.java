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

public class IssueForm implements HasComboBoxes {
    public JPanel panel1;
    private boolean editable;
    private JFrame mainFrame;
    private JTextField issueNameText;
    private JTextField descText;
    private JComboBox severityCombo;
    private JComboBox priorityCombo;
    private JButton cancelButton;
    private JButton saveButton;
    private JPanel raisedDatePanel;
    private JPanel assignedDatePanel;
    private JButton priorityButton;
    private JButton severityButton;
    private JComboBox statusCombo;
    private JButton statusButton;
    private JLabel updateDate;
    private JDatePicker raisedDatePicker;
    private JDatePicker assignedDatePicker;


    public IssueForm(JFrame frame) {
        mainFrame = frame;

        //TODO medthod to get defaults and fill comboboxes

        raisedDatePicker = new JDateComponentFactory().createJDatePicker();
        raisedDatePicker.setTextEditable(true);
        raisedDatePicker.setShowYearButtons(true);

        assignedDatePicker = new JDateComponentFactory().createJDatePicker();
        assignedDatePicker.setTextEditable(true);
        assignedDatePicker.setShowYearButtons(true);

        raisedDatePicker.getModel().setDate(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        raisedDatePicker.getModel().setSelected(true);

        assignedDatePicker.getModel().setDate(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        assignedDatePicker.getModel().setSelected(true);

        raisedDatePanel.add((JComponent) raisedDatePicker);
        assignedDatePanel.add((JComponent) assignedDatePicker);

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("d MMMM yyyy");
        updateDate.setText(format.format(now));

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date raisedDate = Utils.getDateFromPicker(raisedDatePicker);
                Date assignedDate = Utils.getDateFromPicker(assignedDatePicker);
                //TODO input values into DB
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
                statusDefaults.setContentPane(new DefaultsForm(statusDefaults, IssueForm.this, "Status").panel1);
                statusDefaults.pack();
                statusDefaults.setVisible(true);
            }
        });
        priorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame priorityDefaults = new JFrame();
                priorityDefaults.setTitle("Priority Defaults");
                priorityDefaults.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                priorityDefaults.setContentPane(new DefaultsForm(priorityDefaults, IssueForm.this, "Priority").panel1);
                priorityDefaults.pack();
                priorityDefaults.setVisible(true);
            }
        });
        severityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame severityDefaults = new JFrame();
                severityDefaults.setTitle("Severity Defaults");
                severityDefaults.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                severityDefaults.setContentPane(new DefaultsForm(severityDefaults, IssueForm.this, "Severity").panel1);
                severityDefaults.pack();
                severityDefaults.setVisible(true);
            }
        });
    }

    public IssueForm(JFrame frame, boolean edit, String name, String desc, String status, Date raised, Date assigned, Date updated, String priority, String severity) {
        mainFrame = frame;
        editable = edit;

        issueNameText.setText(name);
        descText.setText(desc);
        //TODO method to get defaults and fill comboboxes
        statusCombo.setSelectedItem(status);
        priorityCombo.setSelectedItem(priority);
        severityCombo.setSelectedItem(severity);

        raisedDatePicker = new JDateComponentFactory().createJDatePicker();
        assignedDatePicker = new JDateComponentFactory().createJDatePicker();

        Calendar cal = Calendar.getInstance();
        cal.setTime(raised);

        raisedDatePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        raisedDatePicker.getModel().setSelected(true);

        cal.setTime(assigned);

        assignedDatePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        assignedDatePicker.getModel().setSelected(true);

        raisedDatePanel.add((JComponent) raisedDatePicker);
        assignedDatePanel.add((JComponent) assignedDatePicker);

        SimpleDateFormat format = new SimpleDateFormat("d MMMM yyyy");
        updateDate.setText(format.format(updated));

        if (!editable) {
            issueNameText.setEditable(false);
            descText.setEditable(false);
            statusCombo.setEditable(false);
            priorityCombo.setEditable(false);
            severityCombo.setEditable(false);
            saveButton.setText("Edit");
            cancelButton.setText("Close");
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editable) {
                    Date raisedDate = Utils.getDateFromPicker(raisedDatePicker);
                    Date assignedDate = Utils.getDateFromPicker(assignedDatePicker);
                    //TODO input values into DB
                    mainFrame.dispose();
                } else {
                    editable = true;
                    issueNameText.setEditable(true);
                    descText.setEditable(true);
                    statusCombo.setEditable(true);
                    priorityCombo.setEditable(true);
                    severityCombo.setEditable(true);
                    raisedDatePicker.setTextEditable(true);
                    raisedDatePicker.setShowYearButtons(true);
                    assignedDatePicker.setTextEditable(true);
                    assignedDatePicker.setShowYearButtons(true);
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
                statusDefaults.setContentPane(new DefaultsForm(statusDefaults, IssueForm.this, "Status").panel1);
                statusDefaults.pack();
                statusDefaults.setVisible(true);
            }
        });
        priorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame priorityDefaults = new JFrame();
                priorityDefaults.setTitle("Priority Defaults");
                priorityDefaults.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                priorityDefaults.setContentPane(new DefaultsForm(priorityDefaults, IssueForm.this, "Priority").panel1);
                priorityDefaults.pack();
                priorityDefaults.setVisible(true);
            }
        });
        severityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame severityDefaults = new JFrame();
                severityDefaults.setTitle("Severity Defaults");
                severityDefaults.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                severityDefaults.setContentPane(new DefaultsForm(severityDefaults, IssueForm.this, "Severity").panel1);
                severityDefaults.pack();
                severityDefaults.setVisible(true);
            }
        });
    }

    public void renewComboBoxes() {

    }
}
