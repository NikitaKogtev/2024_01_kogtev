//package ru.kogtev.models;
//
//import java.util.Random;
//
//public class MinesweeperModel {
//    private int rows; // ���������� ����� �� ������� ����
//    private int cols; // ���������� �������� �� ������� ����
//
//    private int[][] board; // ������ ��� �������� ���������� � ������� (0 - ������ ������, 1-8 - ���������� ��� ������, -1 - ����)
//    private boolean[][] opened; // ������ ��� ������������ �������� ������ false ������� true �������
//    private boolean[][] flagged; // ������ ��� ������������ ������������� �������
//
//    private Random random = new Random();
//
//    private int totalMines; // ����� ���������� ��� �� ����
//    private int remainingMines; // ���������� ���������� ���
//
//    private boolean gameOver;  // ����, ����������� �� ���������� ����
//    private boolean gameWon;  // ����, ����������� �� ������ � ����\
//
//    private boolean firstClick;
//
//
//    public MinesweeperModel(int rows, int cols, int totalMines) {
//        this.rows = rows;
//        this.cols = cols;
//        this.totalMines = totalMines;
//    }
//
//
//    public void startNewGame() {
//        gameWon = false;
//        gameOver = false;
//        firstClick = true;
//
//        board = new int[rows][cols];
//        opened = new boolean[rows][cols];
//        flagged = new boolean[rows][cols];
//
//        remainingMines = totalMines;
//    }
//
//    public void openCell(int row, int col) {
//
//        if (firstClick) {
//            generateCellValueOnBoard(row, col);
//            firstClick = false;
//        }
//
//        // ���� ������ ��� ������� ��� ��������� ���� �� return
//        if (opened[row][col] || flagged[row][col]) {
//            return;
//        }
//
//        // ������ ������ � �������� �� ��������
//        opened[row][col] = true;
//
//
//        if (board[row][col] == -1) {
//            gameOver = true;
//        }
//
//        if (board[row][col] == 0) {
//            openAdjacentCells(row, col);
//        }
//
//        checkGameWon();
//
//    }
//
//    public void generateCellValueOnBoard(int row, int col) {
//
//        randomPlaceMines(row, col);
//
//        calculateValueCellWithoutMines();
//        // ���������� ����� ��� ����� ��� ���
//
//    }
//
//    public void randomPlaceMines(int row, int col) {
//        int minesPlaced = 0;
//
//        while (minesPlaced < totalMines) {
//
//            int x = this.random.nextInt(rows);
//            int y = this.random.nextInt(cols);
//
//            // ���� ������ ����, �� ���������� ������ ������� �����
//            if (firstClick && (Math.abs(x - row) <= 1 && Math.abs(y - col) <= 1)) {
//                continue;
//            }
//
//            if (board[x][y] != -1) {
//                board[x][y] = -1;
//                minesPlaced++;
//            }
//        }
//    }
//
//    public void calculateValueCellWithoutMines() {
//        for (int x = 0; x < rows; x++) {
//            for (int y = 0; y < cols; y++) {
//
//                if (board[x][y] != -1) {
//                    int count = countMinesAroundCell(x, y);
//                    board[x][y] = count;
//                }
//            }
//        }
//    }
//
//    private int countMinesAroundCell(int x, int y) {
//        int count = 0;
//
//        for (int i = x - 1; i <= x + 1; i++) {
//            for (int j = y - 1; j <= y + 1; j++) {
//                if (i >= 0 && i < rows && j >= 0 && j < cols && board[i][j] == -1) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//
//    public void toggleFlag(int x, int y) {
//        if (remainingMines == 0 && !flagged[x][y]) {
//            return;
//        }
//
//        flagged[x][y] = !flagged[x][y];
//        if (flagged[x][y]) {
//            remainingMines--;
//        } else {
//            remainingMines++;
//        }
//    }
//
//    public void openSurroundingCellsIfFlagged(int x, int y) {
//        // ���������, ��� ���������� ������ ������� � ������ �������
//        if (!isValidCell(x, y) || !opened[x][y] || board[x][y] == 0) {
//            return;
//        }
//
//        int flagsAround = countFlagsAround(x, y);
//
//        // ���������, ��� ���������� ������� ������ ����� �������� � ������� ������
//        if (flagsAround == board[x][y]) {
//            // ��������� ��� �������� ������ ������ �������
//            for (int dx = -1; dx <= 1; dx++) {
//                for (int dy = -1; dy <= 1; dy++) {
//                    int newX = x + dx;
//                    int newY = y + dy;
//                    // ���������, ��� ����� ���������� �������� ��������� �������� � ������ �������
//                    if (isValidCell(newX, newY) && !opened[newX][newY] && !flagged[newX][newY]) {
//                        openCell(newX, newY);
//                    }
//                }
//            }
//        }
//    }
//
//    private int countFlagsAround(int x, int y) {
//        int count = 0;
//        // ��������� ���������� ������� ������ ������
//        for (int dx = -1; dx <= 1; dx++) {
//            for (int dy = -1; dy <= 1; dy++) {
//                int newX = x + dx;
//                int newY = y + dy;
//                // ���������, ��� ����� ���������� �������� ��������� �������� � ��� ���������� ������
//                if (isValidCell(newX, newY) && flagged[newX][newY]) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//
//    private void openAdjacentCells(int x, int y) {
//        for (int dx = -1; dx <= 1; dx++) {
//            for (int dy = -1; dy <= 1; dy++) {
//                int nx = x + dx;
//                int ny = y + dy;

//                if (isValidCell(nx, ny) && !opened[nx][ny] && !flagged[nx][ny]) {

//                    opened[nx][ny] = true;

//                    if (board[nx][ny] == 0) {
//                        openAdjacentCells(nx, ny);
//                    }
//                }
//            }
//        }
//    }
//
//    private boolean isValidCell(int x, int y) {
//        return x >= 0 && x < rows && y >= 0 && y < cols;
//    }
//
//    private void checkGameWon() {
//        for (int x = 0; x < rows; x++) {
//            for (int y = 0; y < cols; y++) {
//                if (board[x][y] != -1 && !opened[x][y]) {
//                    return; // ���� ���� �� ���������� ������ ��� ���, ���� �� ��������
//                }
//            }
//        }
//        gameWon = true;
//    }
//
//
//    public void openAllMines() {
//        for (int x = 0; x < rows; x++) {
//            for (int y = 0; y < cols; y++) {
//                if (board[x][y] == -1) {
//                    opened[x][y] = true;
//                }
//            }
//        }
//    }
//
//    public int getRows() {
//        return rows;
//    }
//
//
//    public int getCols() {
//        return cols;
//    }
//
//    public boolean isGameOver() {
//        return gameOver;
//    }
//
//    public boolean isGameWon() {
//        return gameWon;
//    }
//
//    public int getRemainingMines() {
//        return remainingMines;
//    }
//
//    public int getTotalMines() {
//        return totalMines;
//    }
//
//    public void setRows(int rows) {
//        this.rows = rows;
//    }
//
//    public void setCols(int cols) {
//        this.cols = cols;
//    }
//
//    public int getBoard(int row, int col) {
//        return board[row][col];
//    }
//
//    public void setBoard(int[][] board) {
//        this.board = board;
//    }
//
//    public boolean getOpenedCell(int row, int col) {
//        return opened[row][col];
//    }
//
//    public void setOpened(boolean[][] opened) {
//        this.opened = opened;
//    }
//
//    public boolean getFlaggedCell(int row, int col) {
//        return flagged[row][col];
//    }
//
//    public void setFlagged(boolean[][] flagged) {
//        this.flagged = flagged;
//    }
//
//    public void setTotalMines(int totalMines) {
//        this.totalMines = totalMines;
//    }
//
//    public void setRemainingMines(int remainingMines) {
//        this.remainingMines = remainingMines;
//    }
//
//    public void setGameOver(boolean gameOver) {
//        this.gameOver = gameOver;
//    }
//
//    public void setGameWon(boolean gameWon) {
//        this.gameWon = gameWon;
//    }
//
//    public boolean isFirstClick() {
//        return firstClick;
//    }
//
//    public void setFirstClick(boolean firstClick) {
//        this.firstClick = firstClick;
//    }
//}
