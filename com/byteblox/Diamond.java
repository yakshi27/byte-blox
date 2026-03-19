package com.byteblox;

public class Diamond extends Item {
    private int x;
    private int y;
    private boolean collected;

    public Diamond(String name, String description, int x, int y) {
        super(name, description); // Initialize Item attributes
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    public Diamond(String name) {
        super(name); // Overloaded constructor if description and position aren't needed
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        this.collected = true;
    }

    @Override
    public String getSymbol() {
        return "<>"; // Represent the diamond with "<>"
    }
}
