package ru.kogtev.models;

import ru.kogtev.view.GameType;

import java.io.*;
import java.util.*;

public class GameModel {
    private BoardModel boardModel;
    private final GameType gameType;
    private int remainingMines;
    private boolean firstClick;
    private boolean gameOver;
    private boolean gameWon;

    private Map<GameType, HighScore> highScore;

    public GameModel(GameType gameType) {
        this.gameType = gameType;
        boardModel = GameDifficulty.gameDifficultyChoose(gameType);
        highScore = HighScoreManager.initializeHighScore();
    }

    public void start() {
        TimerManager.stopTimer();
        TimerManager.elapsedTimer = 0;
        TimerManager.notifyListeners();
        TimerManager.listeners = new ArrayList<>();

        boardModel = GameDifficulty.gameDifficultyChoose(gameType);
        gameWon = false;
        gameOver = false;
        firstClick = true;
        remainingMines = boardModel.getTotalMines();
    }

    public void openCell(int row, int col) {
        if (firstClick) {
            boardModel.generateCellValueOnBoard(row, col);
            TimerManager.startTimer();
            firstClick = false;
        }

        // Если ячейка уже открыта или поставлен флаг то return
        if (getBoardModel().getOpenedCellValue(row, col) || getBoardModel().getFlaggedCellValue(row, col)) {
            return;
        }

        // Меняем ячейку с закрытой на открытую
        boardModel.setOpenedCellValue(row, col, true);

        if (boardModel.getBoardCellValue(row, col) == -1) {
            gameOver = true;
        }

        if (boardModel.getBoardCellValue(row, col) == 0) {
            openAdjacentCells(row, col);
        }

        checkGameWon();

    }

    private void openAdjacentCells(int row, int col) {
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {

                int nRow = row + dRow;
                int nCol = col + dCol;

                if (isValidCell(nRow, nCol) && !boardModel.getOpenedCellValue(nRow, nCol) &&
                        !boardModel.getFlaggedCellValue(nRow, nCol)) {

                    boardModel.setOpenedCellValue(nRow, nCol, true);

                    if (boardModel.getBoardCellValue(nRow, nCol) == 0) {
                        openAdjacentCells(nRow, nCol);
                    }
                }
            }
        }
    }


    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < boardModel.getRows() && col >= 0 && col < boardModel.getCols();
    }

    public void toggleFlag(int row, int col) {

        if (remainingMines == 0 && !boardModel.getFlaggedCellValue(row, col)
                || boardModel.getOpenedCellValue(row, col)) {
            return;
        }

        boardModel.setFlaggedCellValue(row, col, !boardModel.getFlaggedCellValue(row, col));

        if (boardModel.getFlaggedCellValue(row, col)) {
            remainingMines--;
        } else {
            remainingMines++;
        }
    }

    public void openSurroundingCellsIfFlagged(int row, int col) {
        // Проверяем, что координаты клетки валидны и клетка открыта
        if (!isValidCell(row, col) || !boardModel.getOpenedCellValue(row, col)
                || boardModel.getBoardCellValue(row, col) == 0) {
            return;
        }

        int flagsAround = countFlagsAround(row, col);

        // Проверяем, что количество флажков вокруг равно значению в текущей клетке
        if (flagsAround == boardModel.getBoardCellValue(row, col)) {
            // Открываем все закрытые клетки вокруг текущей
            for (int dRow = -1; dRow <= 1; dRow++) {
                for (int dCol = -1; dCol <= 1; dCol++) {
                    int nRow = row + dRow;
                    int nCol = col + dCol;
                    // Проверяем, что новые координаты являются валидными ячейками и клетка закрыта
                    if (isValidCell(nRow, nCol) && !boardModel.getOpenedCellValue(nRow, nCol) && //duplicate
                            !boardModel.getFlaggedCellValue(nRow, nCol)) {
                        openCell(nRow, nCol);
                    }
                }
            }
        }
    }

    private int countFlagsAround(int row, int col) {
        int count = 0;
        // Проверяем количество флажков вокруг клетки
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                int nRow = row + dRow;
                int nCol = col + dCol;
                // Проверяем, что новые координаты являются валидными ячейками и там установлен флажок
                if (isValidCell(nRow, nCol) && boardModel.getFlaggedCellValue(nRow, nCol)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void checkGameWon() {
        for (int row = 0; row < boardModel.getRows(); row++) {
            for (int col = 0; col < boardModel.getCols(); col++) {
                if (boardModel.getBoardCellValue(row, col) != -1 && !boardModel.getOpenedCellValue(row, col)) {
                    return; // Если есть не пройденная ячейка без мин, игра не выиграна
                }
            }
        }
        gameWon = true;
    }

    public void openAllMines() {
        for (int row = 0; row < boardModel.getRows(); row++) {
            for (int col = 0; col < boardModel.getCols(); col++) {
                if (boardModel.getBoardCellValue(row, col) == -1) {
                    boardModel.setOpenedCellValue(row, col, true);
                }
            }
        }
    }


    public boolean checkRecord() {
        if (TimerManager.score < highScore.get(gameType).getScore()) {
            highScore.get(gameType).setScore(TimerManager.score);
            return true;
        }
        return false;
    }

    public int getRemainingMines() {
        return remainingMines;
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public Map<GameType, HighScore> getHighScore() {
        return highScore;
    }

    public GameType getGameType() {
        return gameType;
    }


}
