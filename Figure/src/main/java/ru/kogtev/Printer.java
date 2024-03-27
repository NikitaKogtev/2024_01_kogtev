package ru.kogtev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Printer {

    //для того чтобы убрать дублирование в переменной output
//    private Figure figure;
//
//    public Printer(Figure figure) {
//        this.figure = figure;
//    }

    public static void printFigureInConsole(Figure figure) {
        String output = "Тип фигуры: " + figure.getName() + "\n" +
                "Площадь: " + figure.getArea() + " кв. мм\n" +
                "Периметр: " + figure.getPerimeter() + " мм\n" +
                figure.getInfo();

        System.out.println(output);

    }

    public static void printFigureInFile(Figure figure) {

        String outputFileName = "output.txt";
        String output = "Тип фигуры: " + figure.getName() + "\n" +
                "Площадь: " + figure.getArea() + " кв. мм\n" +
                "Периметр: " + figure.getPerimeter() + " мм\n" +
                figure.getInfo();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {
            bufferedWriter.write(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
