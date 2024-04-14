package ru.kogtev.models;

import java.util.Random;

public class BoardModel {
    private int rows;
    private int cols;

    private int totalMines;

    private int[][] board; // массив для хранения информации о клетках (0 - пустая клетка, 1-8 - количество мин вокруг, -1 - мина)
    private boolean[][] opened; // массив для отслеживания открытых клеток false закрыта true открыта
    private boolean[][] flagged; // массив для отслеживания установленных флажков

    private Random random = new Random();

    private boolean firstClick = true;

    public BoardModel(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;

        board = new int[rows][cols];
        opened = new boolean[rows][cols];
        flagged = new boolean[rows][cols];

    }


    public void generateCellValueOnBoard(int row, int col) {

        placeMinesRandomly(row, col);

        calculateValueCellWithoutMines();
        // Вычисление чисел для ячеек без мин

    }

    private void placeMinesRandomly(int row, int col) {
        int minesPlaced = 0;

        while (minesPlaced < totalMines) {

            int x = this.random.nextInt(rows);
            int y = this.random.nextInt(cols);

            // Если первый клик, то игнорируем клетку первого клика
            if (firstClick && (Math.abs(x - row) <= 1 && Math.abs(y - col) <= 1)) {
                continue;
            }

            if (board[x][y] != -1) {
                board[x][y] = -1;
                minesPlaced++;
            }
        }
    }

    private void calculateValueCellWithoutMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                if (board[row][col] != -1) {
                    int count = countMinesAroundCell(row, col);
                    board[row][col] = count;
                }
            }
        }
    }

    private int countMinesAroundCell(int x, int y) {
        int count = 0;

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && board[i][j] == -1) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getTotalMines() {
        return totalMines;
    }

    public int getBoardCellValue(int row, int col) {
        return board[row][col];
    }

    public void setBoardCellValue(int row, int col, int value) {
        board[row][col] = value;
    }

    public boolean getOpenedCellValue(int row, int col) {
        return opened[row][col];
    }

    public void setOpenedCellValue(int row, int col, boolean value) {
        opened[row][col] = value;
    }

    public void setFlaggedCellValue(int row, int col, boolean value) {
        flagged[row][col] = value;
    }


    public boolean getFlaggedCellValue(int row, int col) {
        return flagged[row][col];
    }
}
