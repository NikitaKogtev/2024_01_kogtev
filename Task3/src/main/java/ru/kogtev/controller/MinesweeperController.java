package ru.kogtev.controller;

import ru.kogtev.models.GameModel;
import ru.kogtev.view.*;

public class MinesweeperController {
    private MainWindow view;
    private GameModel gameModel;


    public MinesweeperController(GameModel gameModel, MainWindow view) {
        this.gameModel = gameModel;
        this.view = view;
    }

    public void updateModelForGameType(GameType gameType) {
        switch (gameType) {
            case NOVICE:
                gameModel = new GameModel(GameType.NOVICE);
                break;
            case MEDIUM:
                gameModel = new GameModel(GameType.MEDIUM);
                break;
            case EXPERT:
                gameModel = new GameModel(GameType.EXPERT);
                break;
        }
        view.createGameField(gameModel.getBoardModel().getRows(), gameModel.getBoardModel().getCols());
        controllerStartNewGame();
    }

    public void controllerStartNewGame() {
        gameModel.start();
        updateView();
        view.setBombsCount(gameModel.getBoardModel().getTotalMines());
    }

    public void handleCellClick(int row, int col, ButtonType buttonType) {

        switch (buttonType) {
            case LEFT_BUTTON:
                gameModel.openCell(row, col);
                updateView();
                if (gameModel.isGameOver()) {
                    gameModel.openAllMines();
                    updateView();

                    LoseWindow loseWindow = new LoseWindow(view);
                    loseWindow.setNewGameListener(e -> controllerStartNewGame());
                    loseWindow.setExitListener(e -> view.dispose());
                    loseWindow.setVisible(true);
                } else if (gameModel.isGameWon()) {
                    WinWindow winWindow = new WinWindow(view);
                    winWindow.setNewGameListener(e -> controllerStartNewGame());
                    winWindow.setExitListener(e -> view.dispose());
                    winWindow.setVisible(true);

                }
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
                return GameImage.MARKED; // флаг
            } else {
                return GameImage.CLOSED; // закрыта€ €чейка
            }
        } else {
            switch (gameModel.getBoardModel().getBoardCellValue(row, col)) {
                case -1:
                    return GameImage.BOMB; // минированна€ €чейка
                case 0:
                    return GameImage.EMPTY; // пуста€ €чейка
                case 1:
                    return GameImage.NUM_1; // €чейка с числом 1
                case 2:
                    return GameImage.NUM_2; // €чейка с числом 2
                case 3:
                    return GameImage.NUM_3; // €чейка с числом 3
                case 4:
                    return GameImage.NUM_4; // €чейка с числом 4
                case 5:
                    return GameImage.NUM_5; // €чейка с числом 5
                case 6:
                    return GameImage.NUM_6; // €чейка с числом 6
                case 7:
                    return GameImage.NUM_7; // €чейка с числом 7
                case 8:
                    return GameImage.NUM_8; // €чейка с числом 8
                default:
                    throw new IllegalArgumentException("Invalid cell value: " +
                            gameModel.getBoardModel().getBoardCellValue(row, col));
            }
        }
    }


}
