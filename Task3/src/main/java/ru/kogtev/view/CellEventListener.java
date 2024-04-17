package ru.kogtev.view;

import java.io.IOException;

public interface CellEventListener {
    void onMouseClick(int x, int y, ButtonType buttonType) throws IOException;
}
