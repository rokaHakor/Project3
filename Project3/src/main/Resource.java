package ssl.pms;

public class Resource {

    int resourceID;
    String name, title, availability, uniqueID;
    float payRate;

    public Resource() {}

    public Resource(int resourceID, String name, String title, String availability, float payRate) {
        this.resourceID = resourceID;
        this.name = name;
        this.title = title;
        this.availability = availability;
        this.payRate = payRate;
        uniqueID = "RES" + resourceID;
    }

    public int getResourceID() { return resourceID; }
    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
        uniqueID = "RES" + resourceID;
    }
    public String getUniqueID() { return uniqueID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public float getPayRate() { return payRate; }
    public void setPayRate(float payRate) { this.payRate = payRate; }
}
