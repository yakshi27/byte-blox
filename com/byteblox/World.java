package com.byteblox;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class World {
    private int width;
    private int height;
    private Item[][] items;

    private String[][] grid;
    private Map<Point, String> diamondPositions;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.items = new Item[width][height];
        this.grid = new String[height][width];
        this.diamondPositions = new HashMap<>(); // Initialize the map
        initializeEmptyGrid();
        placeRandomDiamonds(3);
    }

    private void initializeEmptyGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                items[x][y] = null; // Initialize items array
                grid[y][x] = "."; // Initialize grid with empty cells
            }
        }
    }

    public void shuffleDiamonds() {
        // Get all current diamond positions
        List<Point> diamondPositions = getDiamondPositions();
        Random rand = new Random();

        // Clear all diamonds from the grid
        clearDiamonds();

        // Shuffle the diamond positions
        for (Point diamond : diamondPositions) {
            boolean placed = false;

            // Try to place the diamond in a new random position
            for (int attempts = 0; attempts < 10; attempts++) { // Limit attempts to avoid infinite loops
                int newX = rand.nextInt(width); // Random x-coordinate
                int newY = rand.nextInt(height); // Random y-coordinate

                if (isValidCell(newX, newY) && !hasDiamond(newX, newY)) {
                    placeDiamond(newX, newY);
                    placed = true;
                    break;
                }
            }

            // If no valid position is found, place the diamond back in its original
            // position
            if (!placed) {
                placeDiamond(diamond.x, diamond.y);
            }
        }

        System.out.println("Diamonds shuffled! Their positions have been updated.");
    }

    private void placeRandomDiamonds(int numberOfDiamonds) {
        Random rand = new Random();
        int placed = 0;

        while (placed < numberOfDiamonds) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);

            if (items[x][y] == null) { // Avoid overwriting
                placeDiamond(x, y);
                placed++;
            }
        }
    }

    public void printWorld(Player thief, Player police) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point p = new Point(x, y);
                if (thief.getPosition().equals(p)) {
                    System.out.print("  T  ");
                } else if (police.getPosition().equals(p)) {
                    System.out.print("  P  ");
                } else if (items[x][y] instanceof Diamond) {
                    System.out.print("  <> ");
                } else {
                    System.out.print("  .  ");
                }
            }
            System.out.println();
        }
    }

    public void placeDiamond(int x, int y) {
        items[x][y] = new Diamond("diamond-" + x + "-" + y);
        diamondPositions.put(new Point(x, y), "diamond-" + x + "-" + y);
    }

    public boolean isValidCell(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height; // Assuming `width` and `height` are defined
    }

    public boolean hasDiamond(int x, int y) {
        return items[x][y] instanceof Diamond;
    }

    public boolean isValid(int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    public String getDiamondIdAtPosition(Point position) {
        int x = position.x;
        int y = position.y;
        if (items[x][y] instanceof Diamond) {
            return "diamond-" + x + "-" + y;
        }
        return null;
    }

    public void removeDiamond(int x, int y) {
        if (items[x][y] instanceof Diamond) {
            items[x][y] = null;
        }
    }

    public int getWidth() {
        return width; // Assuming `width` is a field in the `World` class
    }

    public int getHeight() {
        return height; // Assuming `height` is a field in the `World` class
    }

    public int getTotalDiamonds() {
        int total = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (items[x][y] instanceof Diamond) {
                    total++;
                }
            }
        }
        return total;
    }

    public Item getItemAtPosition(Point position) {
        return items[position.x][position.y];
    }

    public void removeItem(Point position) {
        items[position.x][position.y] = null;
    }

    public void placeDiamond(int x, int y, String diamondId) {
        grid[y][x] = "<>"; // or whatever your representation is
        diamondPositions.put(new Point(x, y), diamondId); // assuming you use a Map<Point, String>
    }

    public List<Point> getDiamondPositions() {
        List<Point> diamondPositions = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] != null && grid[y][x].equals("<>")) { // Check for diamonds
                    diamondPositions.add(new Point(x, y));
                }
            }
        }
        System.out.println("Diamonds detected at: " + diamondPositions); // Debugging statement
        return diamondPositions;
    }

    public List<Point> getAllDiamondPositions() {
        List<Point> diamondPositions = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (hasDiamond(x, y)) {
                    diamondPositions.add(new Point(x, y));
                }
            }
        }
        return diamondPositions;
    }

    public void moveDiamond(Point oldPosition, Point newPosition) {
        if (hasDiamond(oldPosition.x, oldPosition.y)) {
            removeDiamond(oldPosition.x, oldPosition.y);
            placeDiamond(newPosition.x, newPosition.y);
        }
    }

    public void clearDiamonds() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] != null && grid[y][x].equals("<>")) { // Check for diamonds
                    grid[y][x] = "."; // Clear diamond from grid
                    items[x][y] = null; // Clear diamond from items array
                }
            }
        }
    }

    public void placeDiamonds(List<Point> diamonds) {
        for (Point position : diamonds) {
            int x = position.x;
            int y = position.y;
            grid[y][x] = "<>"; // Place diamond on grid
        }
    }

    private void placeNearbyEmpty(Point origin) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = origin.x + dx;
                int newY = origin.y + dy;
                if (isValidCell(newX, newY) && grid[newY][newX].equals(".")) { // Ensure cell is valid and empty
                    grid[newY][newX] = "<>"; // Place diamond
                    return;
                }
            }
        }
    }

}