package com.byteblox;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CommandHandler {
    private final World world;
    private final Player thief;
    private final Player police;
    private final Game game;

    private int thiefJumpsRemaining = 3;
    private int policeJumpsRemaining = 5;

    private final Queue<String> thiefMoveQueue = new LinkedList<>();
    private final Scanner scanner = new Scanner(System.in); // Single Scanner instance

    public CommandHandler(World world, Player thief, Player police, Game game) {
        this.world = world;
        this.thief = thief;
        this.police = police;
        this.game = game;

        for (int i = 0; i < 15; i++) {
            thiefMoveQueue.offer("MOVE");
        }
    }

    private boolean thiefUsedShuffle = false;

    private void handleDiamondShuffle(Player player) {
        if (player.getRole() != Player.PlayerType.THIEF) {
            System.out.println("Only the thief can use diamond shuffle.");
            return;
        }

        if (thiefUsedShuffle) {
            System.out.println("Diamond shuffle can only be used once!");
            return;
        }

        // Get current diamond positions
        java.util.List<Point> diamonds = world.getDiamondPositions(); // You need this method
        java.util.Collections.shuffle(diamonds); // Fisher-Yates shuffle using Collections.shuffle

        // Now clear all diamonds and place them at new shuffled locations
        world.clearDiamonds();
        world.placeDiamonds(diamonds); // You need this method in World.java

        thiefUsedShuffle = true;
        System.out.println("Diamonds shuffled! Their positions have been updated.");
    }

    public int getThiefMovesRemaining() {
        return thiefMoveQueue.size();
    }

    public void handle(String input, Player currentPlayer) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Please enter a valid command.");
            return;
        }

        String command = input.trim().toLowerCase();

        if (currentPlayer.getRole() == Player.PlayerType.THIEF
                && (command.startsWith("move") || command.equals("jump"))) {
            if (thiefMoveQueue.isEmpty()) {
                System.out.println("Thief has no more moves left!");
                return;
            }
        }

        switch (command) {
            case "up":
                currentPlayer.move("UP", world);
                decrementMove(currentPlayer);
                break;
            case "down":
                currentPlayer.move("DOWN", world);
                decrementMove(currentPlayer);
                break;
            case "left":
                currentPlayer.move("LEFT", world);
                decrementMove(currentPlayer);
                break;
            case "right":
                currentPlayer.move("RIGHT", world);
                decrementMove(currentPlayer);
                break;
            case "jump":
                handleJump(currentPlayer);
                break;
            case "pick":
                handlePick(currentPlayer);
                break;
            case "show":
                world.printWorld(thief, police);
                break;
            case "inventory":
                currentPlayer.showInventory();
                break;
            case "shuffle":
                if (currentPlayer.getRole() == Player.PlayerType.THIEF) {
                    world.shuffleDiamonds();
                } else {
                    System.out.println("Only the thief can use the shuffle power!");
                }
                break;

            default:
                System.out.println("Unknown command: \"" + command
                        + "\". Try: move up/down/left/right, jump, pick, show, inventory.");
                break;
        }

        if (!command.equals("inventory") && !command.equals("show")) {
            System.out.println(currentPlayer.getName() + " is at: " + currentPlayer.getPosition());
        }
    }

    private void decrementMove(Player player) {
        if (player.getRole() == Player.PlayerType.THIEF && !thiefMoveQueue.isEmpty()) {
            thiefMoveQueue.poll();
            System.out.println("Thief moves remaining: " + thiefMoveQueue.size());
            if (thiefMoveQueue.isEmpty()) {
                System.out.println("Thief has no more moves left! Game Over.");
                System.exit(0); // End the game

            }
        }
    }

    private void handleJump(Player player) {
        if (player.getRole() == Player.PlayerType.THIEF) {
            if (!thiefMoveQueue.isEmpty() && thiefJumpsRemaining > 0) {
                int jumpHeight = getUserInputForJumpHeight();
                if (jumpHeight == 2 || jumpHeight == 3) {
                    thiefJumpsRemaining--;
                    thiefMoveQueue.poll();
                    int newY = player.getY() - jumpHeight;
                    if (world.isValidCell(player.getX(), newY)) {
                        player.moveBy(0, -jumpHeight, world);
                        System.out.println(
                                "Thief jumped " + jumpHeight + " points! Jumps remaining: " + thiefJumpsRemaining);
                        System.out.println("Thief moves remaining: " + thiefMoveQueue.size());
                    } else {
                        System.out.println("Invalid jump. Out of bounds.");
                    }
                } else {
                    System.out.println("Invalid jump height. Please enter 2 or 3.");
                }
            } else {
                System.out.println("Thief cannot jump! No moves or jumps left.");
            }
        } else if (player.getRole() == Player.PlayerType.POLICE) {
            if (policeJumpsRemaining > 0) {
                int jumpHeight = getUserInputForJumpHeight();
                if (jumpHeight == 2 || jumpHeight == 3) {
                    policeJumpsRemaining--;
                    int newY = player.getY() - jumpHeight;
                    if (world.isValidCell(player.getX(), newY)) {
                        player.moveBy(0, -jumpHeight, world);
                        System.out.println(
                                "Police jumped " + jumpHeight + " points! Jumps remaining: " + policeJumpsRemaining);
                    } else {
                        System.out.println("Invalid jump. Out of bounds.");
                    }
                } else {
                    System.out.println("Invalid jump height. Please enter 2 or 3.");
                }
            } else {
                System.out.println("Police cannot jump anymore.");
            }
        }
    }

    private int getUserInputForJumpHeight() {
        while (true) {
            try {
                System.out.println("Enter jump height (2 or 3): ");
                int input = scanner.nextInt();
                if (input == 2 || input == 3) {
                    return input;
                } else {
                    System.out.println("Invalid jump height. Please enter 2 or 3.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private void handlePick(Player player) {
        int x = player.getX();
        int y = player.getY();

        if (world.hasDiamond(x, y)) {
            if (player.getRole() == Player.PlayerType.THIEF) {
                String diamondId = world.getDiamondIdAtPosition(new Point(x, y));
                if (!game.isDiamondCollected(diamondId)) {
                    world.removeDiamond(x, y);

                    Diamond diamond = new Diamond("DIAMOND");
                    player.getInventory().put("DIAMOND_" + diamondId, diamond);
                    game.collectDiamond(diamondId);

                    System.out.println(player.getName() + " picked a diamond!");
                } else {
                    System.out.println("This diamond was already collected.");
                }
            } else {
                System.out.println("Only the thief can collect diamonds.");
            }
        } else {
            System.out.println("There is nothing to pick here.");
        }
    }
}