package ssl.pms;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BuildTables {

    public static void buildPMSDatabase(Connection conn) {
        createDeliverableTable(conn);
        createTaskTable(conn);
        createIssueTable(conn);
        createResourceTable(conn);
        createActionItemTable(conn);
        createRequirementTable(conn);
        createDecisionTable(conn);
        createReferenceDocumentTable(conn);
        createRiskTable(conn);
        createChangeTable(conn);
        createDependentTable(conn);
        createIssueAffectingTaskTable(conn);
        createResourceSkillTable(conn);
        createMeetingNoteTable(conn);
        createDefaultsTable(conn);
    }

    public static void createDeliverableTable(Connection conn) {
        String sql = "CREATE TABLE Deliverable (" +
                "DeliverableID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Due_Date INTEGER);";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createTaskTable(Connection conn) {
        String sql = "CREATE TABLE Task (" +
                "TaskID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DeliverableID INTEGER DEFAULT NULL, " +
                "ResourceID INTEGER, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Expected_Start_Date INTEGER, " +
                "Expected_End_Date INTEGER, " +
                "Expected_Duration INTEGER, " +
                "Expected_Effort INTEGER, " +
                "Actual_Start_Date INTEGER, " +
                "Actual_End_Date INTEGER, " +
                "Actual_Duration INTEGER, " +
                "Effort_Completed INTEGER, " +
                "Actual_Effort INTEGER, " +
                "Percent_Complete INTEGER, " +
                "Type INTEGER, " +
                "FOREIGN KEY(DeliverableID) REFERENCES Deliverable(DeliverableID), " +
                "FOREIGN KEY(ResourceID) REFERENCES Resource(ResourceID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createIssueTable(Connection conn) {
        String sql = "CREATE TABLE Issue (" +
                "IssueID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Priority INTEGER, " +
                "Severity INTEGER, " +
                "Date_Raised INTEGER, " +
                "Date_Assigned INTEGER, " +
                "Expected_Completion_Date INTEGER, " +
                "Actual_Completion_Date INTEGER, " +
                "Status INTEGER, " +
                "Status_Description TEXT, " +
                "Update_Date INTEGER);";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createResourceTable(Connection conn) {
        String sql = "CREATE TABLE Resource (" +
                "ResourceID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "Name TEXT, " +
                "Title TEXT, " +
                "Availability Calendar TEXT, " +
                "Pay Rate REAL);";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createActionItemTable(Connection conn) {
        String sql = "CREATE TABLE Action_Item ( " +
                "ActionItemID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "IssueID INTEGER, " +
                "RiskID INTEGER, " +
                "ResourceID INTEGER, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Date_Created INTEGER, " +
                "Date_Assigned INTEGER, " +
                "Expected_Completion_Date INTEGER, " +
                "Actual_Completion_Date INTEGER, " +
                "Status INTEGER," +
                "Status_Description TEXT," +
                "Update_Date TEXT," +
                "FOREIGN KEY(IssueID) REFERENCES Issue(IssueID)," +
                "FOREIGN KEY(RiskID) REFERENCES Risk(RiskID)" +
                "FOREIGN KEY(ResourceID) REFERENCES Resource(ResourceID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createRequirementTable(Connection conn) {
        String sql = "CREATE TABLE Requirement (" +
                "RequirementID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DeliverableID INTEGER, " +
                "Name TEXT, " +
                "Requirement_Text TEXT, " +
                "Source_Document TEXT, " +
                "Location_In_Source_Document TEXT, " +
                "Client_Reference TEXT, " +
                "FOREIGN KEY(DeliverableID) REFERENCES Deliverable(DeliverableID) " +
                ");";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createDecisionTable(Connection conn) {
        String sql = "CREATE TABLE Decision (" +
                "DecisionID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "ResourceID INTEGER, " +
                "IssueID INTEGER, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Priority INTEGER, " +
                "Impact INTEGER, " +
                "Date_Created INTEGER, " +
                "Date_Needed INTEGER, " +
                "Date_Made INTEGER, " +
                "Expected_Completion_Date INTEGER, " +
                "Actual_Completion_Date INTEGER, " +
                "Note_Date INTEGER, " +
                "Status INTEGER, " +
                "Status_Description TEXT, " +
                "Update_Date INTEGER, " +
                "FOREIGN KEY(ResourceID) REFERENCES Resource(ResourceID) " +
                "FOREIGN KEY(IssueID) REFERENCES Issue(IssueID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createReferenceDocumentTable(Connection conn) {
        String sql = "CREATE TABLE Reference_Document (" +
                "ReferenceID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DecisionID INTEGER, " +
                "Name TEXT, " +
                "FOREIGN KEY(DecisionID) REFERENCES Decision(DecisionID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createRiskTable(Connection conn) {
        String sql = "CREATE TABLE Risk (" +
                "RiskID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DeliverableID INTEGER, " +
                "Category INTEGER, " +
                "Name TEXT, " +
                "Probability REAL, " +
                "Impact INTEGER, " +
                "Mitigation TEXT, " +
                "Contingency TEXT, " +
                "Action_By INTEGER, " +
                "FOREIGN KEY(DeliverableID) REFERENCES Deliverable(DeliverableID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createChangeTable(Connection conn) {
        String sql = "CREATE TABLE Change (" +
                "ChangeID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "Name TEXT, " +
                "Date_Requested INTEGER, " +
                "Requestor TEXT, " +
                "Status INTEGER, " +
                "Update Date INTEGER);";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createIssueAffectingTaskTable(Connection conn) {
        String sql = "CREATE TABLE Issue_Affecting_Task (" +
                "TaskID INTEGER, " +
                "IssueID INTEGER, " +
                "FOREIGN KEY(TaskID) REFERENCES Task(TaskID), " +
                "PRIMARY KEY(TaskID, IssueID), " +
                "FOREIGN KEY(IssueID) REFERENCES Issue(IssueID)" +
                ");";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createDependentTable(Connection conn) {
        String sql = "CREATE TABLE Dependent (" +
                "SuccessorID INTEGER NOT NULL, " +
                "PredecessorID INTEGER NOT NULL, " +
                "Dependency INTEGER NOT NULL, " +
                "FOREIGN KEY(SuccessorID) REFERENCES Task(TaskID), " +
                "PRIMARY KEY(SuccessorID,PredecessorID), " +
                "FOREIGN KEY(PredecessorID) REFERENCES Task(TaskID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createResourceSkillTable(Connection conn) {
        String sql = "CREATE TABLE Resource_Skill (" +
                "SkillID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "ResourceID INTEGER, " +
                "Description TEXT, " +
                "FOREIGN KEY(ResourceID) REFERENCES Resource(ResourceID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createMeetingNoteTable(Connection conn) {
        String sql = "CREATE TABLE Meeting_Note (" +
                "MeetingNoteID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DecisionID INTEGER, " +
                "Note TEXT, " +
                "FOREIGN KEY(DecisionID) REFERENCES Decision(DecisionID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createDefaultsTable(Connection conn) {
        String sql = "CREATE TABLE Defaults (" +
                "Severity_Defaults TEXT, " +
                "Priority_Defaults TEXT, " +
                "Status_Defaults TEXT, " +
                "Category_Defaults TEXT, " +
                "Impact_Defaults TEXT, " +
                ");";
    }
}   //End class
