package ru.kogtev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CircleTest {

    @Test
    void testCircle() {
        Circle circle = new Circle(5);
        assertEquals("Круг", circle.getName());
        assertEquals(78.54, circle.getArea(), 0.01);
        assertEquals(31.42, circle.getPerimeter(), 0.01);
        assertEquals(5.00, circle.getRadius(), 0.01);
        assertEquals(10.00, circle.getDiameter(), 0.01);
    }
}
