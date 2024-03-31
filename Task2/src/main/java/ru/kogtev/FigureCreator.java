package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FigureCreator {
    private static final Logger logger = LogManager.getLogger(FigureCreator.class);

    public static Figure createFigure(String figureType, double[] params) {
        switch (FigureType.valueOf(figureType)) {
            case CIRCLE:
                return new Circle(params[0]);
            case RECTANGLE:
                return new Rectangle(params[0], params[1]);
            case TRIANGLE:
                return new Triangle(params[0], params[1], params[2]);
            default:
                logger.error("Неверный тип фигуры: {}", figureType);
                throw new IllegalArgumentException();
        }
    }
}
