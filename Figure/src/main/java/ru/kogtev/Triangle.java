package ru.kogtev;

public class Triangle extends Figure {
    private double firstSide;
    private double secondSide;
    private double thirdSide;
    private double halfMeter;
    private double firstSideAngle;
    private double secondSideAngle;
    private double thirdSideAngle;

    public Triangle(double firstSide, double secondSide, double thirdSide) {
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
        return String.format("Сторона 1: %.2f мм, Противоположный угол: %.2f градусов%n" +
                        "Сторона 2: %.2f мм, Противоположный угол: %.2f градусов%n" +
                        "Сторона 3: %.2f мм, Противоположный угол: %.2f градусов%n",
                firstSide, firstSideAngle, secondSide, secondSideAngle, thirdSide, thirdSideAngle);
    }
}
