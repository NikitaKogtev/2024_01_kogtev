package ru.kogtev.models;

import ru.kogtev.view.GameType;

public class GameDifficulty {
    private static final int NOVICE_ROWS_AMOUNT = 10;
    private static final int NOVICE_COLS_AMOUNT = 10;
    private static final int NOVICE_MINES_AMOUNT = 10;
    private static final int MEDIUM_ROWS_AMOUNT = 16;
    private static final int MEDIUM_COLS_AMOUNT = 16;
    private static final int MEDIUM_MINES_AMOUNT = 40;
    private static final int EXPERT_ROWS_AMOUNT = 16;
    private static final int EXPERT_COLS_AMOUNT = 30;
    private static final int EXPERT_MINES_AMOUNT = 99;

    private GameDifficulty() {

    }

    public static BoardModel gameDifficultyChoose(GameType gameType) {
        switch (gameType) {
            case NOVICE:
                return new BoardModel(NOVICE_ROWS_AMOUNT, NOVICE_COLS_AMOUNT, NOVICE_MINES_AMOUNT);
            case MEDIUM:
                return new BoardModel(MEDIUM_ROWS_AMOUNT, MEDIUM_COLS_AMOUNT, MEDIUM_MINES_AMOUNT);
            case EXPERT:
                return new BoardModel(EXPERT_ROWS_AMOUNT, EXPERT_COLS_AMOUNT, EXPERT_MINES_AMOUNT);
        }
        return new BoardModel(NOVICE_ROWS_AMOUNT, NOVICE_COLS_AMOUNT, NOVICE_MINES_AMOUNT);
    }

}
