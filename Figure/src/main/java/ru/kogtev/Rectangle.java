package ru.kogtev;

public class Rectangle implements Figure {
    private double length;
    private double width;
    private double diagonal = Math.sqrt(length * length + width * width);

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    public String getName() {
        return "Прямоугольник";
    }

    @Override
    public double getArea() {
        return length * width;
    }

    @Override
    public double getPerimeter() {
        return 2 * (length + width);
    }

    @Override
    public String getInfo() {
        return String.format("Длина диагонали: %.2f мм%nДлина: %.2f мм%nШирина: %.2f мм%n", diagonal,
                Math.max(length, width), Math.min(length, width));
    }
}
