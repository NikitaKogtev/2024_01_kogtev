package ru.kogtev.view;

import ru.kogtev.models.Cell;
import ru.kogtev.models.CellUpdateListener;
import ru.kogtev.models.GameModel;
import ru.kogtev.models.GameStateListener;

public class View implements CellUpdateListener, GameStateListener {
    private final GameModel gameModel;
    private MainWindow mainWindow = new MainWindow();

    private WinWindow winWindow = new WinWindow(mainWindow);

    public View(GameModel gameModel) {
        this.gameModel = gameModel;
        gameModel.getBoardModel().addCellUpdateListener(this);
        gameModel.addGameStateListener(this);
    }

    @Override
    public void onCellUpdate(int row, int col, Cell cell) {
        if (!cell.isOpened()) {
            if (cell.isFlagged()) {
                mainWindow.setCellImage(row, col, GameImage.MARKED);
            } else {
                mainWindow.setCellImage(row, col, GameImage.CLOSED);
            }
        } else {
            GameImage cellImage = switch (cell.getBoardValue()) {
                case -1 -> GameImage.BOMB;
                case 0 -> GameImage.EMPTY;
                case 1 -> GameImage.NUM_1;
                case 2 -> GameImage.NUM_2;
                case 3 -> GameImage.NUM_3;
                case 4 -> GameImage.NUM_4;
                case 5 -> GameImage.NUM_5;
                case 6 -> GameImage.NUM_6;
                case 7 -> GameImage.NUM_7;
                case 8 -> GameImage.NUM_8;
                default -> throw new IllegalArgumentException("Invalid cell value: " +
                        cell.getBoardValue());
            };
            mainWindow.setCellImage(row, col, cellImage);
        }
    }


    @Override
    public void onGameWon() {

        WinWindow winWindow = new WinWindow(mainWindow);
        winWindow.setNewGameListener(e -> gameModel.start());
        winWindow.setExitListener(e -> mainWindow.dispose());
        winWindow.setVisible(true);
    }

    @Override
    public void onGameLost() {
        LoseWindow loseWindow = new LoseWindow(mainWindow);
        loseWindow.setNewGameListener(e -> gameModel.start());
        loseWindow.setExitListener(e -> mainWindow.dispose());
        loseWindow.setVisible(true);
    }
}