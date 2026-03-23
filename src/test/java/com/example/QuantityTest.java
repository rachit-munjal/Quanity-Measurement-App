package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testLengthEquality() {
        assertEquals(
                new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCHES)
        );
    }

    @Test
    void testWeightEquality() {
        assertEquals(
                new Quantity<>(1.0, WeightUnit.KILOGRAM),
                new Quantity<>(1000.0, WeightUnit.GRAM)
        );
    }

    @Test
    void testLengthConversion() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);
        assertEquals(12.0,
                q.convertTo(LengthUnit.INCHES).getValue(),
                EPSILON);
    }

    @Test
    void testWeightConversion() {
        Quantity<WeightUnit> q = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertEquals(1000.0,
                q.convertTo(WeightUnit.GRAM).getValue(),
                EPSILON);
    }

    @Test
    void testLengthAddition() {
        Quantity<LengthUnit> result =
                new Quantity<>(1.0, LengthUnit.FEET)
                        .add(new Quantity<>(12.0, LengthUnit.INCHES));

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testWeightAddition() {
        Quantity<WeightUnit> result =
                new Quantity<>(1.0, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1000.0, WeightUnit.GRAM));

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAdditionWithTargetUnit() {
        Quantity<LengthUnit> result =
                new Quantity<>(1.0, LengthUnit.FEET)
                        .add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.INCHES);

        assertEquals(24.0, result.getValue(), EPSILON);
    }

    @Test
    void testInvalidInputs() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null));

        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testNullOperations() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> q.add(null));
        assertThrows(IllegalArgumentException.class, () -> q.convertTo(null));
    }

    @Test
    void testCrossCategoryNotEqual() {
        assertNotEquals(
                new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(1.0, WeightUnit.KILOGRAM)
        );
    }
}