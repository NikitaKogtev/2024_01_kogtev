package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String PRODUCER_COUNT = "producerCount";
    private static final String CONSUMER_COUNT = "consumerCount";
    private static final String PRODUCER_TIME = "producerTime";
    private static final String CONSUMER_TIME = "consumerTime";
    private static final String STORAGE_SIZE = "storageSize";
    private static final String PROPERTIES_FILE = "config.properties";


    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);

            int producerCount = Integer.parseInt(properties.getProperty(PRODUCER_COUNT));
            int consumerCount = Integer.parseInt(properties.getProperty(CONSUMER_COUNT));
            int producerTime = Integer.parseInt(properties.getProperty(PRODUCER_TIME));
            int consumerTime = Integer.parseInt(properties.getProperty(CONSUMER_TIME));
            int storageSize = Integer.parseInt(properties.getProperty(STORAGE_SIZE));

            Storage storage = new Storage(storageSize);

            for (int i = 1; i <= producerCount; i++) {
                Thread producerThread = new Thread(new Producer(i, producerTime, storage));
                producerThread.start();
                logger.info("Создание нового Производителя - {}, и запуск потоков-производителя", i);
            }

            for (int i = 1; i <= consumerCount; i++) {
                Thread consumerThread = new Thread(new Consumer(i, consumerTime, storage));
                consumerThread.start();
                logger.info("Создание нового Потребителя - {}, и запуск потоков потребителя", i);
            }

        } catch (FileNotFoundException e) {
            logger.error("Не найден конфигурационный файл {}", e.getMessage());
            System.exit(2);
        } catch (IOException e) {
            logger.error("Параметры конфигурационного файла не верны {}", e.getMessage());
            System.exit(2);
        }
    }
}