package ru.kogtev.controller;

import ru.kogtev.models.GameDifficulty;
import ru.kogtev.models.GameModel;
import ru.kogtev.models.HighScore;
import ru.kogtev.models.TimerManager;
import ru.kogtev.view.*;

import java.util.ArrayList;
import java.util.Map;

public class GameController implements GameStartListener, CellEventListener, GameTypeListener {
    private View view;
    private GameModel gameModel;

    public GameController(View view, GameModel gameModel) {
        this.view = view;
        this.gameModel = gameModel;

        view.addCellEventListener(this);
        view.addGameStartListener(this::onStartGame);
        view.addGameTypeListener(this);
    }

    @Override
    public void onStartGame() {
        TimerManager.timerListeners = new ArrayList<>();
        gameModel.start();
    }

    @Override
    public void onMouseClick(int x, int y, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON:
                gameModel.openCell(x, y);
                break;
            case RIGHT_BUTTON:
                gameModel.toggleFlag(x, y);
                break;
            case MIDDLE_BUTTON:
                gameModel.openSurroundingCellsIfFlagged(x, y);
                break;
        }
    }

    @Override
    public void onGameTypeChanged(GameType gameType) {
        gameModel = new GameModel(gameType);
        view.setGameModel(gameModel);
        view.getMainWindow().createGameField(gameModel.getBoardModel().getRows(), gameModel.getBoardModel().getCols());
        onStartGame();
        System.out.println(gameModel.getBoardModel().toString());

    }

    public GameModel getGameModel() {
        return gameModel;
    }




}
