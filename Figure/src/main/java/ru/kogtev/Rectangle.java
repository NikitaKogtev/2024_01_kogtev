package ru.kogtev;

public class Rectangle extends Figure {
    private double length;
    private double width;
    private double diagonal;


    public Rectangle(double length, double width) {
        this.length = Math.max(length, width);
        this.width = Math.min(length, width);

        diagonal = Math.sqrt(length * length + width * width);
        area = length * width;
        perimeter = 2 * (length + width);
    }

    @Override
    public String getName() {
        return "Прямоугольник";
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getDiagonal() {
        return diagonal;
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
        return String.format("Длина диагонали: %.2f мм%nДлина: %.2f мм%nШирина: %.2f мм%n", diagonal,
                length, width);
    }
}
