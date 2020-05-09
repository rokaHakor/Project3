package main;

import java.sql.*;

public class Connect {

    public static Connection getConnectionToDB(String url) {
        String url1 = "jdbc:sqlite:PmsAuthentication.db";    //Todo: Change url to sql server
        if(url.equals("")) {
            url = url1;
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return conn;
    }

    //Closes the connection to the database that was opened when connectAuthentication() was called.
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Returns the primary key of the most recently added row in the table from the database specified by conn
    public static int getNewestIDFromTable(Connection conn, String tableName, String tableIDName) {
        String sql = "SELECT ? FROM ? WHERE ? = (SELECT MAX(?) FROM ?)";
        ResultSet rs;
        int id = 0;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableIDName);
            pstmt.setString(2, tableName);
            pstmt.setString(3, tableIDName);
            pstmt.setString(4, tableIDName);
            pstmt.setString(5, tableName);
            rs = pstmt.executeQuery();
            rs.next();
            id = rs.getInt(1);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Chance to pop-out dialog
        }
        closeConnection(conn);
        return id;
    }

}
