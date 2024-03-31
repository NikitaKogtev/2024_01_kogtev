package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final class Printer {
    private static final Logger logger = LogManager.getLogger(Printer.class);

    private Printer() {

    }

    public static void printFigureInConsole(Figure figure) {
        System.out.println(figure.getInfo());
        logger.info("Фигура успешно выведена в консоль");
    }

    public static void printFigureInFile(Figure figure) {
        String fileName = "output.txt";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(figure.getInfo());
            logger.info("Фигура успешно записана в файл: {} ", fileName);
        } catch (IOException e) {
            logger.error("Ошибка вывода данных: {}", e.getMessage());
        }
    }
}
