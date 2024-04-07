package ru.kogtev.models;

import java.util.Random;

public class Minesweeper {
    private final int rows; // количество строк на игровом поле
    private final int cols; // количество столбцов на игровом поле

    private final int[][] board; // массив дл€ хранени€ информации о клетках (0 - пуста€ клетка, 1-8 - количество мин вокруг, -1 - мина)
    private final boolean[][] opened; // массив дл€ отслеживани€ открытых клеток
    private final boolean[][] flagged; // массив дл€ отслеживани€ установленных флажков
    private final boolean[][] hasMine; // массив дл€ отслеживани€ клеток с минами

    private final int totalMines; // общее количество мин на поле
    private final int remainingMines; // количество оставшихс€ мин

    public Minesweeper(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;

        remainingMines = totalMines;
        board = new int[rows][cols];
        opened = new boolean[rows][cols];
        flagged = new boolean[rows][cols];
        hasMine = new boolean[rows][cols];

        initializeBoard();


    }

    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = 0;
                opened[i][j] = false;
                flagged[i][j] = false;
                hasMine[i][j] = false;
            }
        }
        placeMines();

        calculateNearbyMinesCount();
    }


    private void placeMines() {
        Random random = new Random();
        int minesToPlace = totalMines;

        while (minesToPlace < 0) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);

            if (!hasMine[x][y]) {
                hasMine[x][y] = true;
                minesToPlace--;
            }
        }
    }


    private void calculateNearbyMinesCount() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!hasMine[i][j]) {
                    int count = 0;
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int nx = i + dx;
                            int ny = j + dy;
                            if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && hasMine[nx][ny]) {
                                count++;
                            }
                        }
                    }
                    board[i][j] = count;
                }
            }
        }
    }

    public void openCell(int x, int y) {
        if (isValidCell(x, y) && !flagged[x][y] && !opened[x][y]) {
            opened[x][y] = true;
            if (hasMine[x][y]) {
                gameOver();
            } else if (board[x][y] == 0) {
                openNearbyCells(x, y);
            }
        }
    }

    public void toggleFlag(int x, int y) {
        if (isValidCell(x, y) && !opened[x][y]) {
            flagged[x][y] = !flagged[x][y];
            // TODO: ќбновить интерфейс после установки/сн€ти€ флажка
        }
    }


    private void gameOver() {
        // TODO: ƒобавить обработку поражени€ (например, показать все мины на игровом поле)
        // «десь можно реализовать логику, св€занную с окончанием игры при взрыве мины.
        // Ќапример, показать все мины на игровом поле и завершить игру.
    }

    private void openNearbyCells(int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (isValidCell(nx, ny) && !opened[nx][ny] && !flagged[nx][ny]) {
                    opened[nx][ny] = true;
                    if (board[nx][ny] == 0) {
                        openNearbyCells(nx, ny);
                    }
                }
            }
        }
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }


}
