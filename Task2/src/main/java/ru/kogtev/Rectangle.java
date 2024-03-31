package ru.kogtev;

public class Rectangle extends Figure {
    private final double length;
    private final double width;
    private final double diagonal;


    public Rectangle(double length, double width) {
        if (length < 0 || width < 0) {
            logger.error("Переданные значение меньше нуля, невозможно создать {} с заданными параметрами", getName().toLowerCase());
            System.exit(2);
        }

        this.length = Math.max(length, width);
        this.width = Math.min(length, width);

        diagonal = Math.sqrt(length * length + width * width);
        area = length * width;
        perimeter = 2 * (length + width);

        logger.info("{} был создан успешно", getName());
    }

    @Override
    public String getName() {
        if (length == width) {
            return "Квадрат";
        }
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
        return String.format("Тип фигуры: %s%nПлощадь: %.2f кв. мм%nПериметр: %.2f мм%nДлина диагонали: %.2f мм%nДлина: %.2f мм%nШирина: %.2f мм%n",
                getName(), area, perimeter, diagonal, length, width);
    }
}
