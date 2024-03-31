package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public final class FigureReader {
    private static final Logger logger = LogManager.getLogger(FigureReader.class);
    private static final String SPLITTER = " ";

    private FigureReader() {

    }

    public static Figure figureReaderFromFile(String inputFilename) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilename))) {

            String figureType = bufferedReader.readLine();
            String[] paramsStr = bufferedReader.readLine().split(SPLITTER);
            double[] params = new double[paramsStr.length];

            for (int i = 0; i < paramsStr.length; i++) {
                params[i] = Double.parseDouble(paramsStr[i]);
            }

            logger.info("Чтение фигуры из файла выполнено успешно");

            return FigureCreator.createFigure(figureType, params);
        }
    }
}
