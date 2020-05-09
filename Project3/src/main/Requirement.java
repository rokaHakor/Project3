package main;

public class Requirement {

    int requirementID, deliverableID;
    String uniqueID, name, requirementText, sourceDocument, locationInSourceDocument, clientReference;

    Requirement() {}

    Requirement(int requirementID, int deliverableID, String name, String requirementText,
                String sourceDocument, String locationInSourceDocument, String clientReference) {

        this.requirementID = requirementID;
        this.deliverableID = deliverableID;
        this.name = name;
        this.requirementText = requirementText;
        this.sourceDocument = sourceDocument;
        this.locationInSourceDocument = locationInSourceDocument;
        this.clientReference = clientReference;
        uniqueID = "REQ" + requirementID;
    }

    //GETTERS
    public int getRequirementID() { return requirementID; }
    public int getDeliverableID() { return deliverableID; }
    public String getUniqueID() { return uniqueID; }
    public String getName() { return name; }
    public String getRequirementText() { return requirementText; }
    public String getSourceDocument() { return sourceDocument; }
    public String getLocationInSourceDocument() { return locationInSourceDocument; }
    public String getClientReference() { return clientReference; }

    //SETTERS
    public void setRequirementID(int requirementID) {
        this.requirementID = requirementID;
        uniqueID = "REQ" + requirementID;
    }
    public void setDeliverableID(int deliverableID) { this.deliverableID = deliverableID; }
    public void setUniqueID(String uniqueID) { this.uniqueID = uniqueID; }
    public void setName(String name) { this.name = name; }
    public void setRequirementText(String requirementText) { this.requirementText = requirementText; }
    public void setSourceDocument(String sourceDocument) { this.sourceDocument = sourceDocument; }
    public void setLocationInSourceDocument(String locationInSourceDocument) { this.locationInSourceDocument = locationInSourceDocument; }
    public void setClientReference(String clientReference) { this.clientReference = clientReference; }
}
