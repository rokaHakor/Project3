package main;

import java.util.Date;

public class Issue {

    int issueID, priority, severity, status;
    String uniqueID, name, description, statusDescription;
    Date dateRaised, dateAssigned, expectedCompletionDate, actualCompletionDate, updateDate;

    public Issue() {}

    public Issue(int issueID, int priority, int severity, int status, String name, String description,
                 String statusDescription, Date dateRaised, Date dateAssigned, Date expectedCompletionDate,
                 Date actualCompletionDate, Date updateDate) {

        this.issueID = issueID;
        this.priority = priority;
        this.severity = severity;
        this.status = status;
        this.name = name;
        this.description = description;
        this.statusDescription = statusDescription;
        this.dateRaised = dateRaised;
        this.dateAssigned = dateAssigned;
        this.expectedCompletionDate = expectedCompletionDate;
        this.actualCompletionDate = actualCompletionDate;
        this.updateDate = updateDate;
        uniqueID = "I" + issueID;
    }

    public int getIssueID() { return issueID; }
    public String getUniqueID() { return uniqueID; }
    public void setIssueID(int issueID) {
        this.issueID = issueID;
        uniqueID = "I" + issueID;
    }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public int getSeverity() { return severity; }
    public void setSeverity(int severity) { this.severity = severity; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
    public Date getDateRaised() { return dateRaised; }
    public void setDateRaised(Date dateRaised) { this.dateRaised = dateRaised; }
    public Date getDateAssigned() { return dateAssigned; }
    public void setDateAssigned(Date dateAssigned) { this.dateAssigned = dateAssigned; }
    public Date getExpectedCompletionDate() { return expectedCompletionDate; }
    public void setExpectedCompletionDate(Date expectedCompletionDate) { this.expectedCompletionDate = expectedCompletionDate; }
    public Date getActualCompletionDate() { return actualCompletionDate; }
    public void setActualCompletionDate(Date actualCompletionDate) { this.actualCompletionDate = actualCompletionDate; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }

    public void updateIssue(int priority, int severity, int status, String name, String description,
                            String statusDescription, Date dateRaised, Date dateAssigned, Date expectedCompletionDate, Date actualCompletionDate,
                            Date updateDate) {

        setPriority(priority);
        setSeverity(severity);
        setStatus(status);
        setName(name);
        setDescription(description);
        setStatusDescription(statusDescription);
        setDateRaised(dateRaised);
        setDateAssigned(dateAssigned);
        setExpectedCompletionDate(expectedCompletionDate);
        setActualCompletionDate(actualCompletionDate);
        setUpdateDate(updateDate);
    }

}
