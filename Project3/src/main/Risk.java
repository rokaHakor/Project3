package main;

import java.util.Date;

public class Risk {

    int riskID, deliverableID, category, impact;
    String uniqueID, name, mitigation, contingency;
    float probability;
    Date actionBy;

    public Risk() {}

    public Risk(int riskID, int deliverableID, int category, int impact, String name, String mitigation,
                String contingency, Date actionBy, float probability) {
        this.riskID = riskID;
        this.deliverableID = deliverableID;
        this.category = category;
        this.impact = impact;
        this.name = name;
        this.mitigation = mitigation;
        this.contingency = contingency;
        this.actionBy = actionBy;
        this.probability = probability;
        uniqueID = "RI" + riskID;
    }

    public int getRiskID() { return riskID; }
    public void setRiskID(int riskID) {
        this.riskID = riskID;
        uniqueID = "RI" + riskID;
    }
    public int getDeliverableID() { return deliverableID; }
    public void setDeliverableID(int deliverableID) { this.deliverableID = deliverableID; }
    public int getCategory() { return category; }
    public void setCategory(int category) { this.category = category; }
    public int getImpact() { return impact; }
    public void setImpact(int impact) { this.impact = impact; }
    public String getUniqueID() { return uniqueID; }
    public void setUniqueID(String uniqueID) { this.uniqueID = uniqueID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMitigation() { return mitigation; }
    public void setMitigation(String mitigation) { this.mitigation = mitigation; }
    public String getContingency() { return contingency; }
    public void setContingency(String contingency) { this.contingency = contingency; }
    public float getProbability() { return probability; }
    public void setProbability(float probability) { this.probability = probability; }
    public Date getActionBy() { return actionBy; }
    public void setActionBy(Date actionBy) { this.actionBy = actionBy; }

}