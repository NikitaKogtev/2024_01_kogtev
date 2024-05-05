package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Task implements Runnable {
    private static final Logger logger = LogManager.getLogger(Task.class);

    private final long start;
    private final long end;
    private final long valueNum;
    private double result;

    public Task(long start, long end, long valueNum) {
        this.start = start;
        this.end = end;
        this.valueNum = valueNum;
        logger.info("Создана задача с диапазоном от {} до {}", end, start);
    }

    public double getResult() {
        logger.info("Результат вычисления диапазона от {} до {} : {}", start, end, result);
        return result;
    }

    @Override
    public void run() {
        double sum = 0;
        for (long i = start; i < end; i++) {
            sum += 1.0 / Math.pow(2, i);
        }

        if (end == valueNum) {
            sum += 1.0 / Math.pow(2, end);
        }

        result = sum;
    }
}
