package main.gui;

import main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

public class ProjectGUI {
    public JPanel panel1;
    private JFrame mainFrame;
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
    ArrayList<Deliverable> deliverables = new ArrayList<>();
    ArrayList<Task> tasks = new ArrayList<>();
    ArrayList<Issue> issues = new ArrayList<>();
    ArrayList<ActionItem> actionItems = new ArrayList<>();
    ArrayList<Resource> resources = new ArrayList<>();

    public ProjectGUI(JFrame frame) {
        mainFrame = frame;

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

        createDummy();

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
                deliverableForm.setContentPane(new DeliverableForm(deliverableForm, ProjectGUI.this).panel1);
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
                taskForm.setContentPane(new TaskForm(taskForm, ProjectGUI.this).panel1);
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
                issueForm.setContentPane(new IssueForm(issueForm, ProjectGUI.this).panel1);
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
                actionItemForm.setContentPane(new ActionItemForm(actionItemForm, ProjectGUI.this).panel1);
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
                resourceForm.setContentPane(new ResourcesForm(resourceForm, ProjectGUI.this).panel1);
                resourceForm.pack();
                resourceForm.setVisible(true);
            }
        });
        ganttChart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel1.setVisible(false);
            }
        });
    }

    public void setProjectName(String name) {
        projectName.setText(name);
    }

    private void createDummy() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 15);
        Date date1 = new Date();
        Date date2 = cal.getTime();

        deliverables.add(new Deliverable(1, "GUI Deliverable", "Deliverable for the GUI", date2));
        deliverables.add(new Deliverable(2, "Gantt Chart", "Deliverable for Gantt Chart", date2));
        deliverables.add(new Deliverable(3, "Reports", "Deliverable for reports", date2));
        deliverables.add(new Deliverable(4, "Database", "Database delivery", date2));

        tasks.add(new Task(1, 1, 1, "First Task", "First Group of Tasks", date1,
                date2, 15, 100, date1,
                date2, 26, 20, 15,
                20, 1));
        tasks.add(new Task(2, 2, 2, "Second Task", "Second Group of Tasks", date1,
                date2, 15, 100, date1,
                date2, 26, 20, 15,
                70, 1));

        issues.add(new Issue(1, "Low", "High", "On Hold", "Broken Coffee", "The coffee machine broke again",
                "On Hold", date1, date1, date2,
                date2, date1));
        issues.add(new Issue(1, "Medium", "Low", "In Progress", "No Money", "Ran out of money, can't pay employees",
                "In Progress", date1, date1, date2,
                date2, date1));

        actionItems.add(new ActionItem(1, 1, 1, 1, "On Hold", "New Coffee",
                "Need to Buy new Coffee Machine", "On Hold", date1, date1,
                date2, date2, date1));
        actionItems.add(new ActionItem(1, 1, 1, 1, "In Progress", "Get Money",
                "Need to get more Money", "In Progress", date1, date1,
                date2, date2, date1));


        resources.add(new Resource(1, "Irmuun", "Java, GUI Code", "8AM,5PM,M,W,F", (float) 25.50));
        resources.add(new Resource(2, "Bezan", "Java, SQL", "8AM,5PM,M,W,F", (float) 25.50));
        resources.add(new Resource(3, "Neli", "GUI Design", "8AM,5PM,M,W,F", (float) 25.50));
        resources.add(new Resource(4, "Gabe", "Detailed Design", "8AM,5PM,M,W,F", (float) 25.50));
        resources.add(new Resource(5, "James", "Detailed Design", "8AM,5PM,M,W,F", (float) 25.50));
    }

    public void setAddDeliverable(String name, String desc, Date due) {
        deliverables.add(new Deliverable(1, name, desc, due));
    }

    public void setAddTask(String text, String descTextText, Date expectedStart, Date expectedEnd, Date actualStart, Date actualEnd) {
        tasks.add(new Task(1, 1, 1, text, descTextText, expectedStart,
                expectedEnd, 15, 100, actualStart,
                actualEnd, 26, 0, 0,
                0, 1));
    }

    public void setAddIssue(String text, String descTextText, Date raisedDate, Date assignedDate, Object selectedItem, Object item, Object severityComboSelectedItem) {
        issues.add(new Issue(1, (String) item, (String) severityComboSelectedItem, (String) selectedItem, text, descTextText,
                "On Hold", raisedDate, raisedDate, assignedDate,
                assignedDate, raisedDate));
    }

    public void setAddActionItem(String text, String descTextText, Object selectedItem, Date createdDate, Date expectedDate, Date actualDate, Date updatedDate) {
        actionItems.add(new ActionItem(1, 1, 1, 1, "On Hold", text,
                descTextText, (String) selectedItem, createdDate, createdDate,
                expectedDate, actualDate, updatedDate));
    }

    public void setAddResource(String text, String titleTextText, String skillsTextText, float v) {
        resources.add(new Resource(1, text, titleTextText, "8AM,5PM,M,W,F", v));
    }

    public void refreshPanels() {
        addDeliverables();
        addTasks();
        addIssues();
        addActionItems();
        addResources();
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addDeliverables() {
        deliverablesPanel.removeAll();
        for (Deliverable d : deliverables) {
            deliverablesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JButton button = new JButton(d.getName());
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            deliverablesPanel.add(button);
            deliverablesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame deliverableForm = new JFrame();
                    deliverableForm.setTitle("Deliverable Form");
                    deliverableForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    deliverableForm.setContentPane(new DeliverableForm(deliverableForm, false, d.getName(), d.getDescription(), null, null, d.getDueDate()).panel1);
                    deliverableForm.pack();
                    deliverableForm.setVisible(true);
                }
            });
        }
    }

    private void addTasks() {
        tasksPanel.removeAll();
        for (Task t : tasks) {
            tasksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JButton button = new JButton(t.getName());
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            tasksPanel.add(button);
            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue(t.getPercentComplete());
            bar.setString(t.getPercentComplete() + "%");
            bar.setStringPainted(true);
            tasksPanel.add(bar);
            tasksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame taskForm = new JFrame();
                    taskForm.setTitle("Task Form");
                    taskForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    taskForm.setContentPane(new TaskForm(taskForm, false, t.getName(), t.getDescription(), null, null, null, t.getActualStartDate(), t.getActualEndDate(), t.getExpectedStartDate(), t.getExpectedEndDate()).panel1);
                    taskForm.pack();
                    taskForm.setVisible(true);
                }
            });
        }
    }

    private void addIssues() {
        issuesPanel.removeAll();
        for (Issue i : issues) {
            issuesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JButton button = new JButton(i.getName());
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            issuesPanel.add(button);
            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new BorderLayout());
            statusPanel.add(new JLabel("<html>Priority<br/>" + i.getPriority() + "</html>"), BorderLayout.WEST);
            statusPanel.add(new JLabel("<html>Status<br/>" + i.getStatus() + "</html>", SwingConstants.CENTER), BorderLayout.CENTER);
            statusPanel.add(new JLabel("<html>Severity<br/>" + i.getSeverity() + "</html>"), BorderLayout.EAST);
            statusPanel.setMaximumSize(new Dimension(200,50));
            issuesPanel.add(statusPanel);
            issuesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame issueForm = new JFrame();
                    issueForm.setTitle("Issue Form");
                    issueForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    issueForm.setContentPane(new IssueForm(issueForm, false, i.getName(), i.getDescription(), i.getStatus(), i.getDateRaised(), i.getDateAssigned(), i.getUpdateDate(), i.getPriority(), i.getSeverity()).panel1);
                    issueForm.pack();
                    issueForm.setVisible(true);
                }
            });
        }
    }

    private void addActionItems() {
        actionItemsPanel.removeAll();
        for (ActionItem a : actionItems) {
            actionItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new BorderLayout());
            JButton button = new JButton(a.getName());
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            statusPanel.add(button, BorderLayout.NORTH);
            statusPanel.add(new JLabel("Status: " + a.getStatus()), BorderLayout.CENTER);
            statusPanel.setMaximumSize(new Dimension(200,50));
            actionItemsPanel.add(statusPanel);
            actionItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame actionItemForm = new JFrame();
                    actionItemForm.setTitle("Action Item Form");
                    actionItemForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    actionItemForm.setContentPane(new ActionItemForm(actionItemForm, false, a.getName(), a.getDescription(), null, a.getStatus(), a.getDateCreated(), a.getExpectedCompletionDate(), a.getActualCompletionDate(), a.getUpdateDate()).panel1);
                    actionItemForm.pack();
                    actionItemForm.setVisible(true);
                }
            });
        }
    }

    private void addResources() {
        resourcesPanel.removeAll();
        for (Resource r : resources) {
            resourcesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new BorderLayout());
            JButton button = new JButton(r.getName());
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            statusPanel.add(button, BorderLayout.NORTH);
            statusPanel.add(new JLabel("8AM-5PM", SwingConstants.CENTER), BorderLayout.CENTER);
            statusPanel.add(new JLabel("M-W-F", SwingConstants.CENTER), BorderLayout.SOUTH);
            statusPanel.setMaximumSize(new Dimension(200,60));
            resourcesPanel.add(statusPanel);
            resourcesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame resourceForm = new JFrame();
                    resourceForm.setTitle("Resource Form");
                    resourceForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    resourceForm.setContentPane(new ResourcesForm(resourceForm, false, r.getName(), "Employee", r.getTitle(), 8, 5, EnumSet.of(Days.MONDAY, Days.WEDNESDAY, Days.FRIDAY), r.getPayRate()).panel1);
                    resourceForm.pack();
                    resourceForm.setVisible(true);
                }
            });
        }
    }
}
