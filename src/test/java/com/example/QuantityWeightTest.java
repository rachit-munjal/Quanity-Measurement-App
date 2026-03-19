package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityWeightTest {

    private static final double EPSILON = 1e-6;

    // 🔹 Equality

    @Test
    void testEquality_KgToKg() {
        assertEquals(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
        );
    }

    @Test
    void testEquality_KgToGram() {
        assertEquals(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityWeight(1000.0, WeightUnit.GRAM)
        );
    }

    @Test
    void testEquality_GramToPound() {
        assertEquals(
                new QuantityWeight(453.592, WeightUnit.GRAM),
                new QuantityWeight(1.0, WeightUnit.POUND)
        );
    }

    @Test
    void testEquality_Null() {
        QuantityWeight q = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertNotEquals(q, null);
    }

    // 🔹 Conversion

    @Test
    void testConversion_KgToGram() {
        QuantityWeight result =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.GRAM);

        assertEquals(1000.0, result.getValue(), EPSILON);
    }

    @Test
    void testConversion_PoundToKg() {
        QuantityWeight result =
                new QuantityWeight(2.20462, WeightUnit.POUND)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.0, result.getValue(), 1e-3);
    }

    @Test
    void testConversion_RoundTrip() {
        QuantityWeight q =
                new QuantityWeight(1.5, WeightUnit.KILOGRAM);

        QuantityWeight result =
                q.convertTo(WeightUnit.GRAM)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.5, result.getValue(), 1e-6);
    }

    // 🔹 Addition

    @Test
    void testAddition_KgPlusKg() {
        QuantityWeight result =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(2.0, WeightUnit.KILOGRAM));

        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_KgPlusGram() {
        QuantityWeight result =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(1000.0, WeightUnit.GRAM));

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_PoundPlusKg() {
        QuantityWeight result =
                new QuantityWeight(2.20462, WeightUnit.POUND)
                        .add(new QuantityWeight(1.0, WeightUnit.KILOGRAM));

        assertEquals(4.40924, result.getValue(), 1e-3);
    }

    @Test
    void testAddition_TargetUnit_Gram() {
        QuantityWeight result =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM);

        assertEquals(2000.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_TargetUnit_Kg() {
        QuantityWeight result =
                new QuantityWeight(2.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(4.0, WeightUnit.POUND), WeightUnit.KILOGRAM);

        assertEquals(3.814, result.getValue(), 1e-3);
    }

    // 🔹 Edge Cases

    @Test
    void testZero() {
        QuantityWeight result =
                new QuantityWeight(5.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(0.0, WeightUnit.GRAM));

        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    void testNegative() {
        QuantityWeight result =
                new QuantityWeight(5.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(-2000.0, WeightUnit.GRAM));

        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityWeight(Double.NaN, WeightUnit.KILOGRAM));
    }
}