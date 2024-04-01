package ru.kogtev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RectangleTest {

    @Test
    void testRectangle() {
        Rectangle rectangle = new Rectangle(4, 5);
        assertEquals("Прямоугольник", rectangle.getName());
        assertEquals(20, rectangle.getArea(), 0.01);
        assertEquals(18, rectangle.getPerimeter(), 0.01);
        assertEquals(6.40, rectangle.getDiagonal(), 0.01);
        assertEquals(5, rectangle.getLength());
        assertEquals(4, rectangle.getWidth());
    }
}
