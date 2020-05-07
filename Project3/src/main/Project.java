/*

        String sql = "";
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {

        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);

 */

package main;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Project {
    int projectID;
    String name, description, url;

    Vector<Deliverable> deliverableVector;
    Vector<Task> taskVector;
    Vector<Issue> issueVector;
    Vector<ActionItem> actionItemVector;
    Vector<Resource> resourceVector;
    Vector<Requirement> requirementVector;
    Vector<Decision> decisionVector;
    Vector<ReferenceDocument> referenceDocumentVector;
    Vector<Risk> riskVector;
    Vector<Change> changeVector;
    Vector<Dependent> dependentVector;
    Vector<IssueAffectingTask> issueAffectingTaskVector;
    Vector<MeetingNote> meetingNoteVector;
    Vector<ResourceSkill> resourceSkillVector;
    

    public Project(int projectID, String name, String description, String url) {
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.url = url;

        deliverableVector = new Vector<>();
        taskVector = new Vector<>();
        issueVector = new Vector<>();
        actionItemVector = new Vector<>();
        resourceVector = new Vector<>();
        requirementVector = new Vector<>();
    }

    int getProjectID() {return projectID;}
    String getName() {return name;}
    String getDescription() {return description;}
    String getUrl() {return url;}
    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}

    /*********************************************** DELIVERABLE ******************************************************/
    
    public void createDeliverableButtonClicked(String name, String description, Date dueDate, Vector<Integer> requirementIDVector, Vector<Integer> taskIDVector) {
        insertDeliverableInDB(name, description, dueDate);
        int deliverableID = Connect.getNewestIDFromTable(Connect.getConnectionToDB(getUrl()), "Deliverable", "DeliverableID");      //Get the id of the deliverable that was just inserted into the database (database handles primary key auto increment)
        Requirement requirement = null;
        Task task = null;

        //Create associations to any Requirements or Tasks
        // -Insert the deliverableID of the Deliverable being created into each row of the Requirement & Task tables that have been associated with it
        // -Set requirementID or TaskID for the Requirement or Task obj in the requirementVector or taskVector
        for(int requirementID : requirementIDVector) {
            updateDeliverableIDInRequirementInDB(deliverableID, requirementID);    //Update the Requirement records DeliverableID attribute in the database
            requirement = getRequirement(requirementID);        //Get the Requirement object whose association is being added
            requirement.setDeliverableID(deliverableID);        //Set the deliverableID member variable for the Requirement object
        }
        for(int taskID : taskIDVector) {
            updateDeliverableIDInTaskInDB(deliverableID, taskID);
            task = getTask(taskID);
            task.setDeliverableID(deliverableID);
        }

        addDeliverableToVector(deliverableID, name, description, dueDate);
        //Todo: Navigate back to Deliverable sheet
    }

    public void updateDeliverableButtonClicked(Deliverable deliverable, String name, String description, Date dueDate,
                                               Vector<Integer> requirementIDVector, Vector<Integer> taskIDVector) {

        updateDeliverableInDB(deliverable.getDeliverableID(), name, description, dueDate);
        updateRequirementAndTaskAssociations(deliverable, requirementIDVector, taskIDVector);
        updateDeliverableInProject(deliverable, name, description, dueDate);
    }

    public void deleteDeliverableButtonClicked(Deliverable deliverable) {
        Vector<Integer> requirementIDsVector = new Vector<>();
        Vector<Integer> taskIDsVector = new Vector<>();

        //Remove any Requirement & Task Associations for the Deliverable
        updateRequirementAndTaskAssociations(deliverable, requirementIDsVector, taskIDsVector);     //Passing empty vectors deletes all associations

        //Remove Risks associated with the deliverable
        for(Risk risk : riskVector) {
            if(risk.getDeliverableID() == deliverable.getDeliverableID()) {
                getRisk(risk.getRiskID()).setDeliverableID(0);
            }
        }

        deleteDeliverableFromDB(deliverable.getDeliverableID());
        deliverableVector.remove(deliverable);      //Delete Deliverable from project
    }


    //Returns the Deliverable obj if it exists or null if not
    public Deliverable getDeliverable(int deliverableID) {
        Deliverable ret = null;
        for (Deliverable deliverable : deliverableVector) {
            if (deliverable.getDeliverableID() == deliverableID) {
                ret = deliverable;
            }
        }
        return ret;
    }

    //Inserts the Deliverable into database, creating a new record/row
    public void insertDeliverableInDB(String name, String description, Date dueDate) {
        String sql = "INSERT INTO Deliverable(Name, Description, Due_Date) VALUES (?, ?, ?)";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setLong(3, User.convertDateToLong(dueDate));
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    //Called when createDeliverableButtonClicked() is called
    public void addDeliverableToVector(int deliverableID, String name, String description, Date dueDate) {
        Deliverable del = new Deliverable(deliverableID, name, description, dueDate);
        deliverableVector.add(del);
    }

    //Checks to see if the Requirements & Tasks associated with the Deliverable have changed & Adds or Removes them if needed
    public void updateRequirementAndTaskAssociations(Deliverable deliverable, Vector<Integer> requirementIDVector, Vector<Integer> taskIDVector) {
        
        Vector<Integer> savedRequirementIDsVector = getAllRequirementIDsAssociatedWithThisDeliverable(deliverable.getDeliverableID());
        Vector<Integer> savedTaskIDsVector = getAllTaskIDsAssociatedWithThisDeliverable(deliverable.getDeliverableID());

        // If a requirement of task has been deleted, clear the DeliverableID attribute in the DB & project
        for (Integer requirementID : savedRequirementIDsVector) {
            if (!requirementIDVector.contains(requirementID)) {
                removeDeliverableIDFromRequirementInDB(requirementID);  //Remove the DeliverableID from the requirement in the database
                getRequirement(requirementID).setDeliverableID(0);      //Remove the deliverableID from the requirement obj in the project
            }
        }
        for (Integer taskID : savedTaskIDsVector) {
            if (!requirementIDVector.contains(taskID)) {
                removeDeliverableIDFromTaskInDB(taskID);
                getTask(taskID).setDeliverableID(0);
            }
        }

        //If a Requirement or Task has been added, add the deliverableID to their DeliverableID attribute in DB & project
        for (Integer requirementID : requirementIDVector) {
            if (!savedRequirementIDsVector.contains(requirementID)) {    //If the requirementVector in the Deliverable does not contain the requirementID
                updateDeliverableIDInRequirementInDB(deliverable.getDeliverableID(), requirementID);
                getRequirement(requirementID).setDeliverableID(deliverable.getDeliverableID());
            }
        }
        for (Integer taskID : taskIDVector) {
            if (!savedTaskIDsVector.contains(taskID)) {    //If the requirementVector in the Deliverable does not contain the requirementID
                updateDeliverableIDInTaskInDB(deliverable.getDeliverableID(), taskID);
                getTask(taskID).setDeliverableID(deliverable.getDeliverableID());
            }
        }
    }

    //Adds the DeliverableID to the Requirement in the project database
    public void updateDeliverableIDInRequirementInDB(int deliverableID, int requirementID) {
        String sql = "UPDATE Requirement SET DeliverableID = ? WHERE RequirementID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());

        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deliverableID);
            pstmt.setInt(2, requirementID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    //Adds the DeliverableID to the Task in the project database
    public void updateDeliverableIDInTaskInDB(int deliverableID, int taskID) {
        String sql = "UPDATE Task SET DeliverableID = ? WHERE TaskID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());

        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deliverableID);
            pstmt.setInt(2, taskID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public Vector<Integer> getAllRequirementIDsAssociatedWithThisDeliverable(int deliverableID) {
        Vector<Integer> requirementIDsVector = new Vector<>();
        for(Requirement requirement : requirementVector) {
            if(requirement.getDeliverableID() == deliverableID) {
                requirementIDsVector.add(requirement.getRequirementID());
            }
        }
        return requirementIDsVector;
    }

    public Vector<Integer> getAllTaskIDsAssociatedWithThisDeliverable(int deliverableID) {
        Vector<Integer> taskIDsVector = new Vector<>();
        for(Task task : taskVector) {
            if(task.getDeliverableID() == deliverableID) {
                taskIDsVector.add(task.getTaskID());
            }
        }
        return taskIDsVector;
    }
    

    //Sets the DeliverableID attribute to NULL for the Requirement
    public void removeDeliverableIDFromRequirementInDB(int requirementID) {
        String sql = "UPDATE Requirement SET DeliverableID = NULL WHERE RequirementID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, requirementID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    //Sets the DeliverableID attribute to NULL for the Task
    public void removeDeliverableIDFromTaskInDB(int taskID) {
        String sql = "UPDATE Task SET DeliverableID = NULL WHERE TaskID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, taskID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    //Updates the Deliverable object in the Projects deliverableVector
    public void updateDeliverableInProject(Deliverable deliverable, String name, String description, Date dueDate) {
        deliverable.setName(name);
        deliverable.setDescription(description);
        deliverable.setDueDate(dueDate);
    }

    //Updates the deliverable in the database
    public void updateDeliverableInDB(int deliverableID, String name, String description, Date due_Date) {
        String sql = "UPDATE Deliverable SET Name =  ?, Description = ?, Due_Date = ? WHERE DeliverableID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setLong(3, User.convertDateToLong(due_Date));
            pstmt.setInt(4, deliverableID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Chance to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    //Used for the Deliverable sheet in the Overview page
    public void loadAllDeliverablesFromDB() {
        ResultSet rs1;                   //ResultSet object is used to hold the results of the sql query
        Deliverable del;          //Used for the individual Deliverable object before it is added to the deliverables vector
        int deliverableID;
        Connection conn = Connect.getConnectionToDB(getUrl());

        //SQL Strings that get all rows from the Deliverable table, get all RequirementID & TaskID's associated with each Deliverable
        String sql1 = "SELECT * FROM Deliverable";

        try {
            //Create Statement object to be used to execute the sql Statements above
            Statement stmt = conn.createStatement();
            rs1 = stmt.executeQuery(sql1);              //Execute query getting all rows in the databases Deliverable table

            //Go through each row of the Deliverable table
            while(rs1.next()) {
                del = new Deliverable(rs1.getInt("DeliverableID"), rs1.getString("Name"),
                        rs1.getString("Description"), User.convertLongToDate(rs1.getLong("Due_Date")));
                deliverableVector.add(del);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void deleteDeliverableFromDB(int deliverableID) {
        String sql = "DELETE FROM Deliverable WHERE DeliverableID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deliverableID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }
    
    /*********************************************** TASK *************************************************************/

    //Returns the Task obj if it exists or null if not
    public Task getTask(int taskID) {
        Task ret = null;
        for (Task task : taskVector) {
            if (task.getTaskID() == taskID) {
                ret = task;
            }
        }
        return ret;
    }

    public void createTaskButtonClicked(String name, String description, Date expectedStartDate, Date expectedEndDate,
                                        long expectedDuration, int expectedEffort, Date actualStartDate,
                                        Date actualEndDate, long actualDuration, int effortCompleted, int actualEffort,
                                        int percentComplete, int type, int resourceID, Vector<Integer> issueIDVector,
                                        Map<Integer, Integer> dependentTasksMap) {
        int dependency = 0;

        //Insert a new record into the DB (in the Task table) & in the project
        insertTaskInDB(name, description, expectedStartDate, expectedEndDate, expectedDuration,
                expectedEffort, actualStartDate, actualEndDate, actualDuration, effortCompleted, actualEffort,
                percentComplete, type, resourceID);
        int taskID = Connect.getNewestIDFromTable(Connect.getConnectionToDB(getUrl()), "Task", "TaskID");
        Task task = new Task(taskID, 0, resourceID, name, description, expectedStartDate, expectedEndDate, expectedDuration,
                expectedEffort, actualStartDate, actualEndDate, actualDuration, effortCompleted, actualEffort,
                percentComplete, type);
        taskVector.add(task);       //Add the newly created Deliverable to the Project's deliverableVector

        //Create Issue & Dependent Task associations
        //For every Issue associated with the task add a record into the DB & project
        for(int issueID : issueIDVector) {
            insertIssueAffectingTaskInDB(issueID, taskID);
            IssueAffectingTask iat = new IssueAffectingTask(issueID, taskID);
            issueAffectingTaskVector.add(iat);      //Add Issue Affecting Task to project
        }
        //For every Task that this Task is Dependent on, add a record into the DB & project
        Set<Integer> dependentTaskIDs = dependentTasksMap.keySet();     //Get the taskID's for all the dependent tasks being associated
        for (int dependentTaskID : dependentTaskIDs) {
            dependency = dependentTasksMap.get(dependentTaskID);
            insertDependentInDB(taskID, dependentTaskID, dependency);
            Dependent dependent = new Dependent(taskID, dependentTaskID, dependency);
            dependentVector.add(dependent);
        }

        //Todo: Navigate back to Task sheet
    }

    public void updateTaskButtonClicked(Task task, String name, String description, Date expectedStartDate,
                                        Date expectedEndDate, long expectedDuration, int expectedEffort, Date actualStartDate,
                                        Date actualEndDate, long actualDuration, int effortCompleted, int actualEffort,
                                        int percentComplete, int type, int deliverableID, int resourceID,
                                        Vector<Integer> issueIDVector, Map<Integer, Integer> dependentTasksMap) {

        checkIssueAndTaskLists(task, issueIDVector, dependentTasksMap); //Checks to see is any Issue or Task associations have been added, deleted or changed

        updateTaskInVector(task, name, description, expectedStartDate, expectedEndDate, expectedDuration,
                expectedEffort, actualStartDate, actualEndDate, actualDuration, effortCompleted, actualEffort,
                percentComplete, type, resourceID);

        updateTaskInDB(task);
    }

    public void deleteTaskButtonClicked(Task task) {
        int taskID = task.getTaskID();
        Vector<Integer> issueIDsVector = getAllIssueIDsAssociatedWithThisTask(taskID);
        Map<Integer, Integer> dependentTasksMap = getAllDependenciesAssociatedWithThisTask(taskID);
        //Remove all associations
        checkIssueAndTaskLists(task, issueIDsVector, dependentTasksMap);

        //Delete task in DB & project
        deleteTaskFromDB(taskID);
        taskVector.remove(task);

        //Todo: Return to Task sheet
    }

    public void insertTaskInDB(String name, String description, Date expectedStartDate,
                               Date expectedEndDate, long expectedDuration, int expectedEffort, Date actualStartDate,
                               Date actualEndDate, long actualDuration, int effortCompleted, int actualEffort,
                               int percentComplete, int type, int resourceID) {
        //Todo: If no deliverable is being linked, pass NULL for DeliverableID
        String sql = "INSERT INTO Task(Name, Description, Expected_Start_date, Expected_End_Date, " +
                "Expected_Duration, Expected_Effort, Actual_Start_date, Actual_End_Date, Actual_Duration, " +
                "Effort_Completed, Actual_Effort, Percent_Complete, Type, ResourceID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?)";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setLong(3, User.convertDateToLong(expectedStartDate));
            pstmt.setLong(4, User.convertDateToLong(expectedEndDate));
            pstmt.setLong(5, expectedDuration);
            pstmt.setInt(6, expectedEffort);
            pstmt.setLong(7, User.convertDateToLong(actualStartDate));
            pstmt.setLong(8, User.convertDateToLong(actualEndDate));
            pstmt.setLong(9, actualDuration);
            pstmt.setInt(10, effortCompleted);
            pstmt.setInt(11, actualEffort);
            pstmt.setInt(12, percentComplete);
            pstmt.setInt(13, type);
            pstmt.setInt(14, resourceID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void loadAllTasksFromDB() {
        String sql1 = "SELECT * FROM Task";
        ResultSet rs1;                    //ResultSet object is used to hold the results of the sql query
        Task task;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs1 = stmt.executeQuery(sql1);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs1.next()) {
                task = new Task();
                task.setTaskID(rs1.getInt("TaskID"));
                task.setDeliverableID(rs1.getInt("DeliverableID"));
                task.setResourceID(rs1.getInt("ResourceID"));
                task.setName(rs1.getString("Name"));
                task.setDescription(rs1.getString("Description"));
                task.setExpectedStartDate(User.convertLongToDate(rs1.getLong("Expected_Start_Date")));
                task.setExpectedEndDate(User.convertLongToDate(rs1.getLong("Expected_End_Date")));
                task.setExpectedDuration(rs1.getLong("Expected_Duration"));
                task.setExpectedEffort(rs1.getInt("Expected_Effort"));
                task.setActualStartDate(User.convertLongToDate(rs1.getLong("Actual_Start_Date")));
                task.setActualEndDate(User.convertLongToDate(rs1.getLong("Actual_End_Date")));
                task.setActualDuration(rs1.getLong("Actual_Duration"));
                task.setEffortCompleted(rs1.getInt("Effort_Completed"));
                task.setActualEffort(rs1.getInt("Actual_Effort"));
                task.setPercentComplete(rs1.getInt("Percent_Complete"));
                task.setType(rs1.getInt("Type"));
                taskVector.add(task);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void checkIssueAndTaskLists(Task task, Vector<Integer> issueIDVector, Map<Integer, Integer> dependentTasksMap) {
        Vector<Integer> savedIssuesAssociatedVector = getAllIssueIDsAssociatedWithThisTask(task.getTaskID());
        Map<Integer, Integer> savedDependentTasksMap = getAllDependenciesAssociatedWithThisTask(task.getTaskID());
        int taskID = task.getTaskID();

        //ISSUE ASSOCIATIONS
        //Check to see if any Issue associations were DELETED for the Task
        for(int issueID : savedIssuesAssociatedVector) {
            if (!issueIDVector.contains(issueID)) {
                deleteIssueAffectingTaskInDB(taskID, issueID);

            }
        }
        //Check to see if any Issue associations were ADDED to the Task
        for (int issueID : issueIDVector) {
            if (!savedIssuesAssociatedVector.contains(issueID)) {
                insertIssueAffectingTaskInDB(issueID, taskID);
            }
        }

        //DEPENDENT TASK ASSOCIATIONS
        //Check for any DELETED predecessor Tasks
        for (Integer dependentTaskID : savedDependentTasksMap.keySet()) {
            //If the updated dependentTaskMap DOES NOT contain the current dependentTaskID that is currently saved in the DB
            if(!dependentTasksMap.containsKey(dependentTaskID)) {
                deleteDependentInDB(taskID, dependentTaskID);    //Delete the dependent entry in the DB
                dependentVector.remove(getDependent(taskID, dependentTaskID));//Delete dependent from project

            }
            else {
                //Check dependency attribute is the same for the saved and current dependentTaskID & update the saved value in the DB & project if not
                if(!savedDependentTasksMap.get(dependentTaskID).equals(dependentTasksMap.get(dependentTaskID))) {
                    updateDependentInDB(taskID, dependentTaskID, dependentTasksMap.get(dependentTaskID));
                    getDependent(taskID, dependentTaskID).setDependency(dependentTasksMap.get(dependentTaskID));
                }
            }
        }
        //Check for any ADDED predecessor Tasks
        for (Integer currentDependentTaskID : dependentTasksMap.keySet()) {
            if(!savedDependentTasksMap.containsKey(currentDependentTaskID)) {
                insertDependentInDB(task.taskID, currentDependentTaskID, dependentTasksMap.get(currentDependentTaskID));
            }
        }
    }

    public Vector<Integer> getAllIssueIDsAssociatedWithThisTask(int taskID) {
        Vector<Integer> issueIDsVector = new Vector<>();
        for(IssueAffectingTask iat : issueAffectingTaskVector) {
            if(iat.getTaskID() == taskID) {
                issueIDsVector.add(iat.getIssueID());
            }
        }
        return issueIDsVector;
    }

    public Map<Integer, Integer> getAllDependenciesAssociatedWithThisTask(int taskID) {
        Map<Integer, Integer> tasksMap = new HashMap<>();

        for(Dependent dependent : dependentVector) {
            if(dependent.getSuccessorID() == taskID) {
                tasksMap.put(dependent.getPredecessorID(), dependent.getDependency());
            }
        }

        return tasksMap;
    }

    public void updateTaskInDB(Task task) {
        String sql = "UPDATE Task SET ResourceID = ?, Name =  ?, Description = ?, Expected_Start_Date = ?, " +
                "Expected_End_Date = ?, Expected_Duration = ?, Expected_Effort = ?, Actual_Start_Date = ?, " +
                "Actual_End_Date = ?, Actual_Duration = ?, Effort_Completed = ?, Actual_Effort = ?, " +
                "Percent_Complete = ?, Type = ? WHERE TaskID = ?";

        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, task.getResourceID());
            pstmt.setString(2, task.getName());
            pstmt.setString(3, task.getDescription());
            pstmt.setLong(4, User.convertDateToLong(task.getExpectedStartDate()));
            pstmt.setLong(5, User.convertDateToLong(task.getExpectedEndDate()));
            pstmt.setLong(6, task.getExpectedDuration());
            pstmt.setInt(7, task.getExpectedEffort());
            pstmt.setLong(8, User.convertDateToLong(task.getActualStartDate()));
            pstmt.setLong(9, User.convertDateToLong(task.getActualEndDate()));
            pstmt.setLong(10, task.getActualDuration());
            pstmt.setInt(11, task.getEffortCompleted());
            pstmt.setInt(12, task.getActualEffort());
            pstmt.setInt(13, task.getPercentComplete());
            pstmt.setInt(14, task.getType());
            pstmt.setInt(15, task.getTaskID());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void updateTaskInVector(Task task, String name, String description, Date expectedStartDate,
                                   Date expectedEndDate, long expectedDuration, int expectedEffort, Date actualStartDate,
                                   Date actualEndDate, long actualDuration, int effortCompleted, int actualEffort,
                                   int percentComplete, int type, int resourceID) {

        task.setName(name);
        task.setDescription(description);
        task.setExpectedStartDate(expectedStartDate);
        task.setExpectedEndDate(expectedEndDate);
        task.setExpectedDuration(expectedDuration);
        task.setExpectedEffort(expectedEffort);
        task.setActualStartDate(actualStartDate);
        task.setActualEndDate(actualEndDate);
        task.setActualDuration(actualDuration);
        task.setEffortCompleted(effortCompleted);
        task.setActualEffort(actualEffort);
        task.setPercentComplete(percentComplete);
        task.setType(type);
        task.setResourceID(resourceID);

    }

    public void deleteTaskFromDB(int taskID) {
        String sql = "DELETE FROM Task WHERE TaskID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, taskID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    /*********************************************** ISSUE ************************************************************/
    public Issue getIssue(int issueID) {
        Issue ret = null;
        for (Issue issue : issueVector) {
            if (issue.getIssueID() == issueID) {
                ret = issue;
            }
        }
        return ret;
    }

    public void createIssueButtonClicked(int priority, int severity, int status, String name, String description,
                                         String statusDescription, String priorityDefaults, String severityDefaults, String statusDefaults,
                                         Date dateRaised, Date dateAssigned, Date expectedCompletionDate, Date actualCompletionDate,
                                         Date updateDate, Vector<Integer> actionItemIDsVector, Vector<Integer> decisionIDsVector) {

        int issueID;
        insertIssueInDB(priority, severity, status, name, description, statusDescription,
                priorityDefaults, severityDefaults, statusDefaults, dateRaised, dateAssigned, expectedCompletionDate,
                actualCompletionDate, updateDate);

        issueID = Connect.getNewestIDFromTable(Connect.getConnectionToDB(getUrl()), "Issue", "IssueID");
        Issue issue = new Issue(issueID, priority, severity, status, name, description, statusDescription,
                priorityDefaults, severityDefaults, statusDefaults, dateRaised, dateAssigned, expectedCompletionDate,
                actualCompletionDate, updateDate);
        issueVector.add(issue);

        updateActionItemAndDecisionAssociations(issue, actionItemIDsVector, decisionIDsVector);
    }

    public void updateIssueButtonClicked(Issue issue, int priority, int severity, int status, String name, String description,
                                         String statusDescription, String priorityDefaults, String severityDefaults, String statusDefaults,
                                         Date dateRaised, Date dateAssigned, Date expectedCompletionDate, Date actualCompletionDate,
                                         Date updateDate, Vector<Integer> actionItemIDsVector, Vector<Integer> decisionIDsVector) {

        updateIssueInDB(issue.getIssueID(), priority, severity, status, name, description, statusDescription,
                priorityDefaults, severityDefaults, statusDefaults, dateRaised, dateAssigned, expectedCompletionDate,
                actualCompletionDate, updateDate);

        updateActionItemAndDecisionAssociations(issue, actionItemIDsVector, decisionIDsVector);

        issue.updateIssue(priority, severity, status, name, description, statusDescription,
                priorityDefaults, severityDefaults, statusDefaults, dateRaised, dateAssigned, expectedCompletionDate,
                actualCompletionDate, updateDate);
    }

    public void updateActionItemAndDecisionAssociations(Issue issue, Vector<Integer> actionItemIDsVector, Vector<Integer> decisionIDsVector) {
        Vector<Integer> savedActionItemIDsVector = getAllActionItemsAssociatedWithThisIssue(issue.getIssueID());
        Vector<Integer> savedDecisionIDsVector = getAllDecisionsAssociatedWithThisIssue(issue.getIssueID());
        
        //Check for ADDED Action Items or Decisions associations
        for(Integer actionItemID : actionItemIDsVector) {
            if(!savedActionItemIDsVector.contains(actionItemID)) {
                addIssueIDToActionItemInDB(issue.getIssueID(), actionItemID);
                getActionItem(actionItemID).setIssueID(issue.getIssueID());
            }
        }
        for(Integer decisionID : decisionIDsVector) {
            if(!savedDecisionIDsVector.contains(decisionID)) {
                addIssueIDToDecisionInDB(issue.getIssueID(), decisionID);
                getDecision(decisionID).setIssueID(issue.getIssueID());
            }
        }
        
        //Check for DELETED Action Items or Decisions associations
        for(Integer actionItemID : savedActionItemIDsVector) {
            if(!actionItemIDsVector.contains(actionItemID)) {
                deleteIssueIDFromActionItemInDB(actionItemID);
                getActionItem(actionItemID).setIssueID(0);
            }
        }
        for(Integer decisionID : savedDecisionIDsVector) {
            if(!decisionIDsVector.contains(decisionID)) {
                deleteIssueIDFromDecisionInDB(decisionID);
                getDecision(decisionID).setIssueID(0);
            }
        }
    }

    public void deleteIssueIDFromDecisionInDB(int decisionID) {
        String sql = "UPDATE Decision SET IssueID = 0 WHERE DecisionID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, decisionID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void deleteIssueIDFromActionItemInDB(int actionItemID) {
        String sql = "UPDATE Action_Item SET IssueID = 0 WHERE ActionItemID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, actionItemID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void addIssueIDToDecisionInDB(int issueID, int decisionID) {
        String sql = "UPDATE Decision SET IssueID = ? WHERE DecisionID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, issueID);
            pstmt.setInt(2, decisionID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void addIssueIDToActionItemInDB(int issueID, int actionItemID) {
        String sql = "UPDATE Action_Item SET IssueID = ? WHERE ActionItemID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, issueID);
            pstmt.setInt(2, actionItemID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public Vector<Integer> getAllActionItemsAssociatedWithThisIssue(int issueID) {
        Vector<Integer> actionItemIDsVector = new Vector<>();
        for(ActionItem actionItem : actionItemVector) {
            if(actionItem.getIssueID() == issueID) {
                actionItemIDsVector.add(actionItem.getActionItemID());
            }
        }
        return actionItemIDsVector;
    }

    public Vector<Integer> getAllDecisionsAssociatedWithThisIssue(int issueID) {
        Vector<Integer> decisionIDsVector = new Vector<>();
        for(Decision decision : decisionVector) {
            if(decision.getIssueID() == issueID) {
                decisionIDsVector.add(decision.getDecisionID());
            }
        }
        return decisionIDsVector;
    }

    public void insertIssueInDB(int priority, int severity, int status, String name, String description,
                                String statusDescription, String priorityDefaults, String severityDefaults, String statusDefaults,
                                Date dateRaised, Date dateAssigned, Date expectedCompletionDate, Date actualCompletionDate,
                                Date updateDate) {

        String sql = "INSERT INTO Issue(Name, Description, Priority, Severity, Date_Raised, Date_Assigned, " +
                "Expected_Completion_Date, Actual_Completion_Date, Status, Status_Description, Update_Date, " +
                "Priority_Defaults, Severity_Defaults, status_Defaults) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, priority);
            pstmt.setInt(4, severity);
            pstmt.setLong(5, User.convertDateToLong(dateRaised));
            pstmt.setLong(6, User.convertDateToLong(dateAssigned));
            pstmt.setLong(7, User.convertDateToLong(expectedCompletionDate));
            pstmt.setLong(8, User.convertDateToLong(actualCompletionDate));
            pstmt.setInt(9, status);
            pstmt.setString(10, statusDescription);
            pstmt.setLong(11, User.convertDateToLong(updateDate));
            pstmt.setString(12, priorityDefaults);
            pstmt.setString(13, severityDefaults);
            pstmt.setString(14, statusDefaults);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void updateIssueInDB(int issueID, int priority, int severity, int status, String name, String description,
                                String statusDescription, String priorityDefaults, String severityDefaults, String statusDefaults,
                                Date dateRaised, Date dateAssigned, Date expectedCompletionDate, Date actualCompletionDate,
                                Date updateDate) {

        String sql = "UPDATE Issue SET Name = ?, Description = ?, Priority = ?, Severity = ?, Date_Raised = ?, " +
                "Date_Assigned = ?, Expected_Completion_Date = ?, Actual_Completion_Date = ?, Status = ?, " +
                "Status_Description = ?, Update_Date = ?, Priority_Defaults = ?, Severity_Defaults = ?, " +
                "status_Defaults = ? WHERE IssueID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, priority);
            pstmt.setInt(4, severity);
            pstmt.setLong(5, User.convertDateToLong(dateRaised));
            pstmt.setLong(6, User.convertDateToLong(dateAssigned));
            pstmt.setLong(7, User.convertDateToLong(expectedCompletionDate));
            pstmt.setLong(8, User.convertDateToLong(actualCompletionDate));
            pstmt.setInt(9, status);
            pstmt.setString(10, statusDescription);
            pstmt.setLong(11, User.convertDateToLong(updateDate));
            pstmt.setString(12, priorityDefaults);
            pstmt.setString(13, severityDefaults);
            pstmt.setString(14, statusDefaults);
            pstmt.setInt(15, issueID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void deleteIssueButtonClicked(Issue issue) {
        //Delete associations
        Vector<Integer> requirementIDsVector = new Vector<>();
        Vector<Integer> decisionIDsVector = new Vector<>();
        updateActionItemAndDecisionAssociations(issue, requirementIDsVector, decisionIDsVector);    //Delete Action Item & Decision associations
        //Todo: Delete Task associations

        deleteIssueFromDB(issue.getIssueID());      //Delete from DB
        issueVector.remove(issue);      //Delete from project
    }

    public void deleteIssueFromDB(int issueID) {
        String sql = "DELETE FROM Issue WHERE IssueID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, issueID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void loadAllIssuesFromDB() {
        //Todo
    }

    /********************************************* ACTION ITEM ********************************************************/
    public ActionItem getActionItem(int actionItemID) {
        ActionItem ret = null;
        for (ActionItem actionItem : actionItemVector) {
            if (actionItem.getActionItemID() == actionItemID) {
                ret = actionItem;
            }
        }
        return ret;
    }
    
    public void loadAllActionItemsFromDB() {

    }

    /********************************************* RESOURCE ********************************************************/
    public Resource getResource(int resourceID) {
        Resource ret = null;
        for (Resource resource : resourceVector) {
            if (resource.getResourceID() == resourceID) {
                ret = resource;
            }
        }
        return ret;
    }

    public void loadAllResourcesFromDB() {

    }

    /********************************************* REQUIREMENT ********************************************************/

    //Returns the Requirement obj if it exists or null if not
    public Requirement getRequirement(int requirementID) {
        Requirement req = null;
        for (Requirement requirement : requirementVector) {
            if (requirement.getRequirementID() == requirementID) {
                req = requirement;
            }
        }
        return req;
    }

    public void loadAllRequirementsFromDB() {

    }

    /********************************************* DECISION ********************************************************/
    public Decision getDecision(int decisionID) {
        Decision ret = null;
        for (Decision decision : decisionVector) {
            if (decision.getDecisionID() == decisionID) {
                ret = decision;
            }
        }
        return ret;
    }

    public void loadAllDecisionsFromDB() {

    }

    /******************************************* REFERENCE DOCUMENT ***************************************************/
    public ReferenceDocument getReferenceDocument(int referenceDocumentID) {
        ReferenceDocument ret = null;
        for (ReferenceDocument referenceDocument : referenceDocumentVector) {
            if (referenceDocument.getReferenceDocumentID() == referenceDocumentID) {
                ret = referenceDocument;
            }
        }
        return ret;
    }

    public void loadAllReferenceDocumentsFromDB() {

    }

    /********************************************* RISK ********************************************************/
    public Risk getRisk(int riskID) {
        Risk ret = null;
        for (Risk risk : riskVector) {
            if (risk.getRiskID() == riskID) {
                ret = risk;
            }
        }
        return ret;
    }

    public void loadAllRisksFromDB() {

    }

    /************************************************* CHANGE *********************************************************/
    public Change getChange(int changeID) {
        Change ret = null;
        for (Change change : changeVector) {
            if (change.getChangeID() == changeID) {
                ret = change;
            }
        }
        return ret;
    }

    public void loadAllChangesFromDB() {

    }

    /********************************************** DEPENDENT *********************************************************/

    public Dependent getDependent(int successorID, int predecessorID) {
        Dependent ret = null;

        for(Dependent dependent : dependentVector) {
            if(dependent.getSuccessorID() == successorID && dependent.getPredecessorID() == predecessorID) {
                ret = dependent;
            }
        }
        return ret;
    }

    public void insertDependentInDB(int successorID, int predecessorID, int dependency) {
        String sql = "INSERT INTO DEPENDENT(SuccessorID, PredecessorID, Dependency) VALUES (?, ?, ?)";
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, successorID);
            pstmt.setInt(2, predecessorID);
            pstmt.setInt(3, dependency);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void updateDependentInDB(int successorID, int predecessorID, int dependency) {
        String sql = "UPDATE Dependent SET Dependency = ? WHERE SucessorID = ? AND PredecessorID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dependency);
            pstmt.setInt(2, successorID);
            pstmt.setInt(3, predecessorID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void deleteDependentInDB(int successorID, int predecessorID) {
        String sql = "DELETE FROM Dependent WHERE SuccessorID = ? AND PredecessorID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, successorID);
            pstmt.setInt(2, predecessorID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void loadAllDependentsFromDB() {

    }


    /******************************************* ISSUE AFFECTING TASK *************************************************/
    public void insertIssueAffectingTaskInDB(int issueID, int taskID) {
        String sql = "INSERT INTO Issue_Affecting_Task(IssueID, TaskID) VALUES (?, ?)";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, issueID);
            pstmt.setInt(2, taskID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void deleteIssueAffectingTaskInDB(int taskID, int issueID) {
        String sql = "DELETE FROM Issue_Affecting_Task WHERE TaskID = ? AND IssueID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, taskID);
            pstmt.setInt(2, issueID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }


    public void loadAllIssueAffectingTasksFromDB() {

    }

    /*********************************************** MEETING NOTE *****************************************************/

    public void loadAllMeetingNotesFromDB() {

    }

    /********************************************* RESOURCE SKILL *****************************************************/

    public void loadAllResourceSkillsFromDB() {

    }



}
