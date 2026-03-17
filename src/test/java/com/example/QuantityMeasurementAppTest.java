package com.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    @Test
    void testEquality_SameValue() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet feet2 = new QuantityMeasurementApp.Feet(1.0);

        assertTrue(feet1.equals(feet2), "1.0 ft should equal 1.0 ft");
    }

    @Test
    void testEquality_DifferentValue() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet feet2 = new QuantityMeasurementApp.Feet(2.0);

        assertFalse(feet1.equals(feet2), "1.0 ft should not equal 2.0 ft");
    }

    @Test
    void testEquality_NullComparison() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);

        assertFalse(feet1.equals(null), "Feet should not equal null");
    }

    @Test
    void testEquality_SameReference() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);

        assertTrue(feet1.equals(feet1), "Object should equal itself");
    }

    @Test
    void testEquality_NonNumericInput() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        Object obj = "Not a number";

        assertFalse(feet1.equals(obj), "Feet should not equal non-numeric object");
    }

    @Test
    void testInchEquality_SameValue() {
        QuantityMeasurementApp.Inch i1 = new QuantityMeasurementApp.Inch(1.0);
        QuantityMeasurementApp.Inch i2 = new QuantityMeasurementApp.Inch(1.0);

        assertTrue(i1.equals(i2));
    }

    @Test
    void testInchEquality_DifferentValue() {
        QuantityMeasurementApp.Inch i1 = new QuantityMeasurementApp.Inch(1.0);
        QuantityMeasurementApp.Inch i2 = new QuantityMeasurementApp.Inch(2.0);

        assertFalse(i1.equals(i2));
    }
}