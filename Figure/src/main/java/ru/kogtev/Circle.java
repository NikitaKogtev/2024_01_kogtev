package ru.kogtev;

public class Circle implements Figure {
    private double radius;
    private double diameter;
    private double area;
    private double perimeter;


    public Circle(double radius) {
        this.radius = radius;

        diameter = 2 * radius;
        area = Math.PI * radius * radius;
        perimeter = 2 * Math.PI * radius;
    }

    @Override
    public String getName() {
        return "Круг";
    }

    public double getRadius() {
        return radius;
    }

    public double getDiameter() {
        return diameter;
    }

    @Override
    public double getArea() {
        return area;
    }

    @Override
    public double getPerimeter() {
        return perimeter;
    }

    @Override
    public String getInfo() {
        return String.format("Радиус: %.2f мм%nДиаметр: %.2f мм%n", radius, diameter);
    }


}
