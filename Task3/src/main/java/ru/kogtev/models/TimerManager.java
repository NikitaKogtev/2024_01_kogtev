package ru.kogtev.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
    private static final int DELAY = 1000;
    private static final int PERIOD = 1000;

    private static Timer timer;

    static List<TimerListener> timerListeners = new ArrayList<>();
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

    public static void addTimerListener(TimerListener timerListener) {
        timerListeners.add(timerListener);
    }

    public static void notifyListeners() {
        for (TimerListener timerListener : timerListeners) {
            timerListener.onTimerTick(elapsedTimer);
        }
    }

    public static void stopTimer() {
        if (timer != null) {
            score = elapsedTimer;
            timer.cancel();
        }
    }
}
