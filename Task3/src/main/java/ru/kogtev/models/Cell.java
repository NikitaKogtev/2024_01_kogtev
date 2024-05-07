package ru.kogtev.models;

public class Cell {
    private int boardValue;
    private boolean opened;
    private boolean flagged;

    public Cell() {
        this.boardValue = 0;
        this.opened = false;
        this.flagged = false;
    }

    public int getBoardValue() {
        return boardValue;
    }

    public void setBoardValue(int boardValue) {
        this.boardValue = boardValue;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
