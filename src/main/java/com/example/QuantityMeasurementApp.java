package com.example;

public class QuantityMeasurementApp {


    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value) || source == null || target == null) {
            throw new IllegalArgumentException();
        }

        double base = source.convertToBaseUnit(value);
        return target.convertFromBaseUnit(base);
    }

    public static void main(String[] args) {

        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        System.out.println(q1.add(q2)); // 2 FEET
        System.out.println(q2.add(q1)); // 24 INCH

        Quantity q3 = new Quantity(1.0, LengthUnit.YARDS);
        Quantity q4 = new Quantity(3.0, LengthUnit.FEET);

        System.out.println(q3.add(q4)); // 2 YARDS
    }
}