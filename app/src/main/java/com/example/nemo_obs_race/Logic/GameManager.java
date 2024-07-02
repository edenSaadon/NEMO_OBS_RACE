package com.example.nemo_obs_race.Logic;

import android.util.Log;

import java.util.Arrays;
import java.util.Random;

public class GameManager {
    public static final int GRID_ROWS = 10;
    public static final int GRID_COLS = 5;
    public static final int EMPTY = 0;
    public static final int NEMO = 1;
    public static final int OBSTACLES = 2;
    public static final int COIN = 3;
    private int score;
    private final int[][] grid;
    private final int NemoRow;
    private int NemoCol;
    private int lives;
    private int rowsMoved;
    private double distance;

    public GameManager(int lives) {
        this.lives = lives;
        this.NemoRow = GRID_ROWS - 1;
        this.NemoCol = 2;
        this.rowsMoved = 0;
        this.score = 0;
        this.distance = 0;
        this.grid = new int[GRID_ROWS][GRID_COLS];
        initGrid();
    }

    private void initGrid() {
        for (int i = 0; i < GRID_ROWS; i++) {
            Arrays.fill(grid[i], EMPTY);
        }
        grid[NemoRow][NemoCol] = NEMO;
    }

    public void moveNLeft() {
        if (NemoCol > 0) {
            moveNemo(NemoCol - 1);
        }
    }

    public void moveNRight() {
        if (NemoCol < GRID_COLS - 1) {
            moveNemo(NemoCol + 1);
        }
    }

    private void moveNemo(int newCol) {
        grid[NemoRow][NemoCol] = EMPTY;
        NemoCol = newCol;
        grid[NemoRow][NemoCol] = NEMO;
    }

    public void moveObstaclesDown() {
        Log.d("GameManager", "Moving obstacles down");

        for (int row = GRID_ROWS - 2; row >= 0; row--) {
            System.arraycopy(grid[row], 0, grid[row + 1], 0, GRID_COLS);
        }

        Arrays.fill(grid[0], EMPTY);

        rowsMoved++;
        if (rowsMoved >= 2) {
            generateNewObstacleOrCoin();
            rowsMoved = 0;
        }
        distance += 0.01; // update distance in kilometers
    }

    private void generateNewObstacleOrCoin() {
        Random random = new Random();
        int randomCol = random.nextInt(GRID_COLS);
        if (random.nextBoolean()) {
            grid[0][randomCol] = OBSTACLES;
        } else {
            grid[0][randomCol] = COIN;
        }
    }

    public boolean checkCollision() {
        boolean collision = grid[NemoRow][NemoCol] == OBSTACLES;
        if (collision) {
            Log.d("GameManager", "Collision detected at (" + NemoRow + ", " + NemoCol + ")");
        } else {
            Log.d("GameManager", "No collision at (" + NemoRow + ", " + NemoCol + ")");
        }
        return collision;
    }

    public boolean collectCoin() {
        boolean collected = grid[NemoRow][NemoCol] == COIN;
        if (collected) {
            grid[NemoRow][NemoCol] = EMPTY;
            score++;
            Log.d("GameManager", "Coin collected at (" + NemoRow + ", " + NemoCol + ")");
        }
        return collected;
    }

    public int getLives() {
        return lives;
    }

    public void decreaseLives() {
        if (lives > 0) {
            lives--;
        }
    }

    public boolean isGameEnded() {
        return lives == 0;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getNemoCol() {
        return NemoCol;
    }

    public void reset() {
        lives = 3;
        initGrid();
    }

    public int getScore() {
        return score;
    }

    public double getDistance() {
        return distance;
    }
}
