package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void testEquality_FeetToFeet_SameValue() {
        QuantityMeasurementApp.Quantity q1 =
                new QuantityMeasurementApp.Quantity(1.0, QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Quantity q2 =
                new QuantityMeasurementApp.Quantity(1.0, QuantityMeasurementApp.LengthUnit.FEET);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        QuantityMeasurementApp.Quantity q1 =
                new QuantityMeasurementApp.Quantity(1.0, QuantityMeasurementApp.LengthUnit.INCH);

        QuantityMeasurementApp.Quantity q2 =
                new QuantityMeasurementApp.Quantity(1.0, QuantityMeasurementApp.LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_FeetToInch_Equivalent() {
        QuantityMeasurementApp.Quantity q1 =
                new QuantityMeasurementApp.Quantity(1.0, QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Quantity q2 =
                new QuantityMeasurementApp.Quantity(12.0, QuantityMeasurementApp.LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_DifferentValues() {
        QuantityMeasurementApp.Quantity q1 =
                new QuantityMeasurementApp.Quantity(1.0, QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Quantity q2 =
                new QuantityMeasurementApp.Quantity(2.0, QuantityMeasurementApp.LengthUnit.FEET);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_NullComparison() {
        QuantityMeasurementApp.Quantity q1 =
                new QuantityMeasurementApp.Quantity(1.0, QuantityMeasurementApp.LengthUnit.FEET);

        assertFalse(q1.equals(null));
    }

    @Test
    void testEquality_SameReference() {
        QuantityMeasurementApp.Quantity q1 =
                new QuantityMeasurementApp.Quantity(1.0, QuantityMeasurementApp.LengthUnit.FEET);

        assertTrue(q1.equals(q1));
    }
}