package com.example;
public class QuantityMeasurementApp {

    public enum LengthUnit {

        FEET(1.0),
        INCH(1.0 / 12.0);

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double toFeet(double value) {
            return value * conversionFactor;
        }
    }

    public static class Quantity {

        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {

            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }

            this.value = value;
            this.unit = unit;
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        // Convert to base unit (feet)
        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {

            // Same reference
            if (this == obj) return true;

            // Null or type check
            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            // Compare after conversion
            return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
        }
    }

    public static void main(String[] args) {

        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        System.out.println("Input: Quantity(1.0, feet) and Quantity(12.0, inch)");
        System.out.println("Output: Equal (" + q1.equals(q2) + ")");

        Quantity q3 = new Quantity(1.0, LengthUnit.INCH);
        Quantity q4 = new Quantity(1.0, LengthUnit.INCH);

        System.out.println("Input: Quantity(1.0, inch) and Quantity(1.0, inch)");
        System.out.println("Output: Equal (" + q3.equals(q4) + ")");
    }
}