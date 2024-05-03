package ru.kogtev.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private final int rows;
    private final int cols;

    private final int totalMines;

    private final Cell[][] cells;

    private final List<CellUpdateListener> cellUpdateListeners = new ArrayList<>();

    private final Random random = new Random();

    public GameBoard(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;

        cells = new Cell[rows][cols];
        initCells();
    }

    public void initCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public void generateCellValueOnBoard(int row, int col, boolean firstClick) {
        placeMinesRandomly(row, col, firstClick);
        calculateValueCellWithoutMines();
    }

    private void placeMinesRandomly(int row, int col, boolean firstClick) {
        int minesPlaced = 0;
        int x;
        int y;

        while (minesPlaced < totalMines) {

            x = this.random.nextInt(rows);
            y = this.random.nextInt(cols);

            if (firstClick && (Math.abs(x - row) <= 1 && Math.abs(y - col) <= 1)) {
                continue;
            }

            if (cells[x][y].getBoardValue() != -1) {
                cells[x][y].setBoardValue(-1);
                minesPlaced++;
            }
        }
    }

    private void calculateValueCellWithoutMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                if (cells[row][col].getBoardValue() != -1) {
                    int count = countMinesAroundCell(row, col);
                    cells[row][col].setBoardValue(count);
                }

            }
        }
    }

    private int countMinesAroundCell(int x, int y) {
        int count = 0;

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && cells[i][j].getBoardValue() == -1) {
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
        return cells[row][col].getBoardValue();
    }


    public boolean isCellOpened(int row, int col) {
        return cells[row][col].isOpened();
    }

    public void setOpenedCellValue(int row, int col, boolean value) {
        cells[row][col].setOpened(value);
        notifyCellUpdateListeners(row, col, cells[row][col]);
    }

    public boolean getFlaggedCellValue(int row, int col) {
        return cells[row][col].isFlagged();
    }

    public void setFlaggedCellValue(int row, int col, boolean value) {
        cells[row][col].setFlagged(value);
        notifyCellUpdateListeners(row, col, cells[row][col]);
    }

    public void addCellUpdateListener(CellUpdateListener cellUpdateListener) {
        cellUpdateListeners.add(cellUpdateListener);
    }

    private void notifyCellUpdateListeners(int row, int col, Cell cell) {
        for (CellUpdateListener listener : cellUpdateListeners) {
            listener.onCellUpdate(row, col, cell);
        }
    }
}
