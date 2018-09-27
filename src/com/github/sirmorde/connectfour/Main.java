/* Connect 4 game written in Java
GitHub Repository: https://github.com/SirMorde/connect-four-command-line-java
Author: Brandon Ma (branpm@gmail.com)

   To Do:
   - Write test cases for checkWin function

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
   - Fill in checkWin function (9/26)
   - Check for horizontal win (9/26)
   - Check for vertical win (9/26)
   - Check for diagonal win (9/26)

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
    9/26:
    - Implemented check win function, need to write test cases to test.
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
                            if(numOfTurns > 6 && checkWin(connectFourBoard, nextEmpty[chosenColumn], chosenColumn)) {
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
                        System.out.println("ERROR: Invalid input " + e);
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

    public static boolean checkWin(char[][] connectFourBoard, int chosenRow, int chosenColumn) {

        //System.out.println("INSIDE CHECKWIN!!"); //DEBUG

        // === HORIZONTAL SEARCH ===
        int currentColumn = chosenColumn;
        int count = 0;
        // DEBUG
        // System.out.println("chosenrow: " + chosenRow + " currentColumn: " + currentColumn + " connectFourBoard[chosenRow][currentColumn]: " + connectFourBoard[chosenRow][currentColumn]);
        // DEBUG
        // System.out.println("currentTurn.charAt(0): " + currentTurn.charAt(0));

        // Search horizontally to the right
        while(currentColumn < connectFourBoard[0].length && connectFourBoard[chosenRow][currentColumn] == currentTurn.charAt(0)) {
            currentColumn++;
            count++;
        }

        // Search horizontally to the left
        currentColumn = chosenColumn-1;
        while(currentColumn >= 0 && connectFourBoard[chosenRow][currentColumn] == currentTurn.charAt(0)) {
            currentColumn--;
            count++;
        }

        //System.out.println("count is: " + count); //DEBUG
        if(count >= 4) {
            //System.out.println("horizontal win!!!"); //DEBUG
            return true;
        }

        // === VERTICAL SEARCH ===
        // Only need to search below
        count = 0;
        int currentRow = chosenRow;
        while(currentRow < connectFourBoard.length && connectFourBoard[currentRow][chosenColumn] == currentTurn.charAt(0)) {
            currentRow++;
            count++;
        }

        //System.out.println("count is: " + count); //DEBUG
        if(count >= 4) {
            //System.out.println("vertical win!!!"); //DEBUG
            return true;
        }

        // == DIAGONAL SEARCH 1 - Top left to bottom right ==
        // Search down and to the right
        count = 0;
        currentRow = chosenRow;
        currentColumn = chosenColumn;
        while(currentRow < connectFourBoard.length && currentColumn < connectFourBoard[0].length && connectFourBoard[currentRow][currentColumn] == currentTurn.charAt(0)) {
            currentRow++;
            currentColumn++;
            count++;
        }

        // Search up and to the left - Make sure we are not in the topmost row or leftmost column
        if(chosenRow > 0 && chosenColumn > 0)  {
            currentRow = chosenRow-1;
            currentColumn = chosenColumn-1;
            while(currentRow >= 0 && currentColumn >= 0 && connectFourBoard[currentRow][currentColumn] == currentTurn.charAt(0)) {
                currentRow--;
                currentColumn--;
                count++;
            }
        }

        //System.out.println("count is: " + count); //DEBUG
        if(count >= 4) {
            //System.out.println("diagonal 1 win!!!"); //DEBUG
            return true;
        }

        // === DIAGONAL SEARCH 2 - Top right to bottom Left ===
        count = 0;
        currentRow = chosenRow;
        currentColumn = chosenColumn;

        // Search up and to the right
        while(currentRow >= 0 && currentColumn < connectFourBoard[0].length && connectFourBoard[currentRow][currentColumn] == currentTurn.charAt(0)) {
            currentRow--;
            currentColumn++;
            count++;
        }

        // Search down and to the left -  Make sure we are not in the bottommost row or leftmost column
        if (currentRow < connectFourBoard.length-1 && currentColumn > 0) {
            currentRow = chosenRow+1;
            currentColumn = chosenColumn-1;
            while(currentRow < connectFourBoard.length && currentColumn >= 0 && connectFourBoard[currentRow][currentColumn] == currentTurn.charAt(0)) {
                currentRow++;
                currentColumn--;
                count++;
            }
        }

        //System.out.println("count is: " + count); //DEBUG
        if(count >= 4) {
            //System.out.println("diagonal 2 win!!!"); //DEBUG
            return true;
        }
        return false;
    }
}
