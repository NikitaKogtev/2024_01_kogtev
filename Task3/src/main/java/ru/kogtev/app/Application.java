package ru.kogtev.app;

import ru.kogtev.controller.MinesweeperController;
import ru.kogtev.models.GameModel;
import ru.kogtev.view.*;


public class Application {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);

        GameModel gameModel = new GameModel(GameType.NOVICE);

        MinesweeperController controller = new MinesweeperController(gameModel, mainWindow);

        mainWindow.createGameField(10, 10);

        controller.controllerStartNewGame();

        mainWindow.setCellListener((x, y, buttonType) -> controller.handleCellClick(x, y, buttonType));

        mainWindow.setVisible(true);

        mainWindow.setNewGameMenuAction(e -> controller.controllerStartNewGame());

        settingsWindow.setGameTypeListener((gameType) -> controller.updateModelForGameType(gameType));

        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
//
//      HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
//


//

//        settingsWindow.setGameTypeListener(gameType -> {
//                    controller.updateModelForGameType(gameType);
//                    controller.controllerStartNewGame();
//                }
//        );
//
//        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
//
//        mainWindow.setExitMenuAction(e -> mainWindow.dispose());
//
//        mainWindow.setCellListener(controller::handleCellClick);
////
////        mainWindow.createGameField(10, 10);
////        mainWindow.setVisible(true);
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
