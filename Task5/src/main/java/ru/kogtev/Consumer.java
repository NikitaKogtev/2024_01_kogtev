package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Consumer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Consumer.class);

    private int id;
    private int consumerTime;
    private Storage storage;

    public Consumer(int id, int consumerTime, Storage storage) {
        this.id = id;
        this.consumerTime = consumerTime;
        this.storage = storage;
    }


    @Override
    public void run() {
        while (true) {
            try {
                Resource resource = storage.takeResources();
                Thread.sleep(consumerTime);
                logger.info("Получен ресурс c id - {} Consumer - {}", resource.getId(), id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}
