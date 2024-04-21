package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Producer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Producer.class);

    private final int id;
    private final int producerTime;
    private final Storage storage;

    public Producer(int id, int producerTime, Storage storage) {
        this.id = id;
        this.producerTime = producerTime;
        this.storage = storage;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(producerTime);
                Resource resource = new Resource();
                storage.addResources(resource);
                logger.info("Производитель - {} доставил ресурс - {} на склад", id, resource.getId());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
