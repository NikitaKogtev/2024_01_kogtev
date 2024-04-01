package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Figure {
    protected static final Logger logger = LogManager.getLogger(Figure.class);

    protected double area;
    protected double perimeter;

    public abstract String getName();

    public double getArea() {
        return area;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public String getInfo() {
        return String.format("Тип фигуры: %s%nПлощадь: %.2f кв. мм%nПериметр: %.2f мм%n", getName(), area, perimeter);
    }
}
