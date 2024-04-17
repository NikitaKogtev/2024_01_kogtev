package ru.kogtev.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
    private static final int DELAY = 1000;
    private static final int PERIOD = 1000;

    private static Timer timer;

    static List<TimerListener> listeners = new ArrayList<>();
    static int elapsedTimer = 0;
    static int score;

    private TimerManager() {

    }

    public static void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTimer++;
                notifyListeners();
            }
        }, DELAY, PERIOD);
    }

    static void notifyListeners() {
        for (TimerListener listener : listeners) {
            listener.onTimerTick(elapsedTimer);
        }
    }

    public static void addTimerListener(TimerListener listener) {
        listeners.add(listener);
    }

    public static void stopTimer() {
        if (timer != null) {
            score = elapsedTimer;
            timer.cancel();
        }
    }
}
