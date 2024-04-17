package ru.kogtev.models;

import ru.kogtev.view.GameType;

import java.io.*;
import java.util.EnumMap;
import java.util.Map;

public class HighScoreManager {
    public static final String FILENAME = "highscore.txt";
    private static final String DEFAULT_PLAYER_NAME = "Unknown";
    private static final int SCORE = 9999;

    private HighScoreManager() {

    }

    public static Map<GameType, HighScore> initializeHighScore() {
        try {
            return readHighScore();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            Map<GameType, HighScore> highScore = new EnumMap<>(GameType.class);
            highScore.put(GameType.NOVICE, new HighScore(GameType.NOVICE, DEFAULT_PLAYER_NAME, SCORE));
            highScore.put(GameType.MEDIUM, new HighScore(GameType.MEDIUM, DEFAULT_PLAYER_NAME, SCORE));
            highScore.put(GameType.EXPERT, new HighScore(GameType.EXPERT, DEFAULT_PLAYER_NAME, SCORE));
            return highScore;
        }
    }

    public static void saveHighScore(Map<GameType, HighScore> highScore) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILENAME);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(highScore);
            objectOutputStream.flush();
        }
    }

    public static Map<GameType, HighScore> readHighScore() throws ClassCastException, ClassNotFoundException, IOException {
        try (FileInputStream fileInputStream = new FileInputStream(FILENAME);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (Map<GameType, HighScore>) objectInputStream.readObject();
        }
    }


}
