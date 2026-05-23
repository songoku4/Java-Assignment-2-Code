package entities;

public class Forklift {
    private int row;
    private int col;
    private Item carriedItem;

    public Forklift() {
        this.row = 1;
        this.col = 1;
        this.carriedItem = null;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    
    public void setPosition(int r, int c) {
        this.row = r;
        this.col = c;
    }

    public Item getCarriedItem() { return carriedItem; }
    
    public void pickUp(Item item) { this.carriedItem = item; }
    public void drop() { this.carriedItem = null; }
}