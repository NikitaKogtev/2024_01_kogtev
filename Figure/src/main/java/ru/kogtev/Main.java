package ru.kogtev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        if (args.length > 2) {
            throw new ArrayIndexOutOfBoundsException("Аргументы введены неверно, используйте форму: " +
                    "java Main <input_file> <output_file/console>");
        }

        String inputFilename = args[0];
        String outputParameter = args[1];

        try {
            Figure figure = FigureReader.figureReaderFromFile(inputFilename);
            Printer printer = new Printer(figure);

            if (outputParameter.equals("console")) {
                printer.printFigureInConsole();
            } else {
                printer.printFigureInFile();
            }

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        } catch (IllegalArgumentException e) {
            System.out.println("Данные введены некорректно");
        }

    }
}

