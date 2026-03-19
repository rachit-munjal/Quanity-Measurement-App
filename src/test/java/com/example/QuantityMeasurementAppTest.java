package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    private static final double EPSILON = 1e-6;

    // 🔹 UC1–UC4: Equality

    @Test
    void testEquality_SameUnit() {
        assertEquals(
                new Quantity(1.0, LengthUnit.FEET),
                new Quantity(1.0, LengthUnit.FEET)
        );
    }

    @Test
    void testEquality_CrossUnit() {
        assertEquals(
                new Quantity(1.0, LengthUnit.FEET),
                new Quantity(12.0, LengthUnit.INCH)
        );
    }

    @Test
    void testEquality_YardAndFeet() {
        assertEquals(
                new Quantity(1.0, LengthUnit.YARDS),
                new Quantity(3.0, LengthUnit.FEET)
        );
    }

    // 🔹 UC5: Conversion

    @Test
    void testFeetToInches() {
        assertEquals(12.0,
                QuantityMeasurementApp.convert(1.0, LengthUnit.FEET, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testCmToInches() {
        assertEquals(1.0,
                QuantityMeasurementApp.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testConvertToMethod() {
        Quantity q = new Quantity(1.0, LengthUnit.FEET);
        assertEquals(12.0, q.convertTo(LengthUnit.INCH).getValue(), EPSILON);
    }

    // 🔹 UC6: Addition

    @Test
    void testAddition_FeetPlusFeet() {
        Quantity result =
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(2.0, LengthUnit.FEET));

        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_FeetPlusInch() {
        Quantity result =
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(12.0, LengthUnit.INCH));

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_InchPlusFeet() {
        Quantity result =
                new Quantity(12.0, LengthUnit.INCH)
                        .add(new Quantity(1.0, LengthUnit.FEET));

        assertEquals(24.0, result.getValue(), EPSILON);
    }

    // 🔹 UC7: Addition with target unit

    @Test
    void testAddition_TargetUnit_Feet() {
        Quantity result =
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(12.0, LengthUnit.INCH), LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_TargetUnit_Inch() {
        Quantity result =
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(12.0, LengthUnit.INCH), LengthUnit.INCH);

        assertEquals(24.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_TargetUnit_Yards() {
        Quantity result =
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(12.0, LengthUnit.INCH), LengthUnit.YARDS);

        assertEquals(0.6667, result.getValue(), 1e-3);
    }

    // 🔹 Edge cases

    @Test
    void testNullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity(1.0, null));
    }

    @Test
    void testNaNValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testAddNull() {
        Quantity q = new Quantity(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q.add(null));
    }

    @Test
    void testConvertNull() {
        Quantity q = new Quantity(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class,
                () -> q.convertTo(null));
    }
}