package ru.kogtev.view;

import ru.kogtev.controller.GameController;
import ru.kogtev.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class View implements CellUpdateListener, GameStateListener, TimerListener, BombListener {
    private GameModel gameModel;
    private MainWindow mainWindow = new MainWindow();
    private HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
    private SettingsWindow settingsWindow = new SettingsWindow(mainWindow);

    private final List<CellEventListener> cellEventListeners = new ArrayList<>();
    private List<GameStartListener> gameStartListeners = new ArrayList<>();
    private final List<GameTypeListener> gameTypeListeners = new ArrayList<>();

    public View(GameModel gameModel) {
        this.gameModel = gameModel;

        mainWindow.setNewGameMenuAction(e -> startNewGame());
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());

        gameModel.getBoardModel().addCellUpdateListener(this);
        mainWindow.setCellListener(this::notifyCellEventListener);
        settingsWindow.setGameTypeListener(this::notifyGameTypeListener);

        updateHighScore();
        startNewGame();
    }

    private void startNewGame() {
        TimerManager.addTimerListener(this);

        gameModel.getBoardModel().addCellUpdateListener(this);
        mainWindow.setCellListener(this::notifyCellEventListener);
        gameModel.addGameStateListener(this);

        settingsWindow.setGameTypeListener(this::notifyGameTypeListener);

        gameModel.addBombTickListener(this::onBombTick);

        mainWindow.createGameField(gameModel.getBoardModel().getRows(), gameModel.getBoardModel().getCols());

        mainWindow.setBombsCount(gameModel.getRemainingMines());

        System.out.println(this.gameModel.getBoardModel().toString());
        mainWindow.setVisible(true);
        notifyGameStartListener();
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

    public SettingsWindow getSettingsWindow() {
        return settingsWindow;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
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
        gameModel.setGameStateListeners(new ArrayList<>());
        WinWindow winWindow = new WinWindow(mainWindow);
        winWindow.setNewGameListener(e -> startNewGame());
        winWindow.setExitListener(e -> mainWindow.dispose());
        winWindow.setVisible(true);
    }

    @Override
    public void onGameLoss() {
        gameModel.setGameStateListeners(new ArrayList<>());
        LoseWindow loseWindow = new LoseWindow(mainWindow);
        loseWindow.setNewGameListener(e -> startNewGame());
        loseWindow.setExitListener(e -> mainWindow.dispose());
        loseWindow.setVisible(true);
    }

    public void addCellEventListener(CellEventListener cellEventListener) {
        cellEventListeners.add(cellEventListener);
    }

    public void removeCellEventListener(CellEventListener cellEventListener) {
        cellEventListeners.remove(cellEventListener);
    }

    private void notifyCellEventListener(int x, int y, ButtonType buttonType) {
        for (CellEventListener cellEventListener : cellEventListeners) {
            cellEventListener.onMouseClick(x, y, buttonType);
        }
    }

    public void addGameStartListener(GameStartListener gameStartListener) {
        gameStartListeners.add(gameStartListener);
    }

    public void removeGameStartListener(GameStartListener gameStartListener) {
        gameStartListeners.remove(gameStartListener);
    }

    private void notifyGameStartListener() {
        for (GameStartListener gameStartListener : gameStartListeners) {
            gameStartListener.onStartGame();
        }
    }

    public void addGameTypeListener(GameTypeListener gameTypeListener) {
        gameTypeListeners.add(gameTypeListener);
    }

    public void removeGameTypeListener(GameTypeListener gameTypeListener) {
        gameTypeListeners.add(gameTypeListener);
    }


    private void notifyGameTypeListener(GameType gameType) {
        for (GameTypeListener gameTypeListener : gameTypeListeners) {
            gameTypeListener.onGameTypeChanged(gameType);
        }
        startNewGame();
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
}