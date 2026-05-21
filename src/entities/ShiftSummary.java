package entities;

public class ShiftSummary {
    private int totalDeliveries;
    private int totalWallHits;
    private int totalRestrictedHits;

    public ShiftSummary() {
        this.totalDeliveries = 0;
        this.totalWallHits = 0;
        this.totalRestrictedHits = 0;
    }

    public void addDelivery() { this.totalDeliveries++; }
    public void addWallHit() { this.totalWallHits++; }
    public void addRestrictedHit() { this.totalRestrictedHits++; }

    public int getTotalDeliveries() { return totalDeliveries; }
    public int getTotalWallHits() { return totalWallHits; }
    public int getTotalRestrictedHits() { return totalRestrictedHits; }
}