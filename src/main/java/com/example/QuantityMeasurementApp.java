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
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be finite");
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

        public Quantity convertTo(LengthUnit targetUnit) {
            double convertedValue = QuantityMeasurementApp.convert(this.value, this.unit, targetUnit);
            return new Quantity(convertedValue, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Quantity other = (Quantity) obj;
            return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < 1e-4;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        if (source == null || target == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        double valueInFeet = source.toFeet(value);
        return valueInFeet / target.toFeet(1.0);
    }

    public static void main(String[] args) {

        System.out.println("convert(1.0, FEET, INCH) = " +
                convert(1.0, LengthUnit.FEET, LengthUnit.INCH));

        System.out.println("convert(3.0, YARDS, FEET) = " +
                convert(3.0, LengthUnit.YARDS, LengthUnit.FEET));

        System.out.println("convert(36.0, INCH, YARDS) = " +
                convert(36.0, LengthUnit.INCH, LengthUnit.YARDS));

        System.out.println("convert(1.0, CENTIMETERS, INCH) = " +
                convert(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH));

        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        System.out.println(q1 + " equals " + q2 + " → " + q1.equals(q2));

        Quantity converted = q1.convertTo(LengthUnit.INCH);
        System.out.println(q1 + " in INCH → " + converted);
    }
}