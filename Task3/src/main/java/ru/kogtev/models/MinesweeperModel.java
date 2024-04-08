package ru.kogtev.models;

import ru.kogtev.controller.MinesweeperController;
import ru.kogtev.view.GameImage;

import java.util.Random;

public class MinesweeperModel {
    private int rows; // ���������� ����� �� ������� ����
    private int cols; // ���������� �������� �� ������� ����

    private int[][] board; // ������ ��� �������� ���������� � ������� (0 - ������ ������, 1-8 - ���������� ��� ������, -1 - ����)
    private boolean[][] opened; // ������ ��� ������������ �������� ������ false ������� true �������
    private boolean[][] flagged; // ������ ��� ������������ ������������� �������

    private int totalMines; // ����� ���������� ��� �� ����
    private int remainingMines; // ���������� ���������� ���
    private boolean gameOver;  // ����, ����������� �� ���������� ����
    private boolean gameWon;  // ����, ����������� �� ������ � ����
    private int timerValue;     // �������� �������
    private boolean firstClick;

    public MinesweeperModel(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
    }


    public void modelsStartNewGame() {
        gameWon = false;
        gameOver = false;
        remainingMines = totalMines;
        board = new int[rows][cols];
        opened = new boolean[rows][cols];
        flagged = new boolean[rows][cols];
        firstClick = true;
    }

    public void openCell(int x, int y) {

        if (firstClick) {
            generateBoard(x, y);
            firstClick = false;
        }

        // ���� ������ ��� ������� ��� ��������� ���� �� return
        if (opened[x][y] || flagged[x][y]) {
            return;
        }

        // ������ ������ � �������� �� ��������
        opened[x][y] = true;


        if (board[x][y] == -1) {
            gameOver = true;
        }

        if (board[x][y] == 0) {
            openAdjacentCells(x, y);
        }

        getCellImage(x, y);

        checkGameWon();

    }

    private void generateBoard(int firstX, int firstY) {
        Random random = new Random();
        int minesPlaced = 0;

        // ���������� ��� ��������� ������� �� ������� ����
        while (minesPlaced < totalMines) {

            int x = random.nextInt(rows);
            int y = random.nextInt(cols);

            // ���� ������ ����, �� ���������� ������ ������� �����
            if (firstClick && (Math.abs(x - firstX) <= 1 && Math.abs(y - firstY) <= 1)) {
                continue;
            }

            if (board[x][y] != -1) {
                board[x][y] = -1;
                minesPlaced++;
            }
        }

        // ���������� ����� ��� ����� ��� ���
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {

                if (board[x][y] != -1) {
                    int count = 0;

                    for (int i = x - 1; i <= x + 1; i++) {
                        for (int j = y - 1; j <= y + 1; j++) {

                            if (i >= 0 && i < rows && j >= 0 && j < cols && board[i][j] == -1) {
                                count++;
                            }
                        }
                    }
                    board[x][y] = count;
                }
            }
        }
    }

    public void openSurroundingCellsIfFlagged(int x, int y) {
        // ���������, ��� ���������� ������ ������� � ������ �������
        if (!isValidCell(x, y) || !opened[x][y] || board[x][y] == 0) {
            return;
        }

        int flagsAround = countFlagsAround(x, y);

        // ���������, ��� ���������� ������� ������ ����� �������� � ������� ������
        if (flagsAround == board[x][y]) {
            // ��������� ��� �������� ������ ������ �������
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int newX = x + dx;
                    int newY = y + dy;
                    // ���������, ��� ����� ���������� �������� ��������� �������� � ������ �������
                    if (isValidCell(newX, newY) && !opened[newX][newY] && !flagged[newX][newY]) {
                        openCell(newX, newY);
                    }
                }
            }
        }
    }

    private int countFlagsAround(int x, int y) {
        int count = 0;
        // ��������� ���������� ������� ������ ������
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = x + dx;
                int newY = y + dy;
                // ���������, ��� ����� ���������� �������� ��������� �������� � ��� ���������� ������
                if (isValidCell(newX, newY) && flagged[newX][newY]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void openAdjacentCells(int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (isValidCell(nx, ny) && !opened[nx][ny] && !flagged[nx][ny]) {
                    opened[nx][ny] = true;
                    if (board[nx][ny] == 0) {
                        openAdjacentCells(nx, ny);
                    }
                }
            }
        }
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }


    public GameImage getCellImage(int x, int y) {
        if (!opened[x][y]) {
            if (flagged[x][y]) {
                return GameImage.MARKED; // ����
            } else {
                return GameImage.CLOSED; // �������� ������
            }
        } else {
            switch (board[x][y]) {
                case -1:
                    return GameImage.BOMB; // ������������ ������
                case 0:
                    return GameImage.EMPTY; // ������ ������
                case 1:
                    return GameImage.NUM_1; // ������ � ������ 1
                case 2:
                    return GameImage.NUM_2; // ������ � ������ 2
                case 3:
                    return GameImage.NUM_3; // ������ � ������ 3
                case 4:
                    return GameImage.NUM_4; // ������ � ������ 4
                case 5:
                    return GameImage.NUM_5; // ������ � ������ 5
                case 6:
                    return GameImage.NUM_6; // ������ � ������ 6
                case 7:
                    return GameImage.NUM_7; // ������ � ������ 7
                case 8:
                    return GameImage.NUM_8; // ������ � ������ 8
                default:
                    throw new IllegalArgumentException("Invalid cell value: " + board[x][y]);
            }
        }
    }


    private void checkGameWon() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if (board[x][y] != -1 && !opened[x][y]) {
                    return; // ���� ���� �� ���������� ������ ��� ���, ���� �� ��������
                }
            }
        }
        gameWon = true;
    }


    public void toggleFlag(int x, int y) {
        if (remainingMines == 0 && !flagged[x][y]) {
            return;
        }

        flagged[x][y] = !flagged[x][y];
        if (flagged[x][y]) {
            remainingMines--;
        } else {
            remainingMines++;
        }
    }

    public int getRows() {
        return rows;
    }


    public int getCols() {
        return cols;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public int getRemainingMines() {
        return remainingMines;
    }

    public int getTotalMines() {
        return totalMines;
    }
}
