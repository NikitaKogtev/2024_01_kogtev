package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class FigureReader {
    private static final Logger logger = LogManager.getLogger(FigureReader.class);

    public static Figure figureReaderFromFile(String inputFilename) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilename))) {

            String typeReader = bufferedReader.readLine();
            String[] paramsStr = bufferedReader.readLine().split(" ");
            double[] params = new double[paramsStr.length];

            for (int i = 0; i < paramsStr.length; i++) {
                params[i] = Double.parseDouble(paramsStr[i]);
            }

            logger.info("Чтения фигуры из файла выполнено успешно");

            return FigureCreator.createFigure(typeReader, params);
        }
    }
}
