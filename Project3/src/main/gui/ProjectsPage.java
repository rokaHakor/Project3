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

    public ProjectsPage(JFrame frame) {
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

        displayProjects();

        createProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame projectForm = new JFrame();
                projectForm.setTitle("Project Form");
                projectForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                projectForm.setContentPane(new ProjectForm(projectForm, frame).panel1);
                projectForm.pack();
                projectForm.setVisible(true);
            }
        });
    }

    public void displayProjects() {
        int x = 0;
        for (Project p : User.getProjectVector()) {
            if (x % 3 == 0) {
                createProjectButton(p, centerPanel);
            }
            if (x % 3 == 1) {
                createProjectButton(p, rightPanel);
            }
            if (x % 3 == 2) {
                createProjectButton(p, leftPanel);
            }
            x++;
        }
    }

    private void createProjectButton(Project p, JPanel panel) {
        JPanel panelRight = new JPanel(new FlowLayout());
        JButton button = new JButton(p.getName());
        button.setFont(new Font(button.getFont().getName(), button.getFont().getStyle(), 24));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelRight.add(button);
        panel.add(panelRight);
    }
}
