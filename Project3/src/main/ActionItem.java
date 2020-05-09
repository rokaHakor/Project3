package ssl.pms;

import java.util.Date;

public class ActionItem {

    int actionItemID, issueID, riskID, resourceID, status;
    String uniqueID, name, description, statusDescription;
    Date dateCreated, dateAssigned, expectedCompletionDate, actualCompletionDate, updateDate;

    public ActionItem() {}

    public ActionItem(int actionItemID, int issueID, int riskID, int resourceID, int status, String name,
                      String description, String statusDescription, Date dateCreated, Date dateAssigned,
                      Date expectedCompletionDate, Date actualCompletionDate, Date updateDate) {

        this.actionItemID = actionItemID;
        this.issueID = issueID;
        this.riskID = riskID;
        this.resourceID = resourceID;
        this.status = status;
        this.name = name;
        this.description = description;
        this.statusDescription = statusDescription;
        this.dateCreated = dateCreated;
        this.dateAssigned = dateAssigned;
        this.expectedCompletionDate = expectedCompletionDate;
        this.actualCompletionDate = actualCompletionDate;
        this.updateDate = updateDate;
        uniqueID = "A" + actionItemID;
    }

    public int getActionItemID() { return actionItemID; }
    public void setActionItemID(int actionItemID) {
        this.actionItemID = actionItemID;
        uniqueID = "A" + actionItemID;
    }
    public String getUniqueID() { return uniqueID; }
    public int getIssueID() { return issueID; }
    public void setIssueID(int issueID) { this.issueID = issueID; }
    public int getRiskID() { return riskID; }
    public void setRiskID(int riskID) { this.riskID = riskID; }
    public int getResourceID() { return resourceID; }
    public void setResourceID(int resourceID) { this.resourceID = resourceID; }
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
    public Date getDateAssigned() { return dateAssigned; }
    public void setDateAssigned(Date dateAssigned) { this.dateAssigned = dateAssigned; }
    public Date getExpectedCompletionDate() { return expectedCompletionDate; }
    public void setExpectedCompletionDate(Date expectedCompletionDate) { this.expectedCompletionDate = expectedCompletionDate; }
    public Date getActualCompletionDate() { return actualCompletionDate; }
    public void setActualCompletionDate(Date actualCompletionDate) { this.actualCompletionDate = actualCompletionDate; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }

}