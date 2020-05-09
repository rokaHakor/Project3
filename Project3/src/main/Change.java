package ssl.pms;

import java.util.Date;

public class Change {

    private int changeID, status;
    String uniqueID, name, requestor;
    Date dateRequested, updateDate;

    public Change() {}

    public Change(int changeID, int status, String name, String requestor, Date dateRequested, Date updateDate) {
        this.changeID = changeID;
        this.status = status;
        this.name = name;
        this.requestor = requestor;
        this.dateRequested = dateRequested;
        this.updateDate = updateDate;
        uniqueID = "C" + changeID;
    }

    public int getChangeID() { return changeID; }
    public void setChangeID(int changeID) {
        this.changeID = changeID;
        uniqueID = "C" + changeID;
    }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getUniqueID() { return uniqueID; }
    public void setUniqueID(String uniqueID) { this.uniqueID = uniqueID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRequestor() { return requestor; }
    public void setRequestor(String requestor) { this.requestor = requestor; }
    public Date getDateRequested() { return dateRequested; }
    public void setDateRequested(Date dateRequested) { this.dateRequested = dateRequested; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
