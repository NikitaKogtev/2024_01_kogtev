package ru.kogtev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TriangleTest {

    @Test
    void testTriangle() {
        Triangle triangle = new Triangle(3, 4, 5);
        assertEquals("Треугольник", triangle.getName());
        assertEquals(6, triangle.getArea(), 0.01);
        assertEquals(12, triangle.getPerimeter(), 0.01);
        assertEquals(3, triangle.getFirstSide(), 0.01);
        assertEquals(4, triangle.getSecondSide(), 0.01);
        assertEquals(5, triangle.getThirdSide(), 0.01);
        assertEquals(36.87, triangle.getFirstSideAngle(), 0.01);
        assertEquals(53.13, triangle.getSecondSideAngle(), 0.01);
        assertEquals(90.00, triangle.getThirdSideAngle(), 0.01);

    }
}
