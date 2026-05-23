package entities;

import java.util.ArrayList;
import java.util.List;

public class Shelf {
    private List<Item> items;

    public Shelf() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public List<Item> getItems() {
        return this.items;
    }
}