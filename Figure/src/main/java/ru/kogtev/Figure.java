package ru.kogtev;

public abstract class Figure {
    protected double area;
    protected double perimeter;

    public abstract String getName();

    public abstract double getArea();

    public abstract double getPerimeter();

    public abstract String getInfo();
}
