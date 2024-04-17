package ru.kogtev.controller;

import ru.kogtev.models.*;
import ru.kogtev.view.*;

import java.io.IOException;
import java.util.Map;

public class MinesweeperController implements TimerListener {
    private final MainWindow view;
    private GameModel gameModel;
    private final HighScoresWindow highScoresWindow;


    public MinesweeperController(GameModel gameModel, MainWindow view, HighScoresWindow highScoresWindow) {
        this.gameModel = gameModel;
        this.view = view;
        this.highScoresWindow = highScoresWindow;

        updateHighScore();
    }

    private void updateHighScore() {
        Map<GameType, HighScore> highScoreMap = gameModel.getHighScore();
        highScoresWindow.setNoviceRecord(highScoreMap.get(GameType.NOVICE).getName(),
                highScoreMap.get(GameType.NOVICE).getScore());
        highScoresWindow.setMediumRecord(highScoreMap.get(GameType.MEDIUM).getName(),
                highScoreMap.get(GameType.MEDIUM).getScore());
        highScoresWindow.setExpertRecord(highScoreMap.get(GameType.EXPERT).getName(),
                highScoreMap.get(GameType.EXPERT).getScore());

        try {
            HighScoreManager.saveHighScore(gameModel.getHighScore());
        } catch (IOException e) {
            System.out.println("Невозможно записать файл " + e.getMessage());
        }

    }

    public void controllerStartNewGame() {
        gameModel.start();
        TimerManager.addTimerListener(this);
        updateView();
        view.setBombsCount(gameModel.getBoardModel().getTotalMines());
    }

    public void handleCellClick(int row, int col, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON:
                handleLeftButtonClick(row, col);
                break;
            case RIGHT_BUTTON:
                gameModel.toggleFlag(row, col);
                view.setBombsCount(gameModel.getRemainingMines());
                break;
            case MIDDLE_BUTTON:
                gameModel.openSurroundingCellsIfFlagged(row, col);
                break;
        }
        updateView();
    }

    private void handleLeftButtonClick(int row, int col) {
        gameModel.openCell(row, col);
        if (gameModel.isGameOver()) {
            handleGameOver();
        } else if (gameModel.isGameWon()) {
            handleGameWon();
        }
    }

    private void handleGameOver() {
        TimerManager.stopTimer();
        gameModel.openAllMines();
        updateView();
        showLoseWindow();
    }

    private void handleGameWon() {
        TimerManager.stopTimer();
        if (gameModel.checkRecord()) {
            showRecordsWindow();
            updateHighScore();
        }
        showWinWindow();
    }

    private void showLoseWindow() {
        LoseWindow loseWindow = new LoseWindow(view);
        loseWindow.setNewGameListener(e -> controllerStartNewGame());
        loseWindow.setExitListener(e -> view.dispose());
        loseWindow.setVisible(true);
    }

    private void showWinWindow() {
        WinWindow winWindow = new WinWindow(view);
        winWindow.setNewGameListener(e -> controllerStartNewGame());
        winWindow.setExitListener(e -> view.dispose());
        winWindow.setVisible(true);
    }

    private void showRecordsWindow() {
        RecordsWindow recordsWindow = new RecordsWindow(view);
        recordsWindow.setNameListener(this::updateName);
        recordsWindow.setVisible(true);
    }


    private void updateView() {
        int rows = gameModel.getBoardModel().getRows();
        int cols = gameModel.getBoardModel().getCols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                view.setCellImage(row, col, getCellImage(row, col));
            }
        }
    }


    public GameImage getCellImage(int row, int col) {

        if (!gameModel.getBoardModel().getOpenedCellValue(row, col)) {
            if (gameModel.getBoardModel().getFlaggedCellValue(row, col)) {
                return GameImage.MARKED;
            } else {
                return GameImage.CLOSED;
            }
        } else {
            switch (gameModel.getBoardModel().getBoardCellValue(row, col)) {
                case -1:
                    return GameImage.BOMB;
                case 0:
                    return GameImage.EMPTY;
                case 1:
                    return GameImage.NUM_1;
                case 2:
                    return GameImage.NUM_2;
                case 3:
                    return GameImage.NUM_3;
                case 4:
                    return GameImage.NUM_4;
                case 5:
                    return GameImage.NUM_5;
                case 6:
                    return GameImage.NUM_6;
                case 7:
                    return GameImage.NUM_7;
                case 8:
                    return GameImage.NUM_8;
                default:
                    throw new IllegalArgumentException("Invalid cell value: " +
                            gameModel.getBoardModel().getBoardCellValue(row, col));
            }
        }
    }

    public void updateModelForGameType(GameType gameType) {
        gameModel = new GameModel(gameType);
        view.createGameField(gameModel.getBoardModel().getRows(), gameModel.getBoardModel().getCols());
        controllerStartNewGame();
    }

    @Override
    public void onTimerTick(int elapsedTime) {
        view.setTimerValue(elapsedTime);
    }

    public void updateName(String name) {
        gameModel.getHighScore().get(gameModel.getGameType()).setName(name);
    }
}
