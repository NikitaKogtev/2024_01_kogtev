package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            logger.info("Начало работы приложения");

            logger.info("Введите значение для расчета диапазона: ");
            long valueNum = Long.parseLong(reader.readLine());

            logger.info("Диапазон для вычисления суммы равен: {}", valueNum);

            int numThreads = Math.min(Runtime.getRuntime().availableProcessors(), (int) valueNum);

            ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
            logger.info("Создано {} потоков", numThreads);

            long chunksSize = valueNum / numThreads;
            logger.info("Вычисление разбито на диапазоны: {}", chunksSize);

            Task[] tasks = new Task[numThreads];

            for (int i = 0; i < numThreads; i++) {
                long start = i * chunksSize;
                long end = Math.min((i + 1) * chunksSize, valueNum);

                tasks[i] = new Task(start, end, valueNum);
                executorService.submit(tasks[i]);
                logger.info("Отправка {} пула потоков", i + 1);
            }

            executorService.shutdown();
            logger.info("Упорядоченное завершение работы");


            executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            logger.info("Завершение и ожидание пока все задачи не завершаться");

            double totalSum = 0;

            for (Task task : tasks) {
                totalSum += task.getResult();
            }

            logger.info("Вычисление выполнено, результат {}", totalSum);

        } catch (IOException e) {
            logger.error("Введен неверный параметр: {}", e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Поток прерван {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
