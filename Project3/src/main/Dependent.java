package main;

public class Dependent {

    int successorID, predecessorID, dependency;

    public Dependent(int successorID, int predecessorID, int dependency) {
        this.successorID = successorID;
        this.predecessorID = predecessorID;
        this.dependency = dependency;
    }

    public int getSuccessorID() { return successorID; }
    public void setSuccessorID(int successorID) { this.successorID = successorID; }
    public int getPredecessorID() { return predecessorID; }
    public void setPredecessorID(int predecessorID) { this.predecessorID = predecessorID; }
    public int getDependency() { return dependency; }
    public void setDependency(int dependency) { this.dependency = dependency; }
}
