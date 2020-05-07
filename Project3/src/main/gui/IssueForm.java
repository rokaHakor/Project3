package main.gui;

import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;
import java.util.Calendar;

public class IssueForm {
    public JPanel panel1;
    private JFrame mainFrame;
    private JTextField issueNameTextField;
    private JTextField statusOnHoldTextField;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton cancelButton;
    private JButton saveButton;
    private JPanel raisedDate;
    private JPanel assignedDate;
    private JDatePicker raisedDatePicker;
    private JDatePicker assignedDatePicker;


    public IssueForm(JFrame frame) {
        mainFrame = frame;

        raisedDatePicker = new JDateComponentFactory().createJDatePicker();
        raisedDatePicker.setTextEditable(true);
        raisedDatePicker.setShowYearButtons(true);

        raisedDatePicker.getModel().setYear(Year.now().getValue());
        raisedDatePicker.getModel().setMonth(Calendar.getInstance().get(Calendar.MONTH));
        raisedDatePicker.getModel().setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        raisedDatePicker.getModel().setSelected(true);

        raisedDate.add((JComponent) raisedDatePicker);

        assignedDatePicker = new JDateComponentFactory().createJDatePicker();
        assignedDatePicker.setTextEditable(true);
        assignedDatePicker.setShowYearButtons(true);

        assignedDatePicker.getModel().setYear(Year.now().getValue());
        assignedDatePicker.getModel().setMonth(Calendar.getInstance().get(Calendar.MONTH));
        assignedDatePicker.getModel().setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        assignedDatePicker.getModel().setSelected(true);

        assignedDate.add((JComponent) assignedDatePicker);

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
