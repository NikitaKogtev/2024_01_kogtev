package ru.kogtev.view;

import ru.kogtev.controller.GameController;
import ru.kogtev.models.*;

import java.util.ArrayList;
import java.util.List;

public class View implements CellUpdateListener, GameStateListener, TimerListener, BombListener {
    private final GameModel gameModel;
    private MainWindow mainWindow = new MainWindow();
    private HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
    private SettingsWindow settingsWindow = new SettingsWindow(mainWindow);

    private final List<CellEventListener> cellEventListeners = new ArrayList<>();
    private final List<GameStartListener> gameStartListeners = new ArrayList<>();
    private final List<GameTypeListener> gameTypeListeners = new ArrayList<>();

    public View(GameModel gameModel) {
        this.gameModel = gameModel;

        gameModel.getBoardModel().addCellUpdateListener(this);
        gameModel.addBombTickListener(this::onBombTick);

        startNewGame();

    }

    private void startNewGame() {
        notifyGameStartListener();

        mainWindow.createGameField(10, 10);

        mainWindow.setCellListener(this::notifyCellEventListener);

        gameModel.addGameStateListener(this);

        gameModel.addBombTickListener(this::onBombTick);

        mainWindow.setNewGameMenuAction(e -> startNewGame());
        mainWindow.setBombsCount(gameModel.getRemainingMines());

        mainWindow.setVisible(true);
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        settingsWindow.setGameTypeListener(this::notifyGameTypeListener);

        TimerManager.addTimerListener(this);

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

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    @Override
    public void onTimerTick(int elapsedTime) {
        mainWindow.setTimerValue(elapsedTime);
    }

    @Override
    public void onBombTick(int remainingMines) {
        mainWindow.setBombsCount(remainingMines);

    }

    @Override
    public void onGameWin() {
        WinWindow winWindow = new WinWindow(mainWindow);
        winWindow.setNewGameListener(e -> startNewGame());
        winWindow.setExitListener(e -> mainWindow.dispose());
        winWindow.setVisible(true);
    }

    @Override
    public void onGameLoss() {
        LoseWindow loseWindow = new LoseWindow(mainWindow);
        loseWindow.setNewGameListener(e -> startNewGame());
        loseWindow.setExitListener(e -> mainWindow.dispose());
        loseWindow.setVisible(true);
    }

    public void addCellEventListener(CellEventListener cellEventListener) {
        cellEventListeners.add(cellEventListener);
    }

    private void notifyCellEventListener(int x, int y, ButtonType buttonType) {
        for (CellEventListener cellEventListener : cellEventListeners) {
            cellEventListener.onMouseClick(x, y, buttonType);
        }
    }

    public void addGameStartListener(GameStartListener gameStartListener) {
        gameStartListeners.add(gameStartListener);
    }

    private void notifyGameStartListener() {
        for (GameStartListener gameStartListener : gameStartListeners) {
            gameStartListener.onStartGame();
        }
    }

    public void addGameTypeListener(GameTypeListener gameTypeListener) {
        gameTypeListeners.add(gameTypeListener);
    }

    private void notifyGameTypeListener(GameType gameType) {
        for (GameTypeListener gameTypeListener : gameTypeListeners) {
            gameTypeListener.onGameTypeChanged(gameType);
        }
    }

}