package ru.kogtev.app;

import ru.kogtev.controller.GameController;
import ru.kogtev.models.GameModel;
import ru.kogtev.view.*;

public class Application {
    public static void main(String[] args) {
        GameModel gameModel = new GameModel(GameType.NOVICE);
        View view = new View(gameModel);
        GameController gameController = new GameController(view, gameModel);

        gameController.onStartGame();
    }
}
