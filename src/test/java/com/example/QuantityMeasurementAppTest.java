package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.QuantityMeasurementApp.LengthUnit;

class QuantityLengthTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testFeetToInches() {
        assertEquals(12.0,
                QuantityMeasurementApp.convert(1.0, LengthUnit.FEET, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testYardsToFeet() {
        assertEquals(9.0,
                QuantityMeasurementApp.convert(3.0, LengthUnit.YARDS, LengthUnit.FEET),
                EPSILON);
    }

    @Test
    void testCmToInches() {
        assertEquals(1.0,
                QuantityMeasurementApp.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testSameUnit() {
        assertEquals(5.0,
                QuantityMeasurementApp.convert(5.0, LengthUnit.FEET, LengthUnit.FEET),
                EPSILON);
    }

    @Test
    void testNegativeValue() {
        assertEquals(-12.0,
                QuantityMeasurementApp.convert(-1.0, LengthUnit.FEET, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testInvalidUnit() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityMeasurementApp.convert(1.0, null, LengthUnit.FEET));
    }

    @Test
    void testNaNValue() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityMeasurementApp.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH));
    }
}