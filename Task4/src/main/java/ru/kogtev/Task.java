package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Task implements Runnable {
    private static final Logger logger = LogManager.getLogger(Task.class);

    private final long start;
    private final long end;
    private double result;

    public Task(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public double getResult() {
        logger.info("Результат вычисления диапазона: {}", result);
        return result;
    }

    @Override
    public void run() {
        logger.info("Создание потока в диапазоне от {} до {}", start, end);
        double sum = 0;
        for (long i = start; i <= end; i++) {
            sum += 1.0 / Math.pow(2, i);
        }
        result = sum;
    }
}
