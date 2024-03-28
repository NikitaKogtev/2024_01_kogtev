package ru.kogtev;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Начало работы приложения");

        if (args.length > 2) {
            logger.error("Аргументы указаны неверно. Должны быть указаны <input_file> file/console>");
            System.exit(2);
        }

        String inputFilename = args[0];
        String outputParameter = args[1];

        try {
            Figure figure = FigureReader.figureReaderFromFile(inputFilename);
            Printer printer = new Printer(figure);

            if (outputParameter.equals("console")) {
                printer.printFigureInConsole();
            }
            if (outputParameter.equals("file")) {
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

