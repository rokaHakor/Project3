package main.gui;

import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;
import java.util.Calendar;

public class ActionItemForm {
    public JPanel panel1;
    private JFrame mainFrame;
    private JTextField textField1;
    private JButton button1;
    private JButton neliButton;
    private JButton jamesButton;
    private JButton bezanButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel expectedDate;
    private JTextField actionItemNameTextField;
    private JTextField textField2;
    private JPanel actualDate;
    private JLabel createdDate;
    private JLabel updatedDate;
    private JDatePicker expectedDatePicker;
    private JDatePicker actualDatePicker;


    public ActionItemForm(JFrame frame) {
        mainFrame = frame;

        expectedDatePicker = new JDateComponentFactory().createJDatePicker();
        expectedDatePicker.setTextEditable(true);
        expectedDatePicker.setShowYearButtons(true);

        expectedDatePicker.getModel().setYear(Year.now().getValue());
        expectedDatePicker.getModel().setMonth(Calendar.getInstance().get(Calendar.MONTH));
        expectedDatePicker.getModel().setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        expectedDatePicker.getModel().setSelected(true);

        expectedDate.add((JComponent) expectedDatePicker);

        actualDatePicker = new JDateComponentFactory().createJDatePicker();
        actualDatePicker.setTextEditable(true);
        actualDatePicker.setShowYearButtons(true);

        actualDatePicker.getModel().setYear(Year.now().getValue());
        actualDatePicker.getModel().setMonth(Calendar.getInstance().get(Calendar.MONTH));
        actualDatePicker.getModel().setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        actualDatePicker.getModel().setSelected(true);

        actualDate.add((JComponent) actualDatePicker);

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
