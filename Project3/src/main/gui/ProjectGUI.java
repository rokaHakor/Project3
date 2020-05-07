package main.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectGUI {
    public JPanel panel1;
    private JButton addActionItem;
    private JButton actionItem1Button;
    private JButton addResource;
    private JButton jamesLordButton;
    private JButton addDeliverable;
    private JButton addTask;
    private JButton addIssue;

    public ProjectGUI() {
        addDeliverable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame deliverableForm = new JFrame();
                deliverableForm.setTitle("Deliverable Form");
                deliverableForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                deliverableForm.setContentPane(new DeliverableForm(deliverableForm).panel1);
                deliverableForm.pack();
                deliverableForm.setVisible(true);
            }
        });
        addTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame taskForm = new JFrame();
                taskForm.setTitle("Task Form");
                taskForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                taskForm.setContentPane(new TaskForm(taskForm).panel1);
                taskForm.pack();
                taskForm.setVisible(true);
            }
        });
        addIssue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame issueForm = new JFrame();
                issueForm.setTitle("Issue Form");
                issueForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                issueForm.setContentPane(new IssueForm(issueForm).panel1);
                issueForm.pack();
                issueForm.setVisible(true);
            }
        });
        addActionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame actionItemForm = new JFrame();
                actionItemForm.setTitle("Action Item Form");
                actionItemForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                actionItemForm.setContentPane(new ActionItemForm(actionItemForm).panel1);
                actionItemForm.pack();
                actionItemForm.setVisible(true);
            }
        });
        addResource.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame resourceForm = new JFrame();
                resourceForm.setTitle("Deliverable Form");
                resourceForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resourceForm.setContentPane(new ResourcesForm(resourceForm).panel1);
                resourceForm.pack();
                resourceForm.setVisible(true);
            }
        });
    }
}
