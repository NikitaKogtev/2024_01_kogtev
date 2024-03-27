package ru.kogtev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

//        if (args.length > 2) {
//            throw new ArrayIndexOutOfBoundsException("Аргументы введены неверно, используйте форму: " +
//                    "java Main <input_file> <output_file/console>");
//        }

        String inputFilename = "input.txt";
        String outputParameter = "console";

        try {
            Figure figure = FigureReader.figureReaderFromFile(inputFilename);
            if (outputParameter.equals("console")) {
                Printer.printFigureInConsole(figure);
            } else {
                Printer.printFigureInFile(figure);
            }

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

    }
}

