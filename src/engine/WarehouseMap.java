package engine;

import entities.Forklift;
import entities.WarehouseCell;
import enums.CellType;
import java.util.HashMap;
import java.util.Map;

public class WarehouseMap {

    private final int totalRows;
    private final int totalCols;

    private Map<Integer, WarehouseCell[][]> gridsByFloor;
    private Map<Integer, Forklift> forkliftsByFloor;

    public WarehouseMap(int totalFloors, int rows, int cols) {
        this.totalRows = rows;
        this.totalCols = cols;
        this.gridsByFloor = new HashMap<>();
        this.forkliftsByFloor = new HashMap<>();

        for (int f = 1; f <= totalFloors; f++) {
            WarehouseCell[][] blankGrid = new WarehouseCell[rows][cols];
            
         for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
               if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
               blankGrid[r][c] = new WarehouseCell(r, c, CellType.WALL);
                   } 
                   else {
                      blankGrid[r][c] = new WarehouseCell(r, c, CellType.AISLE);
                    }
                }
            }
            
            blankGrid[1][1] = new WarehouseCell(1, 1, CellType.START);

            this.gridsByFloor.put(f, blankGrid);
            this.forkliftsByFloor.put(f, new Forklift());
        }
    }

    public int getTotalRows() {
        return this.totalRows; 
    }
    public int getTotalCols() {
        return this.totalCols;
     }

    public WarehouseCell getCell(int floorNumber, int row, int col) {
        if (this.gridsByFloor.containsKey(floorNumber)) {
            return this.gridsByFloor.get(floorNumber)[row][col];
        }
        return null;
    }
    
    public Forklift getForkliftForFloor(int floorNumber) {
        return this.forkliftsByFloor.get(floorNumber);
    }

    public void printCurrentState() {
        System.out.println("Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift");

        for (int floorNum = 1; floorNum <= 3; floorNum++) {
            if (!this.gridsByFloor.containsKey(floorNum)) {
                continue;
            }

            WarehouseCell[][] grid = this.gridsByFloor.get(floorNum);
            Forklift currentForklift = this.forkliftsByFloor.get(floorNum);

            System.out.println("\n==========Floor: " + floorNum + "==========");
            System.out.println("\nForklift at: (" + currentForklift.getRow() + "," + currentForklift.getCol() + ")");

            for (int r = 0; r < this.totalRows; r++) {
                for (int c = 0; c < this.totalCols; c++) {
                    if (currentForklift.getRow() == r && currentForklift.getCol() == c) {
                        System.out.print("F ");
                    } else {
                        WarehouseCell cell = grid[r][c];
                        System.out.print(getSymbolForType(cell.getType()) + " ");
                    }
                }
                System.out.println();
            }
        }
    }

    private char getSymbolForType(CellType type) {
        switch (type) {
            case WALL: return '#';
            case AISLE: return '.';
            case RESTRICTED: return 'X';
            case SHELF: return 'S';
            case START: return 'O';
            default: return '.';
        }
    }


    public void printSingleFloor(int floorNum) {
        if (!this.gridsByFloor.containsKey(floorNum)) {
            return;
        }

        WarehouseCell[][] grid = this.gridsByFloor.get(floorNum);
        Forklift currentForklift = this.forkliftsByFloor.get(floorNum);

        System.out.println("\nForklift at: (" + currentForklift.getRow() + "," + currentForklift.getCol() + ")");

        for (int r = 0; r < this.totalRows; r++) {
            for (int c = 0; c < this.totalCols; c++) {
                if (currentForklift.getRow() == r && currentForklift.getCol() == c) {
                    System.out.print("F ");
                } else {
                    WarehouseCell cell = grid[r][c];
                    System.out.print(getSymbolForType(cell.getType()) + " ");
                }
            }
            System.out.println();
        }
    }
}