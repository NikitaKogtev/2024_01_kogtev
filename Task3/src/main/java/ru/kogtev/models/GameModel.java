package ru.kogtev.models;

import ru.kogtev.view.GameType;

import java.io.*;
import java.util.*;

public class GameModel {
    private final int NOVICE_ROWS_AMOUNT = 10;
    private final int NOVICE_COLS_AMOUNT = 10;
    private final int NOVICE_MINES_AMOUNT = 10;
    private final int MEDIUM_ROWS_AMOUNT = 16;
    private final int MEDIUM_COLS_AMOUNT = 16;
    private final int MEDIUM_MINES_AMOUNT = 40;
    private final int EXPERT_ROWS_AMOUNT = 16;
    private final int EXPERT_COLS_AMOUNT = 30;
    private final int EXPERT_MINES_AMOUNT = 99;
    private final int DELAY = 1000;
    private final int PERIOD = 1000;

    private final String FILENAME = "highscore.txt";

    private int score;

    private BoardModel boardModel;

    private GameType gameType;

    private boolean firstClick;

    private Timer timer;
    private List<TimerListener> listeners = new ArrayList<>();
    private int elapsedTimer;


    Map<GameType, HighScore> highScore;

    private int remainingMines; // количество оставшихся мин

    private boolean gameOver;  // флаг, указывающий на завершение игры
    private boolean gameWon;  // флаг, указывающий на победу в игре\

    public GameModel(GameType gameType) {
        this.gameType = gameType;
        boardModel = gameDifficultyChoose();
        initializeHighScore();
    }

    private void initializeHighScore() {
        try {
            highScore = readHighScore();
        } catch (IOException | ClassNotFoundException e) {
            highScore = new HashMap<>();
            highScore.put(GameType.NOVICE, new HighScore(GameType.NOVICE, "Unknown", 9999));
            highScore.put(GameType.MEDIUM, new HighScore(GameType.MEDIUM, "Unknown", 9999));
            highScore.put(GameType.EXPERT, new HighScore(GameType.EXPERT, "Unknown", 9999));
        }
    }


    public void start() {
        stopTimer();
        elapsedTimer = 0;
        notifyListeners();
        listeners = new ArrayList<>();
        boardModel = gameDifficultyChoose();
        gameWon = false;
        gameOver = false;
        firstClick = true;
        remainingMines = boardModel.getTotalMines();
    }

    public BoardModel gameDifficultyChoose() {
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

    public boolean isFirstClick() {
        return firstClick;
    }

    public void openCell(int row, int col) {
        if (firstClick) {
            boardModel.generateCellValueOnBoard(row, col);
            startTimer();
            firstClick = false;
        }

        // Если ячейка уже открыта или поставлен флаг то return
        if (getBoardModel().getOpenedCellValue(row, col) || getBoardModel().getFlaggedCellValue(row, col)) {
            return;
        }

        // Меняем ячейку с закрытой на открытую
        boardModel.setOpenedCellValue(row, col, true);

        if (boardModel.getBoardCellValue(row, col) == -1) {
            gameOver = true;
        }

        if (boardModel.getBoardCellValue(row, col) == 0) {
            openAdjacentCells(row, col);
        }

        checkGameWon();

    }

    private void openAdjacentCells(int row, int col) {
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {

                int nRow = row + dRow;
                int nCol = col + dCol;

                if (isValidCell(nRow, nCol) && !boardModel.getOpenedCellValue(nRow, nCol) &&
                        !boardModel.getFlaggedCellValue(nRow, nCol)) {

                    boardModel.setOpenedCellValue(nRow, nCol, true);

                    if (boardModel.getBoardCellValue(nRow, nCol) == 0) {
                        openAdjacentCells(nRow, nCol);
                    }
                }
            }
        }
    }


    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < boardModel.getRows() && col >= 0 && col < boardModel.getCols();
    }

    public void toggleFlag(int row, int col) {

        if (remainingMines == 0 && !boardModel.getFlaggedCellValue(row, col)
                || boardModel.getOpenedCellValue(row, col)) {
            return;
        }

        boardModel.setFlaggedCellValue(row, col, !boardModel.getFlaggedCellValue(row, col));

        if (boardModel.getFlaggedCellValue(row, col)) {
            remainingMines--;
        } else {
            remainingMines++;
        }
    }

    public void openSurroundingCellsIfFlagged(int row, int col) {
        // Проверяем, что координаты клетки валидны и клетка открыта
        if (!isValidCell(row, col) || !boardModel.getOpenedCellValue(row, col)
                || boardModel.getBoardCellValue(row, col) == 0) {
            return;
        }

        int flagsAround = countFlagsAround(row, col);

        // Проверяем, что количество флажков вокруг равно значению в текущей клетке
        if (flagsAround == boardModel.getBoardCellValue(row, col)) {
            // Открываем все закрытые клетки вокруг текущей
            for (int dRow = -1; dRow <= 1; dRow++) {
                for (int dCol = -1; dCol <= 1; dCol++) {
                    int nRow = row + dRow;
                    int nCol = col + dCol;
                    // Проверяем, что новые координаты являются валидными ячейками и клетка закрыта
                    if (isValidCell(nRow, nCol) && !boardModel.getOpenedCellValue(nRow, nCol) && //duplicate
                            !boardModel.getFlaggedCellValue(nRow, nCol)) {
                        openCell(nRow, nCol);
                    }
                }
            }
        }
    }

    private int countFlagsAround(int row, int col) {
        int count = 0;
        // Проверяем количество флажков вокруг клетки
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                int nRow = row + dRow;
                int nCol = col + dCol;
                // Проверяем, что новые координаты являются валидными ячейками и там установлен флажок
                if (isValidCell(nRow, nCol) && boardModel.getFlaggedCellValue(nRow, nCol)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void checkGameWon() {
        for (int row = 0; row < boardModel.getRows(); row++) {
            for (int col = 0; col < boardModel.getCols(); col++) {
                if (boardModel.getBoardCellValue(row, col) != -1 && !boardModel.getOpenedCellValue(row, col)) {
                    return; // Если есть не пройденная ячейка без мин, игра не выиграна
                }
            }
        }
        gameWon = true;
    }

    public void openAllMines() {
        for (int row = 0; row < boardModel.getRows(); row++) {
            for (int col = 0; col < boardModel.getCols(); col++) {
                if (boardModel.getBoardCellValue(row, col) == -1) {
                    boardModel.setOpenedCellValue(row, col, true);
                }
            }
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTimer++;
                notifyListeners();
            }
        }, DELAY, PERIOD);
    }

    private void notifyListeners() {
        for (TimerListener listener : listeners) {
            listener.onTimerTick(elapsedTimer);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            score = elapsedTimer;
            timer.cancel();
        }
    }

    public void saveHighScore() throws IOException {
        FileOutputStream fos = new FileOutputStream(FILENAME);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(highScore);
        oos.flush();
        oos.close();
    }

    public Map<GameType, HighScore> readHighScore() throws IOException, ClassNotFoundException {
        FileInputStream fos = new FileInputStream(FILENAME);
        ObjectInputStream oos = new ObjectInputStream(fos);

        Map<GameType, HighScore> highScore = (Map<GameType, HighScore>) oos.readObject();
        oos.close();
        return highScore;
    }

    public boolean checkRecord() {
        if (score < highScore.get(gameType).getScore()) {
            highScore.get(gameType).setScore(score);
            return true;
        }
        return false;
    }

    public int getRemainingMines() {
        return remainingMines;
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public void addTimerListener(TimerListener listener) {
        listeners.add(listener);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public Map<GameType, HighScore> getHighScore() {
        return highScore;
    }

    public GameType getGameType() {
        return gameType;
    }
}
