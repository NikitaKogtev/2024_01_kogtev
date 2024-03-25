package ru.kogtev;

public class Triangle implements Figure {
    private double firstSide;
    private double secondSide;
    private double thirdSide;

    private double firstSideAngle = Math.toDegrees(Math.acos(firstSide * firstSide + thirdSide * thirdSide -
            firstSide * firstSide / (2 * secondSide * thirdSide)));
    private double secondSideAngle = Math.toDegrees(Math.acos((firstSide * firstSide + thirdSide * thirdSide -
            secondSide * secondSide) / (2 * firstSide * thirdSide)));
    private double thirdSideAngle = 180 - firstSideAngle - secondSideAngle;

    public Triangle(double firstSide, double secondSide, double thirdSide) {
        this.firstSide = firstSide;
        this.secondSide = secondSide;
        this.thirdSide = thirdSide;
    }

    @Override
    public String getName() {
        return "Треугольник";
    }

    @Override
    public double getArea() {
        double halfMeter = (firstSide + secondSide + thirdSide) / 2;
        return Math.sqrt(halfMeter * (halfMeter - firstSide) * (halfMeter - secondSide) * (halfMeter - thirdSide));
    }

    @Override
    public double getPerimeter() {
        return firstSide + secondSide + thirdSide;
    }

    @Override
    public String getInfo() {
        return String.format("Сторона 1: %.2f мм, Противоположный угол: %2.f градусов%n " +
                        "Сторона 2: %.2f мм, Противоположный угол: %2.f градусов%n" +
                        "Сторона 3: %.2f мм, Противоположный угол: %2.f градусов%n",
                firstSide, firstSideAngle, secondSide, secondSideAngle, thirdSide, thirdSideAngle);
    }
}
