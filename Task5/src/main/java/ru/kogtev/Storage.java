package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

public class Storage {
    private static final Logger logger = LogManager.getLogger(Storage.class);

    private int storageSize;
    private Queue<Resource> resources = new LinkedList<>();

    public Storage(int storageSize) {
        this.storageSize = storageSize;
        logger.info("Создан склад, который вмещает в себя {} ресурсов", storageSize);
    }

    public synchronized void addResources(Resource resource) throws InterruptedException {
        while (resources.size() >= storageSize) {
            logger.warn("Склад переполнен, ждем освобождения склада для добавления ресурсов");
            wait();
        }
        logger.info("На склад добавлен ресурс c id - {}", resource.getId());
        resources.add(resource);
        notifyAll();
    }

    public synchronized Resource takeResources() throws InterruptedException {
        while (resources.isEmpty()) {
            logger.warn("Склад пуст, ждем поступления новых ресурсов на склад");
            wait();
        }
        Resource resource = resources.poll();
        logger.warn("Со склада забрали ресурс с id - {}", resource.getId());
        notifyAll();
        return resource;
    }

}
