package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Consumer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Consumer.class);

    private final int id;
    private final int consumerTime;
    private final Storage storage;

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
                logger.info("Потребитель - {} забрал ресурс - {} со склада", id, resource.getId());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
