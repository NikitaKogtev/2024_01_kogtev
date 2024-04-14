package ru.kogtev.models;

import ru.kogtev.view.GameType;

import java.io.Serial;
import java.io.Serializable;

public class HighScore implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private GameType gameType;
    private String name;
    private int score;

    public HighScore(GameType gameType, String name, int score) {
        this.gameType = gameType;
        this.name = name;
        this.score = score;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
