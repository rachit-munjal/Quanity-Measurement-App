package com.example;
public class QuantityMeasurementApp {

    public static class Feet {

        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (getClass() != obj.getClass()) {
                return false;
            }

            Feet other = (Feet) obj;

            return Double.compare(this.value, other.value) == 0;
        }
    }
    public static class Inch{
        private final double value;

        public Inch(double value){
            this.value = value;
        }

        public double getValue(){
            return value;
        }

        public boolean equals(Object obj){
            if(this == obj){
                return true;
            }
            if(obj == null){
                return false;
            }
            if(obj.getClass() != this.getClass()){
                return false;
            }

            Inch other = (Inch) obj;

            return Double.compare(this.value, other.value) == 0;
        }
    }

    public static boolean compareFeet(double v1, double v2) {
        Feet f1 = new Feet(v1);
        Feet f2 = new Feet(v2);
        return f1.equals(f2);
    }

    public static boolean compareInch(double v1, double v2) {
        Inch i1 = new Inch(v1);
        Inch i2 = new Inch(v2);
        return i1.equals(i2);
    }

    public static void main(String[] args) {

        boolean feetResult = compareFeet(1.0, 1.0);
        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + feetResult + ")");

        boolean inchResult = compareInch(2.0, 1.0);
        System.out.println("Input: 1.0 inch and 1.0 inch");
        System.out.println("Output: Equal (" + inchResult + ")");
    }
}