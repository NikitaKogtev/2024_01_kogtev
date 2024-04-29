package ru.kogtev.controller;

import ru.kogtev.models.*;
import ru.kogtev.view.*;

public class MinesweeperController{
    private final View view;
    private GameModel gameModel;

    public MinesweeperController(GameModel gameModel, View view) {
        this.gameModel = gameModel;
        this.view = view;
//        this.highScoresWindow = highScoresWindow;
//
//        updateHighScore();
    }

//    public void startNewGame() {
//        gameModel.start();
//    }

    public void handleCellClick(int row, int col, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON:
                gameModel.openCell(row,col);
                break;
            case RIGHT_BUTTON:
                gameModel.toggleFlag(row, col);
                break;
            case MIDDLE_BUTTON:
                gameModel.openSurroundingCellsIfFlagged(row, col);
                break;
        }
    }


//
//    private void updateHighScore() {
//        Map<GameType, HighScore> highScoreMap = gameModel.getHighScore();
//        highScoresWindow.setNoviceRecord(highScoreMap.get(GameType.NOVICE).getName(),
//                highScoreMap.get(GameType.NOVICE).getScore());
//        highScoresWindow.setMediumRecord(highScoreMap.get(GameType.MEDIUM).getName(),
//                highScoreMap.get(GameType.MEDIUM).getScore());
//        highScoresWindow.setExpertRecord(highScoreMap.get(GameType.EXPERT).getName(),
//                highScoreMap.get(GameType.EXPERT).getScore());
//
//        try {
//            HighScoreManager.saveHighScore(gameModel.getHighScore());
//        } catch (IOException e) {
//            System.out.println("Невозможно записать файл " + e.getMessage());
//        }
//
//    }
//    //TODO: Настроить листенер во вью на таймер и на счетчик бомб
//    public void controllerStartNewGame() {
//        gameModel.start();
//        TimerManager.addTimerListener(this);
//        view.setBombsCount(gameModel.getBoardModel().getTotalMines());
//    }
//
//    public void handleCellClick(int row, int col, ButtonType buttonType) {
//        switch (buttonType) {
//            case LEFT_BUTTON:
//                handleLeftButtonClick(row, col);
//                break;
//            case RIGHT_BUTTON:
//                gameModel.toggleFlag(row, col);
//                view.setBombsCount(gameModel.getRemainingMines());
//                break;
//            case MIDDLE_BUTTON:
//                gameModel.openSurroundingCellsIfFlagged(row, col);
//                break;
//        }
//    }
//
//    private void handleLeftButtonClick(int row, int col) {
//        gameModel.openCell(row, col);
//        if (gameModel.isGameOver()) {
//            handleGameOver();
//        } else if (gameModel.isGameWon()) {
//            handleGameWon();
//        }
//    }
//
//    private void handleGameOver() {
//        TimerManager.stopTimer();
//        gameModel.openAllMines();
//        showLoseWindow();
//    }
//
//    private void handleGameWon() {
//        TimerManager.stopTimer();
//        if (gameModel.checkRecord()) {
//            showRecordsWindow();
//            updateHighScore();
//        }
//        showWinWindow();
//    }
//
//    private void showRecordsWindow() {
//        RecordsWindow recordsWindow = new RecordsWindow(view);
//        recordsWindow.setNameListener(this::updateName);
//        recordsWindow.setVisible(true);
//    }
//
//    public void updateModelForGameType(GameType gameType) {
//        gameModel = new GameModel(gameType);
//        view.createGameField(gameModel.getBoardModel().getRows(), gameModel.getBoardModel().getCols());
//        controllerStartNewGame();
//    }
//

//
//    public void updateName(String name) {
//        gameModel.getHighScore().get(gameModel.getGameType()).setName(name);
//    }
}
