package ru.kogtev;

public class Circle implements Figure {
    private double radius;
    private double diameter = 2 * radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public String getName() {
        return "Круг";
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String getInfo() {
        return String.format("Радиус: %.2f мм%nДиаметр: %.2f мм%n", radius, diameter);
    }
}
