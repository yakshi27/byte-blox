package com.byteblox;

import java.util.ArrayList;

public class Inventory {
    private final ArrayList<String> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void showInventory() {
        if (items.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory: " + items);
        }
    }

    public boolean hasItem(String item) {
        return items.contains(item);
    }
}
