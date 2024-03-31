package ru.kogtev;

public class Triangle extends Figure {
    private final double firstSide;
    private final double secondSide;
    private final double thirdSide;
    private final double halfMeter;
    private final double firstSideAngle;
    private final double secondSideAngle;
    private final double thirdSideAngle;

    public Triangle(double firstSide, double secondSide, double thirdSide) {

        if (!((firstSide + secondSide > thirdSide) && (firstSide + thirdSide > secondSide)
                && (secondSide + thirdSide > firstSide))) {
            logger.error("Невозможно создать треугольник с заданными сторонами");
            System.exit(2);
        }

        this.firstSide = firstSide;
        this.secondSide = secondSide;
        this.thirdSide = thirdSide;

        halfMeter = (firstSide + secondSide + thirdSide) / 2;
        area = Math.sqrt(halfMeter * (halfMeter - firstSide) * (halfMeter - secondSide) * (halfMeter - thirdSide));
        perimeter = firstSide + secondSide + thirdSide;

        firstSideAngle = Math.toDegrees(Math.acos((secondSide * secondSide + thirdSide * thirdSide -
                firstSide * firstSide) / (2 * secondSide * thirdSide)));
        secondSideAngle = Math.toDegrees(Math.acos((firstSide * firstSide + thirdSide * thirdSide -
                secondSide * secondSide) / (2 * firstSide * thirdSide)));
        thirdSideAngle = 180 - firstSideAngle - secondSideAngle;

        logger.info(getName() + " был создан успешно");
    }

    @Override
    public String getName() {
        return "Треугольник";
    }

    public double getFirstSide() {
        return firstSide;
    }

    public double getSecondSide() {
        return secondSide;
    }

    public double getThirdSide() {
        return thirdSide;
    }

    public double getFirstSideAngle() {
        return firstSideAngle;
    }

    public double getSecondSideAngle() {
        return secondSideAngle;
    }

    public double getThirdSideAngle() {
        return thirdSideAngle;
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
        return String.format(
                        "Тип фигуры: %s%nПлощадь: %.2f кв. мм%nПериметр: %.2f мм%n" +
                        "Сторона 1: %.2f мм, Противоположный угол: %.2f градусов%n" +
                        "Сторона 2: %.2f мм, Противоположный угол: %.2f градусов%n" +
                        "Сторона 3: %.2f мм, Противоположный угол: %.2f градусов%n",
                getName(), area, perimeter, firstSide, firstSideAngle, secondSide, secondSideAngle, thirdSide, thirdSideAngle);
    }
}
