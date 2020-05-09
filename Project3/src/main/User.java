//Run Command: java -classpath ".;sqlite-jdbc-3.30.1.jar" Connect
/*
BUTTON CLICKS (Parameters show information that is available in the form that clicks the button):

public static void loginButtonClicked(String username, String password)
public static void newUserButtonClicked()                                       //Opens a form to add the users username, password & email
public static void createUserButtonClicked(String username, String password, String email)  //Checks if the username or email already exist, then inserts the new user into the database, sends a verification email to the user and opens the Projects page
public static void forgotUsernameButtonClicked(String email)    //Gets the username for the corresponding email from the authentication database and calls sendForgotUsernameEmail()
public static void forgotPasswordButtonClicked(String email)    ""
public static void createNewProjectButtonClicked(int userID, String projectName, String description)  //Inserts the project into the authentication database, creates a new Project obj and opens the Deliverable Overview page
public static void openExistingProjectButtonClicked(Project project)            //Loads all table data from the project's database, creates objects and adds them to the projects Vector members
public static int getMostRecentTableID(String tableName, String tableIDName)    //Used to the the most recent ID added to the table

PMS FUNCTIONS
Utility:
public static Connection getConnectionToAuthenticationDB()                      //Opens a Connection to the SQLite user authentication database and returns the Connection object
public static void closeConnection(Connection conn)                             //Closes the Connection object

//Authentication
public static boolean authenticate(String username, String password)            //Checks the authentication database for the username and checks that it matches the stored password
public static int getUserID(String username)                                    //Returns the userID given the username in the authentication database
public static boolean checkIFProjectNameAlreadyExists(int userID, String projectName)    //Returns true if project name already exists for the user
public static boolean checkIfUsernameExists(String username)                    //Checks the authentication database for the username. Returns true if it exists.
public static boolean checkPassword(String password)                            //Checks that the password meets microsoft standard password https://docs.microsoft.com/en-us/microsoft-365/admin/misc/password-policy-recommendations?view=o365-worldwide
public static void insertUser(String username, String password, String email)   //Inserts the user into the authentication database
public static void insertProject(String projectName, String description)        //Inserts the project into the Authentication database
public static int getNewestProjectID()                                      //Returns the projectID of the most recently added Project in the authentication database
public static void insertProjectAuthorization(int userID, int projectID)        //Inserts the project into the Authentication database
public static Vector<Project> getAllProjects(int userID)                        //Returns a vector containing all the Projects for the user from the authentication database
public static void sendForgotUsernameEmail(String username, String email)       //Sends an email to the user's email with their registered username
public static void sendForgotPasswordEmail(String password, String email)       //Sends an email to the user's email with their registered password
public static long convertDateToLong(Date date)
public static Date convertLongToDate(long dateLong)


*/


package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

public class User {

    private static Vector<Project> projectVector;
    private static int userID;

    public static Vector<Project> getProjectVector() {
        return projectVector;
    }

    public static void setProjectVector(Vector<Project> projectVector) {
        User.projectVector = projectVector;
    }

    public static Project getProject(int projectID) {
        Project ret = null;
        for (Project project : projectVector) {
            if (project.getProjectID() == projectID) {
                ret = project;
            }
        }
        return ret;
    }

    public static String loginButtonClicked(String username, String password) {
        if (checkIfUsernameExists(username)) {
            if (authenticate(username, password)) {
                User.projectVector = loadAllProjectsFromDB(getUserID(username));        //Needed to pass all projects to the Projects page
                User.userID = getUserID(username);
                //Todo: Open the Projects page for this user using the information in the loop above
                return "Good";
            } else {
                return "Password does not match Username";     //Todo: Change to pop-out window
            }
        } else {
            return "Username does not exist";      //Todo: Change to pop-out window
        }
    }

    public static Vector<Project> loadAllProjectsFromDB(int userID) {
        String sql = "SELECT Project.ProjectID, Project.Name, Project.Description, Project.Url " +
                "FROM Project, Project_Authorization " +
                "ON Project.ProjectID = Project_Authorization.projectID " +
                "WHERE Project_Authorization.UserID = ?";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Vector<Project> projectVector = new Vector<>();        //Used to hold every deliverable in the database
        Project project;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = Connect.getConnectionToDB("");
        int projectID = 0;
        String name, description, url;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userID);
            rs = pstmt.executeQuery();        //Execute the sql query and return the results to the ResultSet object

            //Add each project (row) from the project table to the project vector
            while (rs.next()) {
                projectID = rs.getInt("ProjectID");
                name = rs.getString("Name");
                description = rs.getString("Description");
                url = rs.getString("url");
                project = new Project(projectID, name, description, url);
                projectVector.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
        return projectVector;
    }

    public static void newUserButtonClicked() {
        //Todo: Open the form to enter the new user's username, password & email (form contains Create User Button)
    }

    public static String createUserButtonClicked(String username, String password, String email) {
        int userID = 0;
        if (checkIfUsernameExists(username)) {
            return "Username already exists";     //Todo: Change to pop-out dialog
        } else if (!checkPassword(password)) {
            return "Password must have at least 8 characters";     //Todo: Change to pop-out dialog
        } else {
            User.insertUserInAuthenticationDB(username, password, email);
            userID = Connect.getNewestIDFromTable(Connect.getConnectionToDB(""), "User", "UserID");
            //Todo: Open the Projects page for this newly created user
            //Todo: Send email verification to user
            return "Good";
        }
    }

    public static void forgotUsernameButtonClicked(String email) {
        String sql = "SELECT Username FROM User WHERE Email = ?";
        Connection conn = Connect.getConnectionToDB("");
        ResultSet rs;
        String username = "";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            rs.next();
            username = rs.getString("Username");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connect.closeConnection(conn);
        sendForgotUsernameEmail(username, email);
    }

    public static void forgotPasswordButtonClicked(String email) {
        String sql = "SELECT Password FROM User WHERE Email = ?";
        Connection conn = Connect.getConnectionToDB("");
        ResultSet rs;
        String password = "";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            rs.next();
            password = rs.getString("Password");
        } catch (SQLException e) {
            e.printStackTrace();     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
        sendForgotPasswordEmail(password, email);
    }

    public static void createNewProjectButtonClicked(int userID, String projectName, String description) {
        String url;

        //Every users project names must be unique
        if (checkIfProjectNameAlreadyExists(userID, projectName)) {
            System.out.println("Project Name already exists for this user.");    //Todo: Change to pop-out dialog
            return;
        }

        url = "jdbc:sqlite:" + projectName + userID + ".db";
        insertProjectInAuthenticationDB(projectName, description, url);
        int projectID = Connect.getNewestIDFromTable(Connect.getConnectionToDB(""), "Project", "ProjectID");    //url: "" species Authentication database
        insertProjectAuthorizationInDB(userID, projectID);
        Project project = new Project(projectID, projectName, description, url);

        BuildTables.buildPMSDatabase(Connect.getConnectionToDB(project.getUrl()));      //Create database tables and add them to the database at the above url

        project.insertDefaultValuesInDefaultsTableInDBAndProject();     //Add the default values for

        projectVector.add(project);

        //Todo: Open Deliverable Overview page for this project (should be blank)
    }

    public static void openExistingProjectButtonClicked(Project project) {
        project.loadAllDeliverablesFromDB();
        project.loadAllTasksFromDB();
        project.loadAllIssuesFromDB();
        project.loadAllActionItemsFromDB();
        project.loadAllResourcesFromDB();
        project.loadAllRequirementsFromDB();
        project.loadAllDecisionsFromDB();
        project.loadAllReferenceDocumentsFromDB();
        project.loadAllRisksFromDB();
        project.loadAllChangesFromDB();
        project.loadAllResourceSkillsFromDB();
        project.loadAllIssueAffectingTasksFromDB();
        project.loadAllDependentsFromDB();
        project.loadAllMeetingNotesFromDB();
        project.loadAllDefaultsFromDB();
        //Todo: Open Deliverable sheet in the projects Overview page
    }


    //Returns true if project name already exists for the user
    public static boolean checkIfProjectNameAlreadyExists(int userID, String projectName) {
        String sql = "SELECT COUNT(Project.ProjectID) " +
                "FROM Project, Project_Authorization " +
                "ON Project_Authorization.ProjectID = Project.ProjectID " +
                "WHERE Project_Authorization.UserID = ? AND " +
                "Project.Name = ?;";

        ResultSet rs;
        Connection conn = Connect.getConnectionToDB("");
        int resultCount = 0;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userID);
            pstmt.setString(2, projectName);
            rs = pstmt.executeQuery();
            rs.next();
            resultCount = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);

        if (resultCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    //Returns true if password matches the password in the database for that Username
    public static boolean authenticate(String username, String password) {
        String sql = "SELECT Password FROM User WHERE Username = ?";
        ResultSet rs = null;
        String databasePassword = "";       //Used for the password that is saved in the database
        Connection conn = Connect.getConnectionToDB("");
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            rs.next();
            databasePassword = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
        return databasePassword.equals(password);
    }

    public static int getUserID(String username) {
        String sql = "SELECT UserID FROM User WHERE Username = ?";
        ResultSet rs = null;
        int userID = 0;       //Used for the password that is saved in the database
        Connection conn = Connect.getConnectionToDB("");
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            rs.next();
            userID = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
        return userID;
    }

    public static boolean checkIfUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM User WHERE Username = ?";
        ResultSet rs = null;
        Connection conn = Connect.getConnectionToDB("");
        int count = 0;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace(); //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
        return count > 0;
    }

    public static void sendVerificationEmailToUser(String email) {
        //Todo: for final release
    }

    //Returns true if password meets microsoft standards
    public static boolean checkPassword(String password) {
        return password.length() >= 8;
    }

    public static void insertUserInAuthenticationDB(String username, String password, String email) {
        String sql = "INSERT INTO User(Username, Password, Email) VALUES (?, ?, ?)";
        Connection conn = Connect.getConnectionToDB("");
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    //Inserts the project into the Authentication database
    public static void insertProjectInAuthenticationDB(String projectName, String description, String url) {
        String sql1 = "INSERT INTO Project(Name, Description, Url) VALUES (?, ?, ?)";
        Connection conn = Connect.getConnectionToDB("");
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            pstmt.setString(1, projectName);
            pstmt.setString(2, description);
            pstmt.setString(3, url);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    //Project Authorization gives a user access to a project
    public static void insertProjectAuthorizationInDB(int userID, int projectID) {
        String sql = "INSERT INTO Project_Authorization(UserID, ProjectID) VALUES (?, ?)";
        Connection conn = Connect.getConnectionToDB("");
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userID);
            pstmt.setInt(2, projectID);
        } catch (SQLException e) {
            e.printStackTrace();     //Todo: Change to pop-out dialog
        }
        Connect.closeConnection(conn);
    }

    public static void sendForgotUsernameEmail(String username, String email) {
        //Todo: Send an email to the user with their username
    }

    public static void sendForgotPasswordEmail(String password, String email) {
        //Todo: Send an email to the user with their username
    }

    public static long convertDateToLong(Date date) {
        return date.getTime();
    }

    public static Date convertLongToDate(long dateLong) {
        Date date = new Date();
        date.setTime(dateLong);
        return date;
    }


    public static int getUserID() {
        return userID;
    }
}
