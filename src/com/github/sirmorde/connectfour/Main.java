/* Connect 4 game written in Java
GitHub Repository: https://github.com/SirMorde/connect-four-command-line-java
Author: Brandon Ma (branpm@gmail.com)

   To Do:
   - Fill in checkWin function
   - Check for horizontal win
   - Check for vertical win
   - Check for diagonal win

   Completed Features:
   - Support for "restart" and "exit"
   - Display help for list of commands
   - Keep track of how many turns took place
   - After a player wins, display play again text
   - Keep Red and Blue score
   - Reset score
   - If whole board is filled but no winner, reset the game
   - Validate proper input for columns (Only allow 1-7)
   - Add functionality to drop chip (9/25)
   - Added list to keep track of which row in the chosen column to drop a chip in (9/25)
   - Alternate between player's turns (9/25)
   - Display game instructions (9/25)

   Commit Notes
   9/25:
    - Changed hashmap to linkedhashmap to preserve order in which help commands appear
    - Renamed "reset" command to "clear"
    - Renamed "reset score" command to "reset"
    - Added list "nextEmpty" to keep track of which row in the chosen column to populate
    - Alternate between player's turns
    - Offset user inputted column by 1 in order to refer to the corresponding 0-indexed column
    - Refactored and removed unused code
    - Display game instructions
    - If less than 8 turns, no need to check for win yet
*/

package com.github.sirmorde.connectfour;

import java.util.Scanner;
import java.util.Map;
import java.util.LinkedHashMap;
import java.lang.*;

public class Main {

    // global constants
    final static int ROW = 6;
    final static int COLUMN = 7;

    // global variables
    static int redWins = 0;
    static int blueWins = 0;
    static int numOfTurns = 1;
    static String currentTurn = "INIT";
    static String userInput = "INIT";

    // game board variables
    static char[][] connectFourBoard;
    static int[] nextEmpty;

    // scanner used to read input
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Initialize Help instructions
        Map<String, String> helpCommands = new LinkedHashMap<String, String>();
        helpCommands.put("help","Prints list of commands");
        helpCommands.put("instructions","Prints game instructions");
        helpCommands.put("reset","Resets the score and clears the game board");
        helpCommands.put("clear","Clears the game board");
        helpCommands.put("exit","Closes the Game");

        while(true) {
            initializeBoard();
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

                // Validate user input for one of the valid commands.
                if (userInput.equals("help")) {
                    for (Map.Entry<String, String> entry : helpCommands.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        System.out.printf("%-22s%-22s\n", key,value);
                    }
                    System.out.println();
                }
                else if(userInput.equals("instructions")) {
                    System.out.println("Be the first player to get four of your colored chips in a row - horizontally, vertically, or diagonally.");
                }
                else if(userInput.equals("reset")) {
                    initializeScore();
                    initializeBoard();
                    showBoard(connectFourBoard);
                }
                else if(userInput.equals("clear")) {
                    initializeBoard();
                    showBoard(connectFourBoard);
                }
                else if(userInput.equals("exit")) {
                    System.exit(0);
                }
                else {

                    // Validate user input for 1-7
                    try{

                        // Note:Offset chosenColumn by 1 since we refer to columns as index 0-6
                        int chosenColumn = Integer.parseInt(userInput)-1;
                        if(chosenColumn < 0 || chosenColumn > 6) {
                            System.out.println("ERROR: Input out of range. Please enter 1-7.");
                            continue;
                        }
                        else if(nextEmpty[chosenColumn] < 0) {
                            System.out.println("ERROR: The chosen column is full! Please choose a different column");
                            continue;
                        }
                        else {
                            connectFourBoard[nextEmpty[chosenColumn]][chosenColumn] = currentTurn.charAt(0);
                            if(numOfTurns > 8 && checkWin(connectFourBoard)) {
                                numOfTurns++;
                                showBoard(connectFourBoard);

                                if(currentTurn == "Blue") {
                                    blueWins++;
                                }
                                else {
                                    redWins++;
                                }
                                System.out.println(currentTurn + " wins!");
                                break;
                            }

                            nextEmpty[chosenColumn]--;
                        }
                    }
                    catch(Exception e) {
                        System.out.println("ERROR: Invalid input");
                        continue;
                    }

                    // Toggle between player's turn
                    if(currentTurn == "Blue") {
                        currentTurn = "Red";
                    }
                    else {
                        currentTurn = "Blue";
                    }

                    numOfTurns++;
                    showBoard(connectFourBoard);

                    // If board is full but no one has won, restart the game.
                    if(numOfTurns == (ROW*COLUMN+1)) {
                        System.out.println("Tie game! No one wins :(");
                        break;
                    }
                }
            }

            while(true) {
                System.out.println("Play again? (y/n)");
                userInput = scanner.nextLine().toLowerCase().trim();
                if(userInput.equals("y") || userInput.equals("yes")) {
                    break;
                }
                else if(userInput.equals("n") || userInput.equals("no")) {
                    System.out.print("Exiting...");
                    System.exit(0);
                }
                else {
                    System.out.println("Invalid input. Please enter y/yes or n/no");
                }
            }

        }
    }

    private static void initializeBoard() {
        System.out.println("Clearing board...");
        numOfTurns = 1;
        connectFourBoard = new char[ROW][COLUMN];

        // Initialize the array containing the next available spot to drop a chip for each column
        nextEmpty = new int[COLUMN];
        for(int i = 0; i < COLUMN; i++) {
            nextEmpty[i] = ROW-1;
        }
    }

    private static void initializeScore() {
        System.out.println("Resetting score...");
        redWins = 0;
        blueWins = 0;
    }

    public static void showBoard(char[][] connectFourBoard) {
        System.out.println("=====SCORE=====");
        System.out.printf("Red:%-3s Blue:%-3s Turn:%-3s\n", redWins, blueWins, numOfTurns);
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

    public static boolean checkWin(char[][] connectFourBoard) {
//        for (int i = 0; i < connectFourBoard.length; i++) {
//            for (int j = 0; j < connectFourBoard[0].length; j++) {
//                System.out.print("|" + connectFourBoard[i][j]);
//
//                // Check for horizontal win to the right
//                // Check for vertical win below
//                // Check for diagonal win to the bottom right
//                // Check for diagonal win to the bottom left
//            }
//        }
        return true;
    }
}
