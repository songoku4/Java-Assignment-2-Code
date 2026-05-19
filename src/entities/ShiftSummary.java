package src.entities;

public class ShiftSummary {
    private int itemsDelivered;
    private int wallHits;
    private int restrictedHits;

    public ShiftSummary() {
        this.itemsDelivered = 0;
        this.wallHits = 0;
        this.restrictedHits = 0;
    }
    
    public void addDelivery()
    {
        this.itemsDelivered++;
    }
    public void addWallHit()
    {
        this.wallHits++; 
    }
    public void addRestrictedHit()
    {
        this.restrictedHits++;
    }
    public int getItemsDelivered()
    {
        return this.itemsDelivered;
    }
    public int getWallHits() {
        return this.wallHits;
    }
    public int getRestrictedHits() {
        return this.restrictedHits; }
}