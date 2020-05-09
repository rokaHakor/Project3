package main;

public class ResourceSkill {

    int resourceID;
    String skill;

    public ResourceSkill(int resourceID, String skill) {
        this.resourceID = resourceID;
        this.skill = skill;
    }

    public int getResourceID() { return resourceID; }
    public void setResourceID(int resourceID) { this.resourceID = resourceID; }
    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }
}
