package com.example;
public class QuantityMeasurementApp {

    public enum LengthUnit {

        FEET(1.0),
        INCH(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.0328084);

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

        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < 0.0001;
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

        Quantity q5 = new Quantity(1.0, LengthUnit.YARDS);
        Quantity q6 = new Quantity(91.44, LengthUnit.CENTIMETERS);

        System.out.println("Input: Quantity(1.0, yards) and Quantity(91.44, centimeters)");
        System.out.println("Output: Equal (" + q5.equals(q6) + ")");

        Quantity q7 = new Quantity(1.0, LengthUnit.CENTIMETERS);
        Quantity q8 = new Quantity(1.0, LengthUnit.CENTIMETERS);

        System.out.println("Input: Quantity(1.0, centimeters) and Quantity(1.0, centimeters)");
        System.out.println("Output: Equal (" + q7.equals(q8) + ")");

    }
}