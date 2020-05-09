package ssl.pms;

public class IssueAffectingTask {

    int issueID, taskID;

    public IssueAffectingTask(int issueID, int taskID) {
        this.issueID = issueID;
        this.taskID = taskID;
    }

    public int getIssueID() { return issueID; }
    public void setIssueID(int issueID) { this.issueID = issueID; }
    public int getTaskID() { return taskID; }
    public void setTaskID(int taskID) { this.taskID = taskID; }
}
