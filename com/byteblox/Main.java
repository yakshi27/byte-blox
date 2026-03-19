package com.byteblox;

public class Main {
    public static void main(String[] args) {
        System.out.println(
                "Welcome to ByteBlox: \nThief & Police Edition \n The game instructions are as follows: \n 1) Choose your roles in reality.\n"
                        + //
                        " 2) Use up, down, left, right to move. \n 3) Use Jump to jump 2 or 3 block above.\n 4) Use pick to Pick the diamond.\n 5) Use show to show the world.\n 6) Use inventory to show the diamonds you collected.\n 7) Thief has total 15 moves to collect all diamonds.\n 8) Thief wins if he collected all diamonds.\n 9) Police wins if he catches the thief.\n10) Have fun!!!\n\n ");

        // Set default initials for Thief and Police
        String thiefInitials = "T";
        String policeInitials = "P";

        // Create a 10x10 world
        World world = new World(10, 10);

        // Create players with different starting positions and roles
        Player thief = new Player(thiefInitials, 1, 1, Player.PlayerType.THIEF); // Top-left corner
        Player police = new Player(policeInitials, 8, 8, Player.PlayerType.POLICE); // Bottom-right corner

        // Start the game
        Game game = new Game(world, thief, police);
        game.start();

    }
}