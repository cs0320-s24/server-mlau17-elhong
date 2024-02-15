package main;

public class BroadBandData {
    private String stateName;
    private String countyName;
    private Double percentage;
    private String lastUpdated;

    public BroadBandData(String state, String county, Double percent, String lastUpdate){
        this.stateName = state;
        this.countyName = county;
        this.percentage = percent;
        this.lastUpdated = lastUpdate;
    }

    public Double getPercentage() {
        return this.percentage;
    }

    public String toString(){
        return "This" + this.countyName + "in the state of" + this.stateName + "has a broadband percentage of " +
                this.percentage + "was last updated on" + this.lastUpdated;
    }
}
