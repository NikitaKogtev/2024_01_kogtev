package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        // Считывание данных их .properties файла
        int producerCount = 5;
        int consumerCount = 3;
        int producerTime = 1000;
        int consumerTime = 1500;
        int storageSize = 10;

        Storage storage = new Storage(storageSize);

        for (int i = 0; i < producerCount; i++) {
            Thread producerThread = new Thread(new Producer(i, producerTime, storage));
            producerThread.start();
            logger.info("Создание нового Producer - {}, и запуск производства", i);
        }

        for (int i = 0; i < consumerCount; i++) {
            Thread consumerThread = new Thread(new Consumer(i, consumerTime, storage));
            consumerThread.start();
            logger.info("Создание нового Consumer - {}, и запуск потребления", i);
        }


    }
}