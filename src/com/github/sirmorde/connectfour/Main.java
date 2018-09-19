package com.github.sirmorde.connectfour;

public class Main {

    public static void main(String[] args) {
        char[][] connectFourGrid = new char[6][7];
        System.out.println("Welcome to Connect 4!\n");
        showGrid(connectFourGrid);
    }

    public static void showGrid(char[][] connectFourGrid) {
        for (int i = 0; i < connectFourGrid.length; i++) {
            for (int j = 0; j < connectFourGrid[0].length; j++) {
                System.out.print("|" + connectFourGrid[i][j]);
            }
            System.out.println("|");
        }
        for (int i = 0; i < connectFourGrid.length+1; i++) {
            System.out.print("==");
        }

    }
}
