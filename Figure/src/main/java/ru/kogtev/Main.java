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
            String output = "Тип фигуры: " + figure.getName() + "\n" +
                    "Площадь: " + figure.getArea() + " кв. мм\n" +
                    "Периметр: " + figure.getPerimeter() + " мм\n" +
                    figure.getInfo();

            if (outputParameter.equals("console")) {
                System.out.println(output);
            } else {
                String outputFileName = "output.txt";

                try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {
                    bufferedWriter.write(outputFileName);
                }
            }

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

    }
}

