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
    public static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            logger.info("Начало работы приложения");

            logger.info("Введите значение для расчета диапазона: ");
            long valueNum = Integer.parseInt(reader.readLine());

            logger.info("Диапазон для вычисления суммы равен: {}", valueNum);

            ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
            logger.info("Создано {} потоков", NUM_THREADS);

            long chunksSize = valueNum / NUM_THREADS;
            logger.info("Вычисление разбито на диапазоны: {}", chunksSize);

            Task[] tasks = new Task[NUM_THREADS];

            for (int i = 0; i < NUM_THREADS; i++) {
                long start = i * chunksSize;
                long end;

                if (i == NUM_THREADS - 1) {
                    end = valueNum;
                } else {
                    end = (i + 1) * chunksSize;
                }

                tasks[i] = new Task(start, end);
                executorService.submit(tasks[i]);
                logger.info("Отправка {} пула потоков", i);
            }

            executorService.shutdown();
            logger.info("Упорядоченное завершение работы");


            executorService.awaitTermination(60, TimeUnit.SECONDS);
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
