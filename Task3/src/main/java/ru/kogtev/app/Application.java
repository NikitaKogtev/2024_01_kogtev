package ru.kogtev.app;

import ru.kogtev.controller.MinesweeperController;
import ru.kogtev.models.GameModel;
import ru.kogtev.models.HighScore;
import ru.kogtev.view.*;

import java.io.IOException;
import java.util.List;


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

        mainWindow.setCellListener((x, y, buttonType) -> {
            try {
                controller.handleCellClick(x, y, buttonType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mainWindow.setVisible(true);

        recordsWindow.setNameListener(name -> controller.updateName(name));

        mainWindow.setNewGameMenuAction(e -> controller.controllerStartNewGame());

        settingsWindow.setGameTypeListener((gameType) -> controller.updateModelForGameType(gameType));

        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));

        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));

        recordsWindow.setNameListener(name -> controller.updateName(name));

        mainWindow.setExitMenuAction(e -> mainWindow.dispose());


////
////        // TODO: There is a sample code below, remove it after try
////
////        // mainWindow.setTimerValue(145);
////
////        mainWindow.setCellImage(0, 0, GameImage.EMPTY);
////        mainWindow.setCellImage(0, 1, GameImage.CLOSED);
////        mainWindow.setCellImage(0, 2, GameImage.MARKED);
////        mainWindow.setCellImage(0, 3, GameImage.BOMB);
////        mainWindow.setCellImage(1, 0, GameImage.NUM_1);
////        mainWindow.setCellImage(1, 1, GameImage.NUM_2);
////        mainWindow.setCellImage(1, 2, GameImage.NUM_3);
////        mainWindow.setCellImage(1, 3, GameImage.NUM_4);
////        mainWindow.setCellImage(1, 4, GameImage.NUM_5);
////        mainWindow.setCellImage(1, 5, GameImage.NUM_6);
////        mainWindow.setCellImage(1, 6, GameImage.NUM_7);
////        mainWindow.setCellImage(1, 7, GameImage.NUM_8);
////    }
    }
}
