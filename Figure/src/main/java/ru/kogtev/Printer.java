package ru.kogtev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Printer {

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
            System.out.println("Ошибка вывода данных");
        }
    }
}
