/* Connect 4 game written in Java
GitHub Repository: https://github.com/SirMorde/connect-four-command-line-java
Author: Brandon Ma (prithvichakra@gmail.com)

   To Do:
   - Display game instructions
   - Add functionality to drop chip
   - Validate proper input for columns (Only allow 1-7)
   - Check for horizontal win
   - Check for vertical win
   - Check for diagonal win

   Edge cases:
   - If less than 8 turns, no need to check for win yet
   - If whole board is filled but no winner, reset the game

   Completed Features:
   - Support for "restart" and "exit"
   - Display help for list of commands
   - Keep track of how many turns took place
   - After a player wins, display play again text
   - Keep Red and Blue score
   - Reset score
    */

package com.github.sirmorde.connectfour;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.lang.*;

public class Main {

    // global constants
    final static int ROW = 6;
    final static int COLUMN = 7;

    // global variables
    static int redWins = 0;
    static int blueWins = 0;
    static int numOfTurns = 0;
    static String currentTurn = "INIT";
    static String userInput = "INIT";


    // game board
    static char[][] connectFourBoard;

    // scanner used to read input
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeBoard();

        // Initialize Help instructions
        Map<String, String> helpCommands = new HashMap<String, String>();
        helpCommands.put("help","Prints instructions");
        helpCommands.put("reset","Clears the game board");
        helpCommands.put("reset score","Resets the score and game board");
        helpCommands.put("exit","Closes the Game");

        while (true) {
            System.out.println("Welcome to Connect 4! Type help for list of commands");
            showBoard(connectFourBoard);

            // Randomly choose who gets the first turn
            // Math.random() returns [0,1), so * 2 will change range to [0,2)
            // (int) rounds down (floors), so the resulting value will be 0 or 1
            if((int)(Math.random()*2) == 0) {
                currentTurn = "Red";
            }
            else {
                currentTurn = "Blue";
            }

            while(true) {
                System.out.println(currentTurn + " player's turn - Choose a column (1-7) or type a command. (Type help for list of commands)");
                userInput = scanner.nextLine().toLowerCase().trim();

                if (userInput.equals("help")) {
                    for (Map.Entry<String, String> entry : helpCommands.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        System.out.printf("%-22s%-22s\n", key,value);
                    }
                    System.out.println();
                }
                else if (userInput.equals("instructions")) {
                    System.out.println("Put instructions here");
                }
                else if (userInput.equals("reset")) {
                    initializeBoard();
                    showBoard(connectFourBoard);
                }
                else if (userInput.equals("reset score")) {
                    initializeScore();
                    showBoard(connectFourBoard);
                }
                else if (userInput.equals("exit")) {
                    System.exit(0);
                }
                else {
                    break;
                }
            }

            System.out.println("Play again? (y/n)");
            // Prompt input
            userInput = scanner.nextLine().toLowerCase().trim();
            if (userInput.equals("n") || userInput.equals("no")) {
                System.out.print("Exiting...");
                System.exit(0);
            }
        }
    }

    public static void initializeBoard() {
        // Reset number of turns and clear the board
        System.out.println("Clearing board...");
        numOfTurns = 0;
        connectFourBoard = new char[ROW][COLUMN];
    }

    public static void initializeScore() {
        System.out.println("Clearing score...");
        redWins = 0;
        blueWins = 0;
    }

    public static String readInput(String playerTurn) {
        userInput = scanner.nextLine();
        return userInput;
    }

    public static void showBoard(char[][] connectFourBoard) {
        System.out.println("=====SCORE=====");
        System.out.printf("Red:%-3s Blue:%-3s\n", redWins, blueWins);
        for (int i = 0; i < connectFourBoard.length; i++) {
            for (int j = 0; j < connectFourBoard[0].length; j++) {
                System.out.print("|" + connectFourBoard[i][j]);
            }
            System.out.println("|");
        }
        for (int i = 0; i < connectFourBoard.length+1; i++) {
            System.out.print("==");
        }
        System.out.println();
    }
}
