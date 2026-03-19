package com.byteblox;

import java.awt.Point;
import java.util.HashMap;

public class Player {
    private final String name;
    private int x, y;
    private final PlayerType role;
    private final HashMap<String, Item> inventory = new HashMap<>();
    private final Point startingPosition; // Store the player's starting position

    public Player(String name, int x, int y, PlayerType role) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.role = role;
        this.startingPosition = new Point(x, y); // Set the starting position
    }

    private int moveCount = 0;

    public void incrementMove() {
        moveCount++;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void reset() {
        // Reset the player's position to the starting position
        this.x = startingPosition.x;
        this.y = startingPosition.y;

        // Clear the player's inventory
        inventory.clear();

        System.out.println(name + " has been reset to the starting position: " + startingPosition);
    }

    public enum PlayerType {
        POLICE,
        THIEF
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public void setPosition(Point position) {
        this.x = position.x;
        this.y = position.y;
    }

    public PlayerType getRole() {
        return role;
    }

    public HashMap<String, Item> getInventory() {
        return inventory;
    }

    public void moveBy(int dx, int dy, World world) {
        int newX = x + dx;
        int newY = y + dy;

        // Ensure the new position is valid
        if (world.isValidCell(newX, newY)) {
            x = newX;
            y = newY;
        } else {
            System.out.println("Invalid move. Out of bounds.");
        }
    }

    public void move(String direction, World world) {
        switch (direction.toUpperCase()) {
            case "UP":
                if (y == 0) {
                    y = world.getHeight() - 1; // Wrap around to the bottom
                } else if (canMove(0, -1, world)) {
                    y--;
                } else {
                    System.out.println("Cannot move UP.");
                }
                break;
            case "DOWN":
                if (y == world.getHeight() - 1) {
                    y = 0; // Wrap around to the top
                } else if (canMove(0, 1, world)) {
                    y++;
                } else {
                    System.out.println("Cannot move DOWN.");
                }
                break;
            case "LEFT":
                if (x == 0) {
                    x = world.getWidth() - 1; // Wrap around to the right
                } else if (canMove(-1, 0, world)) {
                    x--;
                } else {
                    System.out.println("Cannot move LEFT.");
                }
                break;
            case "RIGHT":
                if (x == world.getWidth() - 1) {
                    x = 0; // Wrap around to the left
                } else if (canMove(1, 0, world)) {
                    x++;
                } else {
                    System.out.println("Cannot move RIGHT.");
                }
                break;
            default:
                System.out.println("Invalid direction.");
        }
    }

    public boolean canMove(int dx, int dy, World world) {
        int newX = x + dx;
        int newY = y + dy;
        return world.isValidCell(newX, newY);
    }

    public void jump(int height, World world) {
        if (canMove(0, -height, world)) {
            y -= height;
            System.out.println(name + " jumped up " + height + " blocks.");
        } else {
            System.out.println("Jump failed. Out of bounds.");
        }
    }

    public void collectItem(Item item) {
        inventory.put(item.getName(), item);
        System.out.println(name + " collected: " + item.getName());
    }

    public boolean hasItem(String itemName) {
        return inventory.containsKey(itemName);
    }

    public void useItem(String itemName) {
        if (hasItem(itemName)) {
            System.out.println(name + " used: " + inventory.get(itemName).getName());
            inventory.remove(itemName);
        } else {
            System.out.println("Item not found in inventory.");
        }
    }

    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println(name + "'s inventory is empty.");
        } else {
            System.out.println(name + "'s Inventory:");
            for (Item item : inventory.values()) {
                System.out.println(" - " + item.getName());
            }
        }
    }

    public void catchPlayer(Player other) {
        if (this.role == PlayerType.POLICE && other.role == PlayerType.THIEF) {
            System.out.println(this.name + " caught " + other.name + " the thief!");
        } else {
            System.out.println(this.name + " can't catch " + other.name + ".");
        }
    }
}