package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FigureCreator {
    private static final Logger logger = LogManager.getLogger(FigureCreator.class);

    private FigureCreator() {

    }

    public static Figure createFigure(String figureType, double[] params) {

        switch (FigureType.valueOf(figureType)) {
            case CIRCLE:
                if (params.length != 1) {
                    logger.error("Неверное количество параметров для круга. Ожидается 1 параметр, получено {}.",
                            params.length);
                    System.exit(2);
                }
                return new Circle(params[0]);
            case RECTANGLE:
                if (params.length != 2) {
                    logger.error("Неверное количество параметров для прямоугольника. Ожидается 2 параметра, получено {}.",
                            params.length);
                    System.exit(2);
                }
                return new Rectangle(params[0], params[1]);
            case TRIANGLE:
                if (params.length != 3) {
                    logger.error("Неверное количество параметров для треугольника. Ожидается 3 параметра, получено {}.",
                            params.length);
                    System.exit(2);
                }
                return new Triangle(params[0], params[1], params[2]);
            default:
                logger.error("Неверный тип фигуры: {}", figureType);
                throw new IllegalArgumentException("Неверный тип фигуры: " + figureType);
        }
    }
}
