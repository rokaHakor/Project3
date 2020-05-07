/*
- No setter for unqiue ID

 */

package main;

import javax.print.attribute.IntegerSyntax;
import java.util.Date;
import java.util.Vector;

public class Deliverable {

    private int deliverableID;
    private String name, description, uniqueID;     //UniqueID is prefix + deliverablieID. Used to meet unique identifier requirement
    private Date dueDate;

    public Deliverable(int deliverableID, String name, String description, Date dueDate) {
        this.deliverableID = deliverableID;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;

        uniqueID = "DEL" + deliverableID;
    }

    //For Testing //Todo: Remove
    public void print() {
        System.out.println(deliverableID + "\t" +
                            name + "\t" +
                            description + "\t" +
                            dueDate);
    }

    //GETTERS
    public int getDeliverableID() { return deliverableID; }
    public String getUniqueID() { return uniqueID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Date getDueDate() { return dueDate; }


    //SETTERS
    public void setDeliverableID(int deliverableID) { this.deliverableID = deliverableID; uniqueID = "DEL" + deliverableID;}
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }



}
