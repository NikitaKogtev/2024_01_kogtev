package ru.kogtev.app;

import ru.kogtev.controller.MinesweeperController;
import ru.kogtev.models.GameModel;
import ru.kogtev.view.*;

public class Application {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
        HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
        RecordsWindow recordsWindow = new RecordsWindow(mainWindow);

        GameModel gameModel = new GameModel(GameType.NOVICE);

        MinesweeperController controller = new MinesweeperController(gameModel, mainWindow, highScoresWindow);


        mainWindow.createGameField(10, 10);
        controller.controllerStartNewGame();

        mainWindow.setNewGameMenuAction(e -> controller.controllerStartNewGame());
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());

        mainWindow.setCellListener(controller::handleCellClick);
        settingsWindow.setGameTypeListener(controller::updateModelForGameType);
        recordsWindow.setNameListener(controller::updateName);


        mainWindow.setVisible(true);

    }
}
