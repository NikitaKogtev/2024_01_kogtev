package ru.kogtev.controller;

import ru.kogtev.models.GameModel;
import ru.kogtev.models.TimerManager;
import ru.kogtev.view.*;

import java.util.ArrayList;

public class GameController implements GameStartListener, CellEventListener, GameTypeListener {
    private final View view;
    private GameModel gameModel;

    public GameController(View view, GameModel gameModel) {
        this.view = view;
        this.gameModel = gameModel;

        this.view.addGameStartListener(this);
        this.view.addCellEventListener(this);
        this.view.addGameTypeListener(this);

        gameModel.start();
    }

    @Override
    public void onStartGame() {
        gameModel.setGameStateListeners(new ArrayList<>());
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
        view.getMainWindow().createGameField(gameModel.getBoardModel().getRows(), gameModel.getBoardModel().getCols());
        gameModel.start(gameType);
    }
}
