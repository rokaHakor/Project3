package ssl.pms;

import java.util.Date;

public class Decision {

    private int decisionID, resourceID, issueID, priority, impact, status;
    String uniqueID, name, description, statusDescription;
    Date dateCreated, dateNeeded, dateMade, expectedCompletionDate, actualCompletionDate, noteDate, updateDate;

    public Decision() {}

    public Decision(int decisionID, int resourceID, int issueID, int priority, int impact, int status, String name,
                    String description, String statusDescription, Date dateCreated, Date dateNeeded, Date dateMade,
                    Date expectedCompletionDate, Date actualCompletionDate, Date noteDate, Date updateDate) {

        this.decisionID = decisionID;
        this.resourceID = resourceID;
        this.issueID = issueID;
        this.priority = priority;
        this.impact = impact;
        this.status = status;
        this.name = name;
        this.description = description;
        this.statusDescription = statusDescription;
        this.dateCreated = dateCreated;
        this.dateNeeded = dateNeeded;
        this.dateMade = dateMade;
        this.expectedCompletionDate = expectedCompletionDate;
        this.actualCompletionDate = actualCompletionDate;
        this.noteDate = noteDate;
        this.updateDate = updateDate;
        uniqueID = "DEC" + decisionID;
    }

    public int getDecisionID() { return decisionID; }
    public void setDecisionID(int decisionID) {
        this.decisionID = decisionID;
        uniqueID = "DEC" + decisionID;
    }
    public String getUniqueID() { return uniqueID; }
    public int getResourceID() { return resourceID; }
    public void setResourceID(int resourceID) { this.resourceID = resourceID; }
    public int getIssueID() { return issueID; }
    public void setIssueID(int issueID) { this.issueID = issueID; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public int getImpact() { return impact; }
    public void setImpact(int impact) { this.impact = impact; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
    public Date getDateCreated() { return dateCreated; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public Date getDateNeeded() { return dateNeeded; }
    public void setDateNeeded(Date dateNeeded) { this.dateNeeded = dateNeeded; }
    public Date getDateMade() { return dateMade; }
    public void setDateMade(Date dateMade) { this.dateMade = dateMade; }
    public Date getExpectedCompletionDate() { return expectedCompletionDate; }
    public void setExpectedCompletionDate(Date expectedCompletionDate) { this.expectedCompletionDate = expectedCompletionDate; }
    public Date getActualCompletionDate() { return actualCompletionDate; }
    public void setActualCompletionDate(Date actualCompletionDate) { this.actualCompletionDate = actualCompletionDate; }
    public Date getNoteDate() { return noteDate; }
    public void setNoteDate(Date noteDate) { this.noteDate = noteDate; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
