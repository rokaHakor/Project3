package main;

public class ReferenceDocument {

    private int referenceDocumentID, decisionID;
    private String uniqueID, name;

    public ReferenceDocument(int referenceDocumentID, int decisionID, String name) {
        this.referenceDocumentID = referenceDocumentID;
        this.decisionID = decisionID;
        this.name = name;
        uniqueID = "REF" + referenceDocumentID;
    }

    public int getReferenceDocumentID() { return referenceDocumentID; }
    public void setReferenceDocumentID(int referenceDocumentID) {
        this.referenceDocumentID = referenceDocumentID;
        uniqueID = "REF" + referenceDocumentID;
    }
    public int getDecisionID() { return decisionID; }
    public void setDecisionID(int decisionID) { this.decisionID = decisionID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
