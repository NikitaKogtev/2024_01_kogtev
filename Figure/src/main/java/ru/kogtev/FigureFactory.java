package ru.kogtev;

public class FigureFactory {
    public static Figure createFigure(String typeReader, double[] params) {
        switch (typeReader) {
            case "CIRCLE":
                return new Circle(params[0]);
            case "RECTANGLE":
                return new Rectangle(params[0], params[1]);
            case "TRIANGLE":
                return new Triangle(params[0], params[1], params[2]);
            default:
                throw new IllegalArgumentException("Неверный тип фигуры: " + typeReader);
        }
    }
}
