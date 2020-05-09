package main;

public class Defaults {

    String priorityDefaults, severityDefaults, statusDefaults, categoryDefaults, impactDefaults;

    public Defaults() {}

    public Defaults(String priorityDefaults, String severityDefaults, String statusDefaults, String categoryDefaults, String impactDefaults) {
        this.priorityDefaults = priorityDefaults;
        this.severityDefaults = severityDefaults;
        this.statusDefaults = statusDefaults;
        this.categoryDefaults = categoryDefaults;
        this.impactDefaults = impactDefaults;
    }

    public String getPriorityDefaults() { return priorityDefaults; }
    public void setPriorityDefaults(String priorityDefaults) { this.priorityDefaults = priorityDefaults; }
    public String getSeverityDefaults() { return severityDefaults; }
    public void setSeverityDefaults(String severityDefaults) { this.severityDefaults = severityDefaults; }
    public String getStatusDefaults() { return statusDefaults; }
    public void setStatusDefaults(String statusDefaults) { this.statusDefaults = statusDefaults; }
    public String getCategoryDefaults() { return categoryDefaults; }
    public void setCategoryDefaults(String categoryDefaults) { this.categoryDefaults = categoryDefaults; }
    public String getImpactDefaults() { return impactDefaults; }
    public void setImpactDefaults(String impactDefaults) { this.impactDefaults = impactDefaults; }
}
