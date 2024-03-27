package ru.kogtev;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Начало работы приложения");

        if (args.length > 2) {
            logger.error("Аргументы введены неверно, используйте форму: java Main <input_file> <output_file/console>");
            throw new ArrayIndexOutOfBoundsException("Аргументы введены неверно, используйте форму: java Main <input_file> <output_file/console>");
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
            logger.error("Ошибка чтения файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Данные введены некорректно: " + e.getMessage());
        }

        logger.info("Конец работы приложения");

    }
}

