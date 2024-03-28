package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Figure {
    protected static final Logger logger = LogManager.getLogger(Figure.class);
    protected double area;
    protected double perimeter;

    public abstract String getName();

    public abstract double getArea();

    public abstract double getPerimeter();

    public abstract String getInfo();
}
