package com.byteblox;

public abstract class Item { // Declare the class as abstract
    private String name;
    private String description;

    // Constructor with both name and description
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Constructor with only name (used by classes like Diamond)
    public Item(String name) {
        this.name = name;
        this.description = "No description"; // Default description
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Abstract method to be implemented by subclasses
    public abstract String getSymbol();
}
