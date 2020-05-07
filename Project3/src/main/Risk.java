package main;

public class Risk {

    int riskID, deliverableID, category, impact;
    String uniqueID, name, mitigation, contingency, categoryDefaults, impactDefaults;
    float probability;

    public Risk(int riskID, int deliverableID, int category, int impact, String name, String mitigation, String contingency, String categoryDefaults, String impactDefaults, float probability) {
        this.riskID = riskID;
        this.deliverableID = deliverableID;
        this.category = category;
        this.impact = impact;
        this.name = name;
        this.mitigation = mitigation;
        this.contingency = contingency;
        this.categoryDefaults = categoryDefaults;
        this.impactDefaults = impactDefaults;
        this.probability = probability;
        uniqueID = "RI" + riskID;
    }

    public int getRiskID() { return riskID; }
    public void setRiskID(int riskID) { this.riskID = riskID; }
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
    public String getCategoryDefaults() { return categoryDefaults; }
    public void setCategoryDefaults(String categoryDefaults) { this.categoryDefaults = categoryDefaults; }
    public String getImpactDefaults() { return impactDefaults; }
    public void setImpactDefaults(String impactDefaults) { this.impactDefaults = impactDefaults; }
    public float getProbability() { return probability; }
    public void setProbability(float probability) { this.probability = probability; }

}