package ru.kogtev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FigureReader {
    public static Figure figureReaderFromFile(String inputFilename) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilename))) {
            String typeReader = bufferedReader.readLine();
            String[] paramsStr = bufferedReader.readLine().split(" ");
            double[] params = new double[paramsStr.length];

            for (int i = 0; i < paramsStr.length; i++) {
                params[i] = Double.parseDouble(paramsStr[i]);
            }

            return FigureCreator.createFigure(typeReader, params);
        }
    }
}
