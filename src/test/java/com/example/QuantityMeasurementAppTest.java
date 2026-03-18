package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.QuantityMeasurementApp.LengthUnit;
import com.example.QuantityMeasurementApp.Quantity;

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

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(2.0, LengthUnit.FEET);

        Quantity result = q1.add(q2);

        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        Quantity q1 = new Quantity(6.0, LengthUnit.INCH);
        Quantity q2 = new Quantity(6.0, LengthUnit.INCH);

        Quantity result = q1.add(q2);

        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        Quantity result = q1.add(q2);

        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        Quantity q1 = new Quantity(12.0, LengthUnit.INCH);
        Quantity q2 = new Quantity(1.0, LengthUnit.FEET);

        Quantity result = q1.add(q2);

        assertEquals(24.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }

    @Test
    void testAddition_YardPlusFeet() {
        Quantity q1 = new Quantity(1.0, LengthUnit.YARDS);
        Quantity q2 = new Quantity(3.0, LengthUnit.FEET);

        Quantity result = q1.add(q2);

        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }

    @Test
    void testAddition_CentimeterPlusInch() {
        Quantity q1 = new Quantity(2.54, LengthUnit.CENTIMETERS);
        Quantity q2 = new Quantity(1.0, LengthUnit.INCH);

        Quantity result = q1.add(q2);

        assertEquals(5.08, result.getValue(), 1e-2);
        assertEquals(LengthUnit.CENTIMETERS, result.getUnit());
    }

    @Test
    void testAddition_WithZero() {
        Quantity q1 = new Quantity(5.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(0.0, LengthUnit.INCH);

        Quantity result = q1.add(q2);

        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_NegativeValues() {
        Quantity q1 = new Quantity(5.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(-2.0, LengthUnit.FEET);

        Quantity result = q1.add(q2);

        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_NullSecondOperand() {
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> q1.add(null));
    }
}