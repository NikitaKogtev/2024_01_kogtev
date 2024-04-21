package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

public class Storage {
    private static final Logger logger = LogManager.getLogger(Storage.class);

    private final int storageSize;
    private final Queue<Resource> resources = new LinkedList<>();

    public Storage(int storageSize) {
        this.storageSize = storageSize;
        logger.info("Создан склад, вместимость {} ресурсов", storageSize);
    }

    public synchronized void addResources(Resource resource) throws InterruptedException {
        while (resources.size() >= storageSize) {
            logger.warn("Производители ждут, склад полон (поток - {} в режиме ожидания)",
                    Thread.currentThread().getId());
            wait();
        }
        resources.add(resource);
        logger.info("Произведен ресурс - {}, на складе {} товаров (поток {} продолжает работу)",
                resource.getId(), resources.size(), Thread.currentThread().getId());
        notifyAll();
    }

    public synchronized Resource takeResources() throws InterruptedException {
        while (resources.isEmpty()) {
            logger.warn("Потребители ждут, склад пуст (поток - {} в режиме ожидания)",
                    Thread.currentThread().getId());
            wait();
        }
        Resource resource = resources.poll();
        logger.info("Потреблен ресурс - {}, на складе {} товаров (поток - {} продолжает работу)",
                resource.getId(), resources.size(), Thread.currentThread().getId());
        notifyAll();
        return resource;
    }
}
