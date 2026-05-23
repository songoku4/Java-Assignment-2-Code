package entities;
import enums.CellType;

public class WarehouseCell {
    private int row;
    private int col;
    private CellType type;
    private Shelf shelf;

    public WarehouseCell(int row, int col, CellType type) {
        this.row = row;
        this.col = col;
        this.type = type;
        if (type == CellType.SHELF) {
            this.shelf = new Shelf();
        }
    }

    public CellType getType() { return type; }
    
    public void setType(CellType type) { 
        this.type = type; 
        if (type == CellType.SHELF && this.shelf == null) {
            this.shelf = new Shelf();
        }
    }

    public Shelf getShelf() { 
        return shelf; }
}