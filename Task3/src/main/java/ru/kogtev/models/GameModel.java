package ru.kogtev.models;

import ru.kogtev.view.GameType;

import java.util.*;

public class GameModel {
    private final BoardModel boardModel;
    private final GameType gameType;

    private boolean firstClick;
    private int remainingMines;

    private boolean gameLost;
    private boolean gameWon;

    private List<GameStateListener> gameStateListeners = new ArrayList<>();
    private final List<BombListener> bombListeners = new ArrayList<>();

    private final Map<GameType, HighScore> highScore;

    public GameModel(GameType gameType) {
        this.gameType = gameType;

        boardModel = GameDifficulty.gameDifficultyChoose(gameType);
        highScore = HighScoreManager.initializeHighScore();
        notifyBombCount();
    }

    public void start() {
        timerUpdater();
        boardModel.initCells();
        gameLost = false;
        gameWon = false;
        firstClick = true;
        remainingMines = boardModel.getTotalMines();
        notifyBombCount();
    }

    private static void timerUpdater() {
        TimerManager.stopTimer();
        TimerManager.elapsedTimer = 0;
        TimerManager.notifyListeners();
    }

    public void openCell(int row, int col) {
        notifyBombCount();
        if (firstClick) {
            boardModel.generateCellValueOnBoard(row, col, true);
            TimerManager.startTimer();
            firstClick = false;
        }

        if (getBoardModel().isCellOpened(row, col) || getBoardModel().getFlaggedCellValue(row, col)) {
            return;
        }

        boardModel.setOpenedCellValue(row, col, true);

        if (boardModel.getBoardCellValue(row, col) == -1) {
            gameLost = true;
            openAllMines();
            TimerManager.stopTimer();
            notifyGameLost();
            return;
        }

        if (boardModel.getBoardCellValue(row, col) == 0) {
            openAdjacentCells(row, col);
        }

        checkGameWon();

    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < boardModel.getRows() && col >= 0 && col < boardModel.getCols();
    }

    private boolean isValidCellNotOpenedAndFlag(int nRow, int nCol) {
        return isValidCell(nRow, nCol) && !boardModel.isCellOpened(nRow, nCol) &&
                !boardModel.getFlaggedCellValue(nRow, nCol);
    }

    private void openAdjacentCells(int row, int col) {
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {

                int nRow = row + dRow;
                int nCol = col + dCol;

                if (isValidCellNotOpenedAndFlag(nRow, nCol)) {

                    boardModel.setOpenedCellValue(nRow, nCol, true);

                    if (boardModel.getBoardCellValue(nRow, nCol) == 0) {
                        openAdjacentCells(nRow, nCol);
                    }
                }
            }
        }
    }

    public void toggleCellFlag(int row, int col) {
        if (remainingMines == 0 && !boardModel.getFlaggedCellValue(row, col)
                || boardModel.isCellOpened(row, col)) {
            return;
        }

        boardModel.setFlaggedCellValue(row, col, !boardModel.getFlaggedCellValue(row, col));

        if (boardModel.getFlaggedCellValue(row, col)) {
            remainingMines--;
            notifyBombCount();
        } else {
            remainingMines++;
            notifyBombCount();
        }
    }

    public void openSurroundingCellsIfFlagged(int row, int col) {
        if (!isValidCell(row, col) || !boardModel.isCellOpened(row, col)
                || boardModel.getBoardCellValue(row, col) == 0) {
            return;
        }

        int flagsAround = countFlagsAround(row, col);

        if (flagsAround == boardModel.getBoardCellValue(row, col)) {
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

                if (boardModel.getBoardCellValue(row, col) != -1 && !boardModel.isCellOpened(row, col)) {
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

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public Map<GameType, HighScore> getHighScore() {
        return highScore;
    }

    public GameType getGameType() {
        return gameType;
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
