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
    }

    public void printFigureInFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"))) {
            bufferedWriter.write(output);
        } catch (IOException e) {
            logger.error("Ошибка вывода данных: " + e.getMessage());
            System.out.println("Ошибка вывода данных: " + e.getMessage());
        }
    }
}
