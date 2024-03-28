package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Printer {
    private static final Logger logger = LogManager.getLogger(Printer.class);

    private String output;

    public Printer(Figure figure) {
        output = String.format("Тип фигуры: %s%nПлощадь: %.2f кв. мм%nПериметр: %.2f мм%n%s",
                figure.getName(), figure.getArea(), figure.getPerimeter(), figure.getInfo());
    }

    public void printFigureInConsole() {
        System.out.println(output);
        logger.info("Фигура успешно выведена в консоль");
    }

    public void printFigureInFile() {
        String fileName = "output.txt";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(output);
            logger.info("Фигура успешно записана в файл: " + fileName);
        } catch (IOException e) {
            logger.error("Ошибка вывода данных: " + e.getMessage());
        }
    }
}
