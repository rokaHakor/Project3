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

public class TaskForm {
    public JPanel panel1;
    private boolean editable;
    private JFrame mainFrame;
    private JTextField taskNameText;
    private JButton resourceButton;
    private JButton button3;
    private JButton button4;
    private JTextField descText;
    private JRadioButton taskRadioButton;
    private JRadioButton milestoneRadioButton;
    private JRadioButton summaryRadioButton;
    private JButton cancelButton;
    private JButton saveButton;
    private JButton resource1Button;
    private JButton resource2Button;
    private JButton resource3Button;
    private JButton task1Button;
    private JButton task2Button;
    private JPanel expectedStartDate;
    private JPanel expectedEndDate;
    private JPanel actualStartDate;
    private JPanel actualEndDate;
    private JPanel resourcePanel;
    private JPanel predPanel;
    private JPanel succPanel;
    private JDatePicker expStartPicker;
    private JDatePicker expEndPicker;
    private JDatePicker actStartPicker;
    private JDatePicker actEndPicker;

    public TaskForm(JFrame frame) {
        mainFrame = frame;

        expStartPicker = new JDateComponentFactory().createJDatePicker();
        expStartPicker.setTextEditable(true);
        expStartPicker.setShowYearButtons(true);

        actStartPicker = new JDateComponentFactory().createJDatePicker();
        actStartPicker.setTextEditable(true);
        actStartPicker.setShowYearButtons(true);

        expEndPicker = new JDateComponentFactory().createJDatePicker();
        expEndPicker.setTextEditable(true);
        expEndPicker.setShowYearButtons(true);

        actEndPicker = new JDateComponentFactory().createJDatePicker();
        actEndPicker.setTextEditable(true);
        actEndPicker.setShowYearButtons(true);

        expStartPicker.getModel().setDate(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        expStartPicker.getModel().setSelected(true);

        actStartPicker.getModel().setDate(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        actStartPicker.getModel().setSelected(true);

        expEndPicker.getModel().setDate(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        expEndPicker.getModel().setSelected(true);

        actEndPicker.getModel().setDate(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        actEndPicker.getModel().setSelected(true);

        expectedStartDate.add((JComponent) expEndPicker);
        expectedEndDate.add((JComponent) expStartPicker);
        actualStartDate.add((JComponent) actStartPicker);
        actualEndDate.add((JComponent) actEndPicker);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date expectedStart = Utils.getDateFromPicker(expEndPicker);
                Date expectedEnd = Utils.getDateFromPicker(expStartPicker);
                Date actualStart = Utils.getDateFromPicker(actStartPicker);
                Date actualEnd = Utils.getDateFromPicker(actEndPicker);
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

    public TaskForm(JFrame frame, boolean edit, String name, String desc, String[] resources, String[] predecessors, String[] successors, Date expStart, Date expEnd, Date actStart, Date actEnd) {
        mainFrame = frame;
        editable = edit;

        taskNameText.setText(name);
        descText.setText(desc);
        //TODO implement resources, preds, and succs

        expStartPicker = new JDateComponentFactory().createJDatePicker();
        actStartPicker = new JDateComponentFactory().createJDatePicker();
        expEndPicker = new JDateComponentFactory().createJDatePicker();
        actEndPicker = new JDateComponentFactory().createJDatePicker();

        Calendar cal = Calendar.getInstance();

        cal.setTime(expStart);
        expStartPicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        expStartPicker.getModel().setSelected(true);

        cal.setTime(actStart);
        actStartPicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        actStartPicker.getModel().setSelected(true);

        cal.setTime(expEnd);
        expEndPicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        expEndPicker.getModel().setSelected(true);

        cal.setTime(actEnd);
        actEndPicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        actEndPicker.getModel().setSelected(true);

        expectedStartDate.add((JComponent) expEndPicker);
        expectedEndDate.add((JComponent) expStartPicker);
        actualStartDate.add((JComponent) actStartPicker);
        actualEndDate.add((JComponent) actEndPicker);

        if (!editable) {
            taskNameText.setEditable(false);
            descText.setEditable(false);
            saveButton.setText("Edit");
            cancelButton.setText("Close");
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editable) {
                    Date expectedStart = Utils.getDateFromPicker(expEndPicker);
                    Date expectedEnd = Utils.getDateFromPicker(expStartPicker);
                    Date actualStart = Utils.getDateFromPicker(actStartPicker);
                    Date actualEnd = Utils.getDateFromPicker(actEndPicker);
                    mainFrame.dispose();
                } else {
                    editable = true;
                    taskNameText.setEditable(true);
                    descText.setEditable(true);
                    expEndPicker.setTextEditable(true);
                    expEndPicker.setShowYearButtons(true);
                    expStartPicker.setTextEditable(true);
                    expStartPicker.setShowYearButtons(true);
                    actStartPicker.setTextEditable(true);
                    actStartPicker.setShowYearButtons(true);
                    actEndPicker.setTextEditable(true);
                    actEndPicker.setShowYearButtons(true);
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
