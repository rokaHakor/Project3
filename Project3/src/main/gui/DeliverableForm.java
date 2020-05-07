package main.gui;

import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeliverableForm {
    public JPanel panel1;
    private JFrame mainFrame;
    private JTextField textField1;
    private JButton button1;
    private JButton req1Button;
    private JButton req2Button;
    private JButton req3Button;
    private JButton button2;
    private JButton task1Button;
    private JButton task2Button;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField deliverableNameTextField;
    private JButton task3Button;
    private JPanel datePanel;

    public DeliverableForm(JFrame frame) {
        mainFrame = frame;

        JDatePicker picker = new JDateComponentFactory().createJDatePicker();
        picker.setTextEditable(true);
        picker.setShowYearButtons(true);

        picker.getModel().setYear(2020);
        picker.getModel().setMonth(6);
        picker.getModel().setDay(15);
        picker.getModel().setSelected(true);

        datePanel.add((JComponent) picker);
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
