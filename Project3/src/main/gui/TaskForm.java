package main.gui;

import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;
import java.util.Calendar;

public class TaskForm {
    public JPanel panel1;
    private JFrame mainFrame;
    private JTextField taskNameTextField;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JTextField textField1;
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
    private JDatePicker expStartPicker;
    private JDatePicker expEndPicker;
    private JDatePicker actStartPicker;
    private JDatePicker actEndPicker;

    public TaskForm(JFrame frame) {
        mainFrame = frame;

        expStartPicker = new JDateComponentFactory().createJDatePicker();
        expStartPicker.setTextEditable(true);
        expStartPicker.setShowYearButtons(true);

        expStartPicker.getModel().setYear(Year.now().getValue());
        expStartPicker.getModel().setMonth(Calendar.getInstance().get(Calendar.MONTH));
        expStartPicker.getModel().setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        expStartPicker.getModel().setSelected(true);

        expectedEndDate.add((JComponent) expStartPicker);

        actStartPicker = new JDateComponentFactory().createJDatePicker();
        actStartPicker.setTextEditable(true);
        actStartPicker.setShowYearButtons(true);

        actStartPicker.getModel().setYear(Year.now().getValue());
        actStartPicker.getModel().setMonth(Calendar.getInstance().get(Calendar.MONTH));
        actStartPicker.getModel().setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        actStartPicker.getModel().setSelected(true);

        actualStartDate.add((JComponent) actStartPicker);

        expEndPicker = new JDateComponentFactory().createJDatePicker();
        expEndPicker.setTextEditable(true);
        expEndPicker.setShowYearButtons(true);

        expEndPicker.getModel().setYear(Year.now().getValue());
        expEndPicker.getModel().setMonth(Calendar.getInstance().get(Calendar.MONTH));
        expEndPicker.getModel().setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        expEndPicker.getModel().setSelected(true);

        expectedStartDate.add((JComponent) expEndPicker);

        actEndPicker = new JDateComponentFactory().createJDatePicker();
        actEndPicker.setTextEditable(true);
        actEndPicker.setShowYearButtons(true);

        actEndPicker.getModel().setYear(Year.now().getValue());
        actEndPicker.getModel().setMonth(Calendar.getInstance().get(Calendar.MONTH));
        actEndPicker.getModel().setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        actEndPicker.getModel().setSelected(true);

        actualEndDate.add((JComponent) actEndPicker);
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
