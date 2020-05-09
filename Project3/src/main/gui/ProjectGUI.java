package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectGUI {
    public JPanel panel1;
    private JButton addActionItem;
    private JButton addResource;
    private JButton addDeliverable;
    private JButton addTask;
    private JButton addIssue;
    private JPanel deliverablesPanel;
    private JPanel tasksPanel;
    private JPanel issuesPanel;
    private JPanel actionItemsPanel;
    private JPanel resourcesPanel;
    private JRadioButton ganttChart;
    private JLabel projectName;

    public ProjectGUI() {
        deliverablesPanel.setLayout(new BoxLayout(deliverablesPanel, BoxLayout.Y_AXIS));
        deliverablesPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        tasksPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        issuesPanel.setLayout(new BoxLayout(issuesPanel, BoxLayout.Y_AXIS));
        issuesPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        actionItemsPanel.setLayout(new BoxLayout(actionItemsPanel, BoxLayout.Y_AXIS));
        actionItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        resourcesPanel.setLayout(new BoxLayout(resourcesPanel, BoxLayout.Y_AXIS));
        resourcesPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        addDeliverables();
        addTasks();
        addIssues();
        addActionItems();
        addResources();


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
                resourceForm.setTitle("Resource Form");
                resourceForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resourceForm.setContentPane(new ResourcesForm(resourceForm).panel1);
                resourceForm.pack();
                resourceForm.setVisible(true);
            }
        });
        ganttChart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void setProjectName(String name) {
        projectName.setText(name);
    }

    public void refreshPanels() {

    }

    private void addDeliverables() {
        for (int x = 1; x <= 10; x++) {
            deliverablesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JButton button = new JButton("Button" + x);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            deliverablesPanel.add(button);
            deliverablesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    private void addTasks() {
        for (int x = 1; x <= 10; x++) {
            tasksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JButton button = new JButton("Button" + x);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            tasksPanel.add(button);
            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue(30);
            bar.setString(30 + "%");
            bar.setStringPainted(true);
            tasksPanel.add(bar);
            tasksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    private void addIssues() {
        for (int x = 1; x <= 10; x++) {
            issuesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JButton button = new JButton("Button" + x);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            issuesPanel.add(button);
            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new BorderLayout());
            statusPanel.add(new JLabel("<html>Priority<br/>Low</html>"), BorderLayout.WEST);
            statusPanel.add(new JLabel("<html>Status<br/>Low</html>", SwingConstants.CENTER), BorderLayout.CENTER);
            statusPanel.add(new JLabel("<html>Severity<br/>Low</html>"), BorderLayout.EAST);
            issuesPanel.add(statusPanel);
            issuesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    private void addActionItems() {
        for (int x = 1; x <= 10; x++) {
            actionItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new BorderLayout());
            JButton button = new JButton("Button" + x);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            statusPanel.add(button, BorderLayout.NORTH);
            statusPanel.add(new JLabel("Status: On Hold"), BorderLayout.CENTER);
            actionItemsPanel.add(statusPanel);
            actionItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    private void addResources() {
        for (int x = 1; x <= 10; x++) {
            resourcesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new BorderLayout());
            JButton button = new JButton("Button " + x);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            statusPanel.add(button, BorderLayout.NORTH);
            statusPanel.add(new JLabel("8AM-5PM", SwingConstants.CENTER), BorderLayout.CENTER);
            statusPanel.add(new JLabel("M-W-F", SwingConstants.CENTER), BorderLayout.SOUTH);
            resourcesPanel.add(statusPanel);
            resourcesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
}
