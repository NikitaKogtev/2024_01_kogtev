package ru.kogtev.controller;

import ru.kogtev.models.MinesweeperModel;
import ru.kogtev.view.ButtonType;
import ru.kogtev.view.LoseWindow;
import ru.kogtev.view.MainWindow;
import ru.kogtev.view.WinWindow;

public class MinesweeperController {
    private MinesweeperModel model;
    private MainWindow view;


    public MinesweeperController(MinesweeperModel model, MainWindow view) {
        this.model = model;
        this.view = view;
    }

    public void controllerStartNewGame() {

        model.modelsStartNewGame();
        view.setBombsCount(model.getTotalMines());
        updateView();
    }


    public void handleCellClick(int x, int y, ButtonType buttonType) {

        switch (buttonType) {
            case LEFT_BUTTON:
                model.openCell(x, y);
                updateView();
                if (model.isGameOver()) {
                    LoseWindow loseWindow = new LoseWindow(view);
                    loseWindow.setNewGameListener(e -> controllerStartNewGame());
                    loseWindow.setExitListener(e -> view.dispose());
                    loseWindow.setVisible(true);
                } else if (model.isGameWon()) {
                    WinWindow winWindow = new WinWindow(view);
                    winWindow.setNewGameListener(e -> controllerStartNewGame());
                    winWindow.setExitListener(e -> view.dispose());
                    winWindow.setVisible(true);
                }
                break;
            case RIGHT_BUTTON:
                model.toggleFlag(x, y);
                view.setBombsCount(model.getRemainingMines());
                break;
            case MIDDLE_BUTTON:
                model.openSurroundingCellsIfFlagged(x, y);
                break;
        }
        updateView();
    }

    private void updateView() {
        int rows = model.getRows();
        int cols = model.getCols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                view.setCellImage(col, row, model.getCellImage(col, row));
            }
        }
    }

}
