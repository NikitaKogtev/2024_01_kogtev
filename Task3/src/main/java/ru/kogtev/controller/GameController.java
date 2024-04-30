package ru.kogtev.controller;

import ru.kogtev.models.GameModel;

import ru.kogtev.view.*;

public class GameController implements GameStartListener, CellEventListener, GameTypeListener {
    private final View view;
    private GameModel gameModel;

    public GameController(View view, GameModel gameModel) {
        this.view = view;
        this.gameModel = gameModel;

        view.addCellEventListener(this);
        view.addGameStartListener(this);
        view.addGameTypeListener(this);
    }

    @Override
    public void onStartGame() {
        gameModel.start();
    }

    @Override
    public void onMouseClick(int x, int y, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON:
                gameModel.openCell(x, y);
                break;
            case RIGHT_BUTTON:
                gameModel.toggleCellFlag(x, y);
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
    }
}
