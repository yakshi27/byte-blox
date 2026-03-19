package com.byteblox;

import java.awt.Point;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Game {

    private final World world;
    private final Player thief;
    private final Player police;
    private boolean gameOver = false; // Track if the game is over

    private int diamondsCollected = 0;
    private final int TOTAL_DIAMONDS;
    private final Set<String> collectedDiamonds = new HashSet<>();

    public Game(World world, Player thief, Player police) {
        this.world = world;
        this.thief = thief;
        this.police = police;
        this.TOTAL_DIAMONDS = world.getTotalDiamonds();
    }

    public void endGame(String message) {
        System.out.println(message);
        setGameOver(true); // Set the gameOver flag to true
    }

    public void restartGame() {
        Main.main(null); // calls main again to reset game
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            CommandHandler handler = new CommandHandler(world, thief, police, this);
            boolean isThiefTurn = true;

            while (true) {
                world.printWorld(thief, police);
                System.out.println((isThiefTurn ? "Thief" : "Police") + "'s turn.");

                // Ask for the next command
                System.out.print("Enter command: ");
                String command = scanner.nextLine().trim();

                // Determine the current player
                Player currentPlayer = isThiefTurn ? thief : police;

                // Handle the command
                handler.handle(command, currentPlayer);

                // Police catches thief
                if (thief.getPosition().equals(police.getPosition())) {
                    System.out.println(
                            "The police caught the thief!\n THE END \n Music Plays: \n~ FEELING PROUD INDIAN ARMY\n JANG KE MAIDAN MAI KADE NA HARDE~\n");
                    break;
                }

                if (diamondsCollected == TOTAL_DIAMONDS) {
                    System.out.println("Thief collected all diamonds! GAME OVER \n********** THIEF WINS **********\n");
                    break; // Exit the loop
                }

                // Switch turn
                isThiefTurn = !isThiefTurn;
            }
        }
    }

    public void collectDiamond(String diamondId) {
        if (!collectedDiamonds.contains(diamondId)) {
            collectedDiamonds.add(diamondId);
            diamondsCollected++;
            System.out.println("Thief collected a diamond! Total: " + diamondsCollected);
        }
    }

    public boolean isDiamondCollected(String diamondId) {
        return collectedDiamonds.contains(diamondId);
    }
}