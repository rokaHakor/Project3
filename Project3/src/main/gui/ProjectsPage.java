package main.gui;

import main.Project;
import main.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectsPage {
    public JPanel panel1;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;

    public ProjectsPage() {
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel panel = new JPanel(new BorderLayout());
        JButton createProject = new JButton("Create Project");
        createProject.setFont(new Font(createProject.getFont().getName(), createProject.getFont().getStyle(), 24));
        createProject.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(createProject, BorderLayout.NORTH);
        leftPanel.add(panel);


        createProject.addActionListener(new ActionListener() {
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
    }

    public void displayProjects() {
        int x = 0;
        for (Project p : User.getProjectVector()) {
            if (x % 3 == 0) {
                JPanel panelCenter = new JPanel(new FlowLayout());
                JButton button = new JButton("Create Project");
                button.setFont(new Font(button.getFont().getName(), button.getFont().getStyle(), 24));
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelCenter.add(button);
                centerPanel.add(panelCenter);
            }
            if (x % 3 == 1) {
                JPanel panelRight = new JPanel(new FlowLayout());
                JButton button = new JButton("Create Project");
                button.setFont(new Font(button.getFont().getName(), button.getFont().getStyle(), 24));
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelRight.add(button);
                rightPanel.add(panelRight);
            }
            if (x % 3 == 2) {
                JPanel panelLeft = new JPanel(new FlowLayout());
                JButton button = new JButton("Create Project");
                button.setFont(new Font(button.getFont().getName(), button.getFont().getStyle(), 24));
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelLeft.add(button);
                leftPanel.add(panelLeft);
            }
            x++;
        }
    }
}
