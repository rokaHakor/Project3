/*

        String sql = "";
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {

        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);

 */

package ssl.pms;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Date;

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
    Defaults defaults;
    

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
        decisionVector = new Vector<>();
        referenceDocumentVector = new Vector<>();
        riskVector = new Vector<>();
        changeVector = new Vector<>();
        dependentVector = new Vector<>();
        issueAffectingTaskVector = new Vector<>();
        meetingNoteVector = new Vector<>();
        resourceSkillVector = new Vector<>();
        defaults = new Defaults();
    }

    int getProjectID() {return projectID;}
    String getName() {return name;}
    String getDescription() {return description;}
    String getUrl() {return url;}
    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}

    public void deleteRecordFromTableInDB(String tableName, String tableIDName, int tableID) {
        String sql = "DELETE FROM ? WHERE ? = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableName);
            pstmt.setString(2, tableIDName);
            pstmt.setInt(3, tableID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void updateAttributeForTableInDB(String tableName, String attributeIDName, int rowID, int newValue) {
        String sql = "UPDATE ? SET ? = 0 WHERE TaskID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableName);
            pstmt.setString(2, attributeIDName);
            pstmt.setInt(3, rowID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    /*********************************************** DELIVERABLE ******************************************************/
    
    public void createDeliverableButtonClicked(String name, String description, Date dueDate, Vector<Integer> requirementIDVector, Vector<Integer> taskIDVector) {
        insertDeliverableInDB(name, description, dueDate);
        int deliverableID = Connect.getNewestIDFromTable(Connect.getConnectionToDB(getUrl()), "Deliverable", "DeliverableID");      //Get the id of the deliverable that was just inserted into the database (database handles primary key auto increment)
        Deliverable deliverable;
        Requirement requirement = null;
        Task task = null;

        //Create associations to any Requirements or Tasks
        // -Insert the deliverableID of the Deliverable being created into each row of the Requirement & Task tables that have been associated with it
        // -Set requirementID or TaskID for the Requirement or Task obj in the requirementVector or taskVector
        for(int requirementID : requirementIDVector) {
            updateAttributeForTableInDB("Requirement", "DeliverableID", requirementID, deliverableID);  //Update the Requirement records DeliverableID attribute in the database
            requirement = getRequirement(requirementID);        //Get the Requirement object whose association is being added
            requirement.setDeliverableID(deliverableID);        //Set the deliverableID member variable for the Requirement object
        }
        for(int taskID : taskIDVector) {
            updateAttributeForTableInDB("Task", "DeliverableID", taskID, deliverableID);
            task = getTask(taskID);
            task.setDeliverableID(deliverableID);
        }

        deliverable = new Deliverable(deliverableID, name, description, dueDate);
        deliverableVector.add(deliverable);
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

        deleteRecordFromTableInDB("Deliverable", "DeliverableID", deliverable.getDeliverableID());
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

    //Checks to see if the Requirements & Tasks associated with the Deliverable have changed & Adds or Removes them if needed
    public void updateRequirementAndTaskAssociations(Deliverable deliverable, Vector<Integer> requirementIDVector, Vector<Integer> taskIDVector) {
        
        Vector<Integer> savedRequirementIDsVector = getAllRequirementIDsAssociatedWithThisDeliverable(deliverable.getDeliverableID());
        Vector<Integer> savedTaskIDsVector = getAllTaskIDsAssociatedWithThisDeliverable(deliverable.getDeliverableID());

        // If a requirement of task has been deleted, clear the DeliverableID attribute in the DB & project
        for (Integer requirementID : savedRequirementIDsVector) {
            if (!requirementIDVector.contains(requirementID)) {
                updateAttributeForTableInDB("Requirement", "DeliverableID", requirementID, 0); //Remove the DeliverableID from the requirement in the database
                getRequirement(requirementID).setDeliverableID(0);      //Remove the deliverableID from the requirement obj in the project
            }
        }
        for (Integer taskID : savedTaskIDsVector) {
            if (!requirementIDVector.contains(taskID)) {
                updateAttributeForTableInDB("Task", "DeliverableID", taskID, 0);
                getTask(taskID).setDeliverableID(0);
            }
        }

        //If a Requirement or Task has been added, add the deliverableID to their DeliverableID attribute in DB & project
        for (Integer requirementID : requirementIDVector) {
            if (!savedRequirementIDsVector.contains(requirementID)) {    //If the requirementVector in the Deliverable does not contain the requirementID
                updateAttributeForTableInDB("Requirement", "DeliverableID", requirementID, deliverable.getDeliverableID());
                getRequirement(requirementID).setDeliverableID(deliverable.getDeliverableID());
            }
        }
        for (Integer taskID : taskIDVector) {
            if (!savedTaskIDsVector.contains(taskID)) {    //If the requirementVector in the Deliverable does not contain the requirementID
                updateAttributeForTableInDB("Task", "DeliverableID", taskID, deliverable.getDeliverableID());
                getTask(taskID).setDeliverableID(deliverable.getDeliverableID());
            }
        }
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

        checkForDateDiscrepancies(expectedStartDate, actualStartDate, expectedEndDate, actualEndDate, dependentTasksMap);

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
        createIssueAndTaskAssociations(taskID, issueIDVector, dependentTasksMap);

        //Todo: Navigate back to Task sheet
    }

    public void updateTaskButtonClicked(Task task, String name, String description, Date expectedStartDate,
                                        Date expectedEndDate, long expectedDuration, int expectedEffort, Date actualStartDate,
                                        Date actualEndDate, long actualDuration, int effortCompleted, int actualEffort,
                                        int percentComplete, int type, int deliverableID, int resourceID,
                                        Vector<Integer> issueIDVector, Map<Integer, Integer> dependentTasksMap) {

        //Todo: If expected/actual start/end date have changed, change successor tasks depending on association.

        //Checks to see is any Issue or Task associations have been added, deleted or changed
        checkForChangedIssueOrTaskAssociations(task, issueIDVector, dependentTasksMap);

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
        checkForChangedIssueOrTaskAssociations(task, issueIDsVector, dependentTasksMap);

        //Delete task in DB & project
        deleteRecordFromTableInDB("Task", "TaskID", taskID);
        taskVector.remove(task);

        //Todo: Return to Task sheet
    }

    public void checkForDateDiscrepancies(Date expectedStartDate, Date actualStartDate, Date expectedEndDate,
                                          Date actualEndDate, Map<Integer, Integer> dependentTasksMap) {

        //Make sure that there are no date discrepancies between dependent tasks:
        Set<Integer> keySet = dependentTasksMap.keySet();
        for(Integer key : keySet) {
            switch(key) {
                case 0: //Finish to Start Dependency: Check that expected/actual start date of successor is after expected/actual date of Predecessor Task
                    if(expectedStartDate.before(getTask(key).getExpectedEndDate())) {
                        System.out.println("Expected Start Date for Successor Task CANNOT be before the Expected End Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: " + key + ")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(expectedStartDate.before(getTask(key).getActualEndDate())) {
                        System.out.println("Expected Start Date for Successor Task CANNOT be before the Actual End Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(actualStartDate.before(getTask(key).getExpectedEndDate())) {
                        System.out.println("Actual Start Date for Successor Task CANNOT be before the Expected End Date of a Predecessor Task if Dependency Type is Finish To Start");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(actualStartDate.before(getTask(key).getActualEndDate())) {
                        System.out.println("Actual Start Date for Successor Task CANNOT be before the Actual End Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    break;
                case 1: //Start to Start Dependency: Check that actual/expected start date of successor task is after actual/expected start date of predecessor task
                    if(expectedStartDate.before(getTask(key).getActualStartDate())) {
                        System.out.println("Expected Start Date for Successor Task CANNOT be before the Actual End Date of a Predecessor Task if Dependency Type is Start To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(expectedStartDate.before(getTask(key).getExpectedStartDate())) {
                        System.out.println("Expected Start Date for Successor Task CANNOT be before the Expected End Date of a Predecessor Task if Dependency Type is Start To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(actualStartDate.before(getTask(key).getActualStartDate())) {
                        System.out.println("Actual Start Date for Successor Task CANNOT be before the Actual End Date of a Predecessor Task if Dependency Type is Start To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    break;
                case 2: //Start to Finish Dependency: Check that actual/expected End Date of successor task is after actual/expected start date of predecessor task
                    if(expectedEndDate.before(getTask(key).getExpectedStartDate())) {
                        System.out.println("Expected End Date for Successor Task CANNOT be before the Expected Start Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(expectedEndDate.before(getTask(key).getActualStartDate())) {
                        System.out.println("Expected End Date for Successor Task CANNOT be before the Actual Start Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(actualEndDate.before(getTask(key).getExpectedStartDate())) {
                        System.out.println("Actual End Date for Successor Task CANNOT be before the Expected Start Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(actualEndDate.before(getTask(key).getActualStartDate())) {
                        System.out.println("Actual End Date for Successor Task CANNOT be before the Actual Start Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    break;
                case 3: //Finish to Finish Dependency: Check that actual/expected End Date of successor task is after actual/expected End Date of predecessor task
                    if(expectedEndDate.before(getTask(key).getExpectedEndDate())) {
                        System.out.println("Expected End Date for Successor Task CANNOT be before the Expected Start Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(actualEndDate.before(getTask(key).getExpectedEndDate())) {
                        System.out.println("Expected End Date for Successor Task CANNOT be before the Actual Start Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(expectedEndDate.before(getTask(key).getActualEndDate())) {
                        System.out.println("Actual End Date for Successor Task CANNOT be before the Expected Start Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    else if(actualEndDate.before(getTask(key).getActualEndDate())) {
                        System.out.println("Actual End Date for Successor Task CANNOT be before the Actual Start Date of a Predecessor Task if Dependency Type is Finish To Start (TaskID: \" + key + \")");    //Todo: Change to pop-out
                        dependentTasksMap.remove(key);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void createIssueAndTaskAssociations(int taskID, Vector<Integer> issueIDVector, Map<Integer, Integer> dependentTasksMap) {
        //For every Issue associated with the task add a record into the DB & project
        for(int issueID : issueIDVector) {
            insertIssueAffectingTaskInDB(issueID, taskID);
            IssueAffectingTask iat = new IssueAffectingTask(issueID, taskID);
            issueAffectingTaskVector.add(iat);      //Add Issue Affecting Task to project
        }
        //For every Task that this Task is Dependent on, add a record into the DB & project
        Set<Integer> dependentTaskIDs = dependentTasksMap.keySet();     //Get the taskID's for all the dependent tasks being associated
        for (int dependentTaskID : dependentTaskIDs) {
            insertDependentInDB(taskID, dependentTaskID, dependentTasksMap.get(dependentTaskID));
            Dependent dependent = new Dependent(taskID, dependentTaskID, dependentTasksMap.get(dependentTaskID));
            dependentVector.add(dependent);
        }
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


    public void checkForChangedIssueOrTaskAssociations(Task task, Vector<Integer> issueIDVector, Map<Integer, Integer> dependentTasksMap) {
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

    /*********************************************** ISSUE ************************************************************/
    public void createIssueButtonClicked(int priority, int severity, int status, String name, String description,
                                         String statusDescription, Date dateRaised, Date dateAssigned, Date expectedCompletionDate, Date actualCompletionDate,
                                         Date updateDate, Vector<Integer> actionItemIDsVector, Vector<Integer> decisionIDsVector) {

        int issueID;
        insertIssueInDB(priority, severity, status, name, description, statusDescription, dateRaised, dateAssigned, expectedCompletionDate,
                actualCompletionDate, updateDate);

        issueID = Connect.getNewestIDFromTable(Connect.getConnectionToDB(getUrl()), "Issue", "IssueID");
        Issue issue = new Issue(issueID, priority, severity, status, name, description, statusDescription, dateRaised,
                dateAssigned, expectedCompletionDate, actualCompletionDate, updateDate);
        issueVector.add(issue);

        updateActionItemAndDecisionAssociations(issue, actionItemIDsVector, decisionIDsVector);
    }

    public void updateIssueButtonClicked(Issue issue, int priority, int severity, int status, String name,
                                         String description, String statusDescription, Date dateRaised,
                                         Date dateAssigned, Date expectedCompletionDate, Date actualCompletionDate,
                                         Date updateDate, Vector<Integer> actionItemIDsVector,
                                         Vector<Integer> decisionIDsVector) {

        updateIssueInDB(issue.getIssueID(), priority, severity, status, name, description, statusDescription,
                dateRaised, dateAssigned, expectedCompletionDate, actualCompletionDate, updateDate);

        updateActionItemAndDecisionAssociations(issue, actionItemIDsVector, decisionIDsVector);

        issue.updateIssue(priority, severity, status, name, description, statusDescription, dateRaised, dateAssigned,
                expectedCompletionDate, actualCompletionDate, updateDate);
    }

    public void deleteIssueButtonClicked(Issue issue) {
        //Delete associations
        Vector<Integer> requirementIDsVector = new Vector<>();
        Vector<Integer> decisionIDsVector = new Vector<>();
        updateActionItemAndDecisionAssociations(issue, requirementIDsVector, decisionIDsVector);    //Delete Action Item & Decision associations
        //Delete Task associations
        for(IssueAffectingTask iat : issueAffectingTaskVector) {
            if(iat.getIssueID() == issue.getIssueID()) {
                deleteIssueAffectingTaskInDB(iat.getTaskID(), iat.getIssueID());    //Delete from DB
                issueAffectingTaskVector.remove(iat);                               //Delete from project
            }
        }

        deleteRecordFromTableInDB("Issue", "IssueID", issue.getIssueID());      //Delete from DB
        issueVector.remove(issue);      //Delete from project
    }

    public Issue getIssue(int issueID) {
        Issue ret = null;
        for (Issue issue : issueVector) {
            if (issue.getIssueID() == issueID) {
                ret = issue;
            }
        }
        return ret;
    }

    public void updateActionItemAndDecisionAssociations(Issue issue, Vector<Integer> actionItemIDsVector, Vector<Integer> decisionIDsVector) {
        Vector<Integer> savedActionItemIDsVector = getAllActionItemsAssociatedWithThisIssue(issue.getIssueID());
        Vector<Integer> savedDecisionIDsVector = getAllDecisionsAssociatedWithThisIssue(issue.getIssueID());
        
        //Check for ADDED Action Items or Decisions associations
        for(Integer actionItemID : actionItemIDsVector) {
            if(!savedActionItemIDsVector.contains(actionItemID)) {
                updateAttributeForTableInDB("Action_Item", "IssueID", actionItemID, issue.getIssueID());
                getActionItem(actionItemID).setIssueID(issue.getIssueID());
            }
        }
        for(Integer decisionID : decisionIDsVector) {
            if(!savedDecisionIDsVector.contains(decisionID)) {
                updateAttributeForTableInDB("Decision", "IssueID", decisionID, issue.getIssueID());
                getDecision(decisionID).setIssueID(issue.getIssueID());
            }
        }
        
        //Check for DELETED Action Items or Decisions associations
        for(Integer actionItemID : savedActionItemIDsVector) {
            if(!actionItemIDsVector.contains(actionItemID)) {
                updateAttributeForTableInDB("Action_Item", "IssueID", actionItemID, 0); //Remove association
                getActionItem(actionItemID).setIssueID(0);
            }
        }
        for(Integer decisionID : savedDecisionIDsVector) {
            if(!decisionIDsVector.contains(decisionID)) {
                updateAttributeForTableInDB("Decision", "IssueID", decisionID, 0); //Remove association
                getDecision(decisionID).setIssueID(0);
            }
        }
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
                                String statusDescription, Date dateRaised, Date dateAssigned, Date expectedCompletionDate, Date actualCompletionDate,
                                Date updateDate) {

        String sql = "INSERT INTO Issue(Name, Description, Priority, Severity, Date_Raised, Date_Assigned, " +
                "Expected_Completion_Date, Actual_Completion_Date, Status, Status_Description, Update_Date) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void updateIssueInDB(int issueID, int priority, int severity, int status, String name, String description,
                                String statusDescription, Date dateRaised, Date dateAssigned,
                                Date expectedCompletionDate, Date actualCompletionDate, Date updateDate) {

        String sql = "UPDATE Issue SET Name = ?, Description = ?, Priority = ?, Severity = ?, Date_Raised = ?, " +
                "Date_Assigned = ?, Expected_Completion_Date = ?, Actual_Completion_Date = ?, Status = ?, " +
                "Status_Description = ?, Update_Date = ? WHERE IssueID = ?";
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
            pstmt.setInt(15, issueID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void loadAllIssuesFromDB() {
        String sql1 = "SELECT * FROM Issue";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Issue issue;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql1);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                issue = new Issue();
                issue.setIssueID(rs.getInt("IssueID"));
                issue.setName(rs.getString("Name"));
                issue.setDescription(rs.getString("Description"));
                issue.setPriority(rs.getInt("Priority"));
                issue.setSeverity(rs.getInt("Severity"));
                issue.setDateRaised(User.convertLongToDate(rs.getLong("Date_Raised")));
                issue.setDateAssigned(User.convertLongToDate(rs.getLong("Date_Assigned")));
                issue.setExpectedCompletionDate(User.convertLongToDate(rs.getLong("Expected_Completion_Date")));
                issue.setActualCompletionDate(User.convertLongToDate(rs.getLong("Actual_Completion_Date")));
                issue.setStatus(rs.getInt("Status"));
                issue.setStatusDescription(rs.getString("Status_Description"));
                issue.setUpdateDate(User.convertLongToDate(rs.getLong("Update_Date")));
                issueVector.add(issue);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
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

    public void createActionItemButtonClicked(int resourceID, int status, String name, String description,
                                              String statusDescription, String statusDefaults, Date dateCreated,
                                              Date dateAssigned, Date expectedCompletionDate,
                                              Date actualCompletionDate, Date updateDate) {
        //Insert in DB
        insertActionItemInDB(resourceID, status, name, description, statusDescription, dateCreated, dateAssigned,
                expectedCompletionDate, actualCompletionDate, updateDate);
        int actionItemID = Connect.getNewestIDFromTable(Connect.getConnectionToDB(getUrl()), "Action_Item", "ActionItemID");
        //Insert in Project
        ActionItem actionItem = new ActionItem(actionItemID, 0, 0, resourceID, status, name, description,
                statusDescription, dateCreated, dateAssigned, expectedCompletionDate,
                actualCompletionDate, updateDate);
        actionItemVector.add(actionItem);
    }

    public void updateActionItemButtonClicked(ActionItem actionItem, int resourceID, int status, String name, String description,
                                              String statusDescription, String statusDefaults, Date dateCreated,
                                              Date dateAssigned, Date expectedCompletionDate,
                                              Date actualCompletionDate, Date updateDate) {

        updateActionItemInDB(actionItem.getActionItemID(), resourceID, status, name, description, statusDescription,
                dateCreated, dateAssigned, expectedCompletionDate, actualCompletionDate, updateDate);

        updateActionItemInProject(actionItem, resourceID, status, name, description, statusDescription,
                dateCreated, dateAssigned, expectedCompletionDate, actualCompletionDate, updateDate);
    }

    public void deleteActionItemButtonClicked(ActionItem actionItem) {
        //Delete from DB
        deleteRecordFromTableInDB("Action_Item", "ActionItemID", actionItem.getActionItemID());
        //Delete from project
        actionItemVector.remove(actionItem);
    }

    public void insertActionItemInDB(int resourceID, int status, String name, String description,
                                     String statusDescription, java.util.Date dateCreated,
                                     java.util.Date dateAssigned, java.util.Date expectedCompletionDate,
                                     java.util.Date actualCompletionDate, java.util.Date updateDate) {

        String sql = "INSERT INTO Action_Item(IssueID, RiskID, ResourceID, Name, Description, Date_Created, Date_Assigned, " +
                "Expected_Completion_Date, Actual_Completion_Date, Status, Status_Description, Update_Date) " +
                "VALUES(0, 0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceID);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setLong(4, User.convertDateToLong(dateCreated));
            pstmt.setLong(5, User.convertDateToLong(dateAssigned));
            pstmt.setLong(6, User.convertDateToLong(expectedCompletionDate));
            pstmt.setLong(7, User.convertDateToLong(actualCompletionDate));
            pstmt.setInt(8, status);
            pstmt.setString(9, statusDescription);
            pstmt.setLong(10, User.convertDateToLong(updateDate));
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }
    
    public void loadAllActionItemsFromDB() {
        String sql1 = "SELECT * FROM Action_Item";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        ActionItem actionItem;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql1);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                actionItem = new ActionItem();
                actionItem.setActionItemID(rs.getInt("ActionItemID"));
                actionItem.setIssueID(rs.getInt("IssueID"));
                actionItem.setRiskID(rs.getInt("RiskID"));
                actionItem.setResourceID(rs.getInt("ResourceID"));
                actionItem.setName(rs.getString("Name"));
                actionItem.setDescription(rs.getString("Description"));
                actionItem.setDateCreated(User.convertLongToDate(rs.getLong("Date_Created")));
                actionItem.setDateAssigned(User.convertLongToDate(rs.getLong("Date_Assigned")));
                actionItem.setExpectedCompletionDate(User.convertLongToDate(rs.getLong("Expected_Completion_Date")));
                actionItem.setActualCompletionDate(User.convertLongToDate(rs.getLong("Actual_Completion_Date")));
                actionItem.setStatus(rs.getInt("Status"));
                actionItem.setStatusDescription(rs.getString("Status_Description"));
                actionItem.setUpdateDate(User.convertLongToDate(rs.getLong("Update_Date")));
                actionItemVector.add(actionItem);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void updateActionItemInProject(ActionItem actionItem, int resourceID, int status, String name, String description,
                                          String statusDescription, Date dateCreated,
                                          Date dateAssigned, Date expectedCompletionDate,
                                          Date actualCompletionDate, Date updateDate) {
        actionItem.setResourceID(resourceID);
        actionItem.setStatus(status);
        actionItem.setName(name);
        actionItem.setDescription(description);
        actionItem.setStatusDescription(statusDescription);
        actionItem.setDateCreated(dateCreated);
        actionItem.setDateAssigned(dateAssigned);
        actionItem.setExpectedCompletionDate(expectedCompletionDate);
        actionItem.setActualCompletionDate(actualCompletionDate);
        actionItem.setUpdateDate(updateDate);
    }

    public void updateActionItemInDB(int actionItemID, int resourceID, int status, String name, String description,
                                     String statusDescription, Date dateCreated, Date dateAssigned,
                                     Date expectedCompletionDate, Date actualCompletionDate, Date updateDate) {

        String sql = "UPDATE Action_Item SET ResourceID = ?, Name = ?, Description = ?, Date_Created = ?, Date_Assigned = ?, " +
                "Expected_Completion_Date = ?, Actual_Completion_Date = ?, Status = ?, Status_Description = ?, Update_Date = ? WHERE = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceID);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setLong(4, User.convertDateToLong(dateCreated));
            pstmt.setLong(5, User.convertDateToLong(dateAssigned));
            pstmt.setLong(6, User.convertDateToLong(expectedCompletionDate));
            pstmt.setLong(7, User.convertDateToLong(actualCompletionDate));
            pstmt.setInt(8, status);
            pstmt.setString(9, statusDescription);
            pstmt.setLong(10, User.convertDateToLong(updateDate));
            pstmt.setInt(11, actionItemID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
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

    public void createResourceButtonClicked(String name, String title, String availability, float payRate) {
        insertResourceInDB(name, title, availability, payRate);
        int resourceID = Connect.getNewestIDFromTable(Connect.getConnectionToDB(getUrl()), "Resource", "ResourceID");
        Resource resource = new Resource(resourceID, name, title, availability, payRate);
        resourceVector.add(resource);
    }

    public void updateResourceButtonClicked(Resource resource, String name, String title, String availability, float payRate) {
        updateResourceInDB(resource.getResourceID(), name, title, availability, payRate);
        updateResourceInProject(resource, name, title, availability, payRate);
    }

    public void insertResourceInDB(String name, String title, String availability, float payRate) {
        String sql = "INSERT INTO Resource(Name, Title, Availability_Calendar, Pay_Rate) VALUES(?, ?, ?, ?)";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, title);
            pstmt.setString(3, availability);
            pstmt.setFloat(4, payRate);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void updateResourceInProject(Resource resource, String name, String title, String availability, float payRate) {
        resource.setName(name);
        resource.setTitle(title);
        resource.setAvailability(availability);
        resource.setPayRate(payRate);
    }

    public void updateResourceInDB(int resourceID, String name, String title, String availability, float payRate) {
        String sql = "UPDATE Resource SET Name = ?, Title = ?, Availability_Calendar = ?, Pay_Rate = ? WHERE ResourceID = ?";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, title);
            pstmt.setString(3, availability);
            pstmt.setFloat(4, payRate);
            pstmt.setInt(5, resourceID);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void deleteResourceButtonClicked(Resource resource) {
        deleteRecordFromTableInDB("Resource", "ResourceID", resource.getResourceID());
        deleteResourceAssociations(resource);
        resourceVector.remove(resource);
    }

    public void deleteResourceAssociations(Resource resource) {
        //Delete Task associations (Change ResourceID attribute to 0)
        for(Task task : taskVector) {
            if(task.getResourceID() == resource.resourceID) {
                updateAttributeForTableInDB("Task", "ResourceID", task.getTaskID(), 0);
                task.setResourceID(0);
            }
        }
        //Delete ResourceSkill associations (Delete ResourceSkill)
        for(ResourceSkill resourceSkill : resourceSkillVector) {
            if(resourceSkill.getResourceID() == resource.getResourceID()) {
                deleteRecordFromTableInDB("Resource_Skill", "ResourceID", resourceSkill.getResourceID());
                resourceSkill.setResourceID(0);
            }
        }
        //Delete Action Item associations
        for(ActionItem actionItem : actionItemVector) {
            if(actionItem.getResourceID() == resource.getResourceID()) {
                updateAttributeForTableInDB("Action_Item", "ResourceID", actionItem.getActionItemID(), 0);
                actionItem.setResourceID(0);
            }
        }
        //Delete Decision associations
        for(Decision decision : decisionVector) {
            if(decision.getResourceID() == resource.getResourceID()) {
                updateAttributeForTableInDB("Decision", "ResourceID", decision.getDecisionID(), 0);
                decision.setResourceID(0);
            }
        }
    }

    public void loadAllResourcesFromDB() {
        String sql = "SELECT * FROM Resource";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Resource resource;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                resource = new Resource();
                resource.setResourceID(rs.getInt("ResourceID"));
                resource.setName(rs.getString("Name"));
                resource.setTitle(rs.getString("Title"));
                resource.setAvailability(rs.getString("Availability_Calendar"));
                resource.setPayRate(rs.getFloat("Pay_Rate"));
                resourceVector.add(resource);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
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
        String sql = "SELECT * FROM Requirement";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Requirement requirement;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                requirement = new Requirement();
                requirement.setRequirementID(rs.getInt("RequirementID"));
                requirement.setDeliverableID(rs.getInt("DeliverableID"));
                requirement.setName(rs.getString("Name"));
                requirement.setRequirementText(rs.getString("Requirement_Text"));
                requirement.setSourceDocument(rs.getString("Source_Document"));
                requirement.setLocationInSourceDocument(rs.getString("Location_In_Source_Document"));
                requirement.setClientReference(rs.getString("Client_Reference"));
                requirementVector.add(requirement);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
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
        String sql = "SELECT * FROM Decision";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Decision decision;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                decision = new Decision();
                decision.setDecisionID(rs.getInt("DecisionID"));
                decision.setResourceID(rs.getInt("ResourceID"));
                decision.setIssueID(rs.getInt("IssueID"));
                decision.setName(rs.getString("Name"));
                decision.setDescription(rs.getString("Description"));
                decision.setPriority(rs.getInt("Priority"));
                decision.setImpact(rs.getInt("Impact"));
                decision.setDateCreated(User.convertLongToDate(rs.getLong("Date_Created")));
                decision.setDateNeeded(User.convertLongToDate(rs.getLong("Date_Needed")));
                decision.setDateMade(User.convertLongToDate(rs.getLong("Date_Made")));
                decision.setExpectedCompletionDate(User.convertLongToDate(rs.getLong("Expected_Completion_Date")));
                decision.setActualCompletionDate(User.convertLongToDate(rs.getLong("Actual_Completion_Date")));
                decision.setNoteDate(User.convertLongToDate(rs.getLong("Note_Date")));
                decision.setStatus(rs.getInt("Status"));
                decision.setStatusDescription(rs.getString("Status_Description"));
                decision.setUpdateDate(User.convertLongToDate(rs.getLong("Update_Date")));
                decisionVector.add(decision);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
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
        String sql = "SELECT * FROM Reference_Document";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        ReferenceDocument referenceDocument;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                referenceDocument = new ReferenceDocument(rs.getInt("ReferenceDocumentID"), rs.getInt("DecisionID"), rs.getString("Name"));
                referenceDocumentVector.add(referenceDocument);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
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
        String sql = "SELECT * FROM Risk";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Risk risk;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                risk = new Risk();
                risk.setRiskID(rs.getInt("RiskID"));
                risk.setDeliverableID(rs.getInt("DeliverableID"));
                risk.setCategory(rs.getInt("Category"));
                risk.setName(rs.getString("Name"));
                risk.setProbability(rs.getInt("Probability"));
                risk.setImpact(rs.getInt("Impact"));
                risk.setMitigation(rs.getString("Mitigation"));
                risk.setContingency(rs.getString("Contingency"));
                risk.setActionBy(User.convertLongToDate(rs.getLong("Action_By")));
                riskVector.add(risk);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
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
        String sql = "SELECT * FROM Change";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Change change;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                change = new Change();
                change.setChangeID(rs.getInt("ChangeID"));
                change.setName(rs.getString("Name"));
                change.setStatus(rs.getInt("Status"));
                change.setRequestor(rs.getString("Requestor"));
                change.setDateRequested(User.convertLongToDate(rs.getLong("Date_Requested")));
                change.setUpdateDate(User.convertLongToDate(rs.getLong("Update_Date")));
                changeVector.add(change);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
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
        String sql = "SELECT * FROM Dependent";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Dependent dependent;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                dependent = new Dependent(rs.getInt("SuccessorID"), rs.getInt("PredecessorID"), rs.getInt("Dependency"));
                dependentVector.add(dependent);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
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
        String sql = "SELECT * FROM Issue_Affecting_Task";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        IssueAffectingTask issueAffectingTask;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                issueAffectingTask = new IssueAffectingTask(rs.getInt("IssueID"), rs.getInt("TaskID"));
                issueAffectingTaskVector.add(issueAffectingTask);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    /*********************************************** MEETING NOTE *****************************************************/

    public void loadAllMeetingNotesFromDB() {
        String sql = "SELECT * FROM Meeting_Note";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        MeetingNote meetingNote;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                meetingNote = new MeetingNote(rs.getInt("MeetingNoteID"), rs.getInt("DecisionID"), rs.getString("Note"));
                meetingNoteVector.add(meetingNote);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    /********************************************* RESOURCE SKILL *****************************************************/

    public void loadAllResourceSkillsFromDB() {
        String sql = "SELECT * FROM Decision";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        ResourceSkill resourceSkill;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                resourceSkill = new ResourceSkill(rs.getInt("ResourceSkillID"), rs.getString("Skill"));
                resourceSkillVector.add(resourceSkill);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    /*********************************************** DEFAULTS *********************************************************/

    public void loadAllDefaultsFromDB() {
        String sql = "SELECT * FROM Defaults";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Defaults defaults = new Defaults();
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object
            rs.next();
            defaults.setPriorityDefaults(rs.getString("Priority_Defaults"));
            defaults.setSeverityDefaults(rs.getString("Severity_Defaults"));
            defaults.setStatusDefaults(rs.getString("Status_Defaults"));
            defaults.setCategoryDefaults(rs.getString("Category_Defaults"));
            defaults.setImpactDefaults(rs.getString("Impact_Defaults"));
            this.defaults = defaults;

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public void insertDefaultValuesInDefaultsTableInDBAndProject() {
        String sql = "INSERT INTO Defaults(Priority_Defaults, Severity_Defaults, Status_Defaults, Category_Defaults, " +
                "Impact_Defaults) VALUES(?, ?, ?, ?, ?)";
        Connection conn = Connect.getConnectionToDB(getUrl());
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "High, Medium, Low");
            pstmt.setString(2, "Critical, High, Medium, Low, Minor");
            pstmt.setString(3, "Open, Closed, In Progress, Hold, Complete");
            pstmt.setString(4, "Schedule, Budget, Scope");
            pstmt.setString(5, "Critical, High, Medium, Low, Minor");
            pstmt.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);

        defaults.setPriorityDefaults("High, Medium, Low");
        defaults.setSeverityDefaults("Critical, High, Medium, Low, Minor");
        defaults.setStatusDefaults("Open, Closed, In Progress, Hold, Complete");
        defaults.setCategoryDefaults("Schedule, Budget, Scope");
        defaults.setImpactDefaults("Critical, High, Medium, Low, Minor");

    }




}
