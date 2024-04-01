package ru.kogtev;

public class Circle extends Figure {
    private final double radius;
    private final double diameter;

    public Circle(double radius) {

        if (radius < 0) {
            logger.error("Переданные значение меньше нуля, невозможно создать круг с заданными параметрами");
            System.exit(2);
        }

        this.radius = radius;

        diameter = 2 * radius;
        area = Math.PI * radius * radius;
        perimeter = 2 * Math.PI * radius;

        logger.info(getName() + " был создан успешно");
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
    public String getInfo() {
        return super.getInfo() + String.format("Радиус: %.2f мм%nДиаметр: %.2f мм%n", radius, diameter);
    }
}
