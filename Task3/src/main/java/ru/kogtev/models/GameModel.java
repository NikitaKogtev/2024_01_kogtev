package ru.kogtev.models;

import ru.kogtev.view.GameType;

import java.util.*;

public class GameModel {
    private GameBoard gameBoard;
    private GameType gameType;

    private boolean firstClick;
    private int remainingMines;

    private boolean gameLost;
    private boolean gameWon;

    private List<GameStateListener> gameStateListeners = new ArrayList<>();
    private final List<BombListener> bombListeners = new ArrayList<>();
    private final List<GameFieldListener> gameFieldListeners = new ArrayList<>();

    private final Map<GameType, HighScore> highScore;

    public GameModel(GameType gameType) {
        this.gameType = gameType;

        gameBoard = GameDifficulty.gameDifficultyChoose(gameType);
        highScore = HighScoreManager.initializeHighScore();

        notifyBombCount();
    }

    public void start() {
        timerUpdater();

        gameBoard.initCells();

        gameLost = false;
        gameWon = false;
        firstClick = true;
        remainingMines = gameBoard.getTotalMines();

        notifyBombCount();
    }

    public void changeGameType(GameType newGameType) {
        gameType = newGameType;

        gameBoard = GameDifficulty.gameDifficultyChoose(newGameType);
        gameBoard.initCells();

        gameLost = false;
        gameWon = false;
        firstClick = true;
        remainingMines = gameBoard.getTotalMines();

        notifyGameFieldChanged(gameBoard.getRows(), gameBoard.getCols());

        notifyBombCount();
    }

    private static void timerUpdater() {
        TimerManager.stopTimer();
        TimerManager.elapsedTimer = 0;
        TimerManager.notifyListeners();
    }

    public void openCell(int row, int col) {
        if (firstClick) {
            gameBoard.generateCellValueOnBoard(row, col, true);
            TimerManager.startTimer();
            firstClick = false;
        }

        if (getGameBoard().isCellOpened(row, col) || getGameBoard().getFlaggedCellValue(row, col)) {
            return;
        }

        gameBoard.setOpenedCellValue(row, col, true);

        if (gameBoard.getBoardCellValue(row, col) == -1) {
            gameLost = true;
            openAllMines();
            TimerManager.stopTimer();
            notifyGameLost();
            return;
        }

        if (gameBoard.getBoardCellValue(row, col) == 0) {
            openAdjacentCells(row, col);
        }

        checkGameWon();

    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < gameBoard.getRows() && col >= 0 && col < gameBoard.getCols();
    }

    private boolean isValidCellNotOpenedAndFlag(int nRow, int nCol) {
        return isValidCell(nRow, nCol) && !gameBoard.isCellOpened(nRow, nCol) &&
                !gameBoard.getFlaggedCellValue(nRow, nCol);
    }

    private void openAdjacentCells(int row, int col) {
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {

                int nRow = row + dRow;
                int nCol = col + dCol;

                if (isValidCellNotOpenedAndFlag(nRow, nCol)) {

                    gameBoard.setOpenedCellValue(nRow, nCol, true);

                    if (gameBoard.getBoardCellValue(nRow, nCol) == 0) {
                        openAdjacentCells(nRow, nCol);
                    }
                }
            }
        }
    }

    public void toggleCellFlag(int row, int col) {
        if (remainingMines == 0 && !gameBoard.getFlaggedCellValue(row, col)
                || gameBoard.isCellOpened(row, col)) {
            return;
        }

        gameBoard.setFlaggedCellValue(row, col, !gameBoard.getFlaggedCellValue(row, col));

        if (gameBoard.getFlaggedCellValue(row, col)) {
            remainingMines--;
            notifyBombCount();
        } else {
            remainingMines++;
            notifyBombCount();
        }
    }

    public void openSurroundingCellsIfFlagged(int row, int col) {
        if (!isValidCell(row, col) || !gameBoard.isCellOpened(row, col)
                || gameBoard.getBoardCellValue(row, col) == 0) {
            return;
        }

        int flagsAround = countFlagsAround(row, col);

        if (flagsAround == gameBoard.getBoardCellValue(row, col)) {
            for (int dRow = -1; dRow <= 1; dRow++) {
                for (int dCol = -1; dCol <= 1; dCol++) {
                    int nRow = row + dRow;
                    int nCol = col + dCol;

                    if (isValidCellNotOpenedAndFlag(nRow, nCol)) {
                        openCell(nRow, nCol);
                    }

                }
            }
        }
    }

    private int countFlagsAround(int row, int col) {
        int count = 0;

        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                int nRow = row + dRow;
                int nCol = col + dCol;

                if (isValidCell(nRow, nCol) && gameBoard.getFlaggedCellValue(nRow, nCol)) {
                    count++;
                }

            }
        }
        return count;
    }

    private void checkGameWon() {
        for (int row = 0; row < gameBoard.getRows(); row++) {
            for (int col = 0; col < gameBoard.getCols(); col++) {

                if (gameBoard.getBoardCellValue(row, col) != -1 && !gameBoard.isCellOpened(row, col)) {
                    return;
                }

            }
        }
        gameWon = true;
        openAllMines();
        TimerManager.stopTimer();
        notifyGameWon();
    }

    public void openAllMines() {
        for (int row = 0; row < gameBoard.getRows(); row++) {
            for (int col = 0; col < gameBoard.getCols(); col++) {

                if (gameBoard.getBoardCellValue(row, col) == -1) {
                    gameBoard.setOpenedCellValue(row, col, true);
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

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Map<GameType, HighScore> getHighScore() {
        return highScore;
    }

    public GameType getGameType() {
        return gameType;
    }


    public void addGameFieldListener(GameFieldListener gameFieldListener) {
        gameFieldListeners.add(gameFieldListener);
    }

    private void notifyGameFieldChanged(int rows, int cols) {
        for (GameFieldListener gameFieldListener : gameFieldListeners) {
            gameFieldListener.onGameFieldChanged(rows, cols);
        }
    }

    public void setGameStateListeners(List<GameStateListener> gameStateListeners) {
        this.gameStateListeners = gameStateListeners;
    }

    public void addGameStateListener(GameStateListener gameStateListener) {
        gameStateListeners.add(gameStateListener);
    }

    private void notifyGameWon() {
        for (GameStateListener gameStateListener : gameStateListeners) {
            gameStateListener.onGameWin();
        }
    }

    private void notifyGameLost() {
        for (GameStateListener gameStateListener : gameStateListeners) {
            gameStateListener.onGameLoss();
        }
    }

    public void addBombCountListener(BombListener bombListener) {
        bombListeners.add(bombListener);
    }

    private void notifyBombCount() {
        for (BombListener bombListener : bombListeners) {
            bombListener.onBombCount(remainingMines);
        }
    }
}
