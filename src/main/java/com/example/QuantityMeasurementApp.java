package com.example;

public class QuantityMeasurementApp {
    public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> quantity1,Quantity<U> quantity2){
        return quantity1.equals(quantity2);
    }
    public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> quantity,U targetUnit){
        return quantity.convertTo(targetUnit);
    }
    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> quantity1,Quantity<U> quantity2){
        return quantity1.add(quantity2);
    }
    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> quantity1,Quantity<U> quantity2,U targetUnit){
        return quantity1.add(quantity2,targetUnit);
    }
    public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> quantity1,Quantity<U> quantity2){
        return quantity1.subtract(quantity2);
    }
    public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> quantity1,Quantity<U> quantity2,U targetUnit){
        return quantity1.subtract(quantity2,targetUnit);
    }
    public static <U extends IMeasurable> double demonstrateDivision(Quantity<U> quantity1,Quantity<U> quantity2){
        return quantity1.divide(quantity2);
    }
    public static void main(String[] args) {
        System.out.println(demonstrateAddition(new Quantity<>(1.0,LengthUnit.FEET),new Quantity<>(12.0,LengthUnit.INCHES)));
        System.out.println(demonstrateAddition(new Quantity<>(10.0,WeightUnit.KILOGRAM),new Quantity<>(5000.0,WeightUnit.GRAM),WeightUnit.GRAM));
        System.out.println(demonstrateSubtraction(new Quantity<>(10.0,LengthUnit.FEET),new Quantity<>(6.0,LengthUnit.INCHES)));
        System.out.println(demonstrateSubtraction(new Quantity<>(5.0,VolumeUnit.LITRE),new Quantity<>(2.0,VolumeUnit.LITRE),VolumeUnit.MILLILITRE));
        System.out.println(demonstrateDivision(new Quantity<>(10.0,LengthUnit.FEET),new Quantity<>(2.0,LengthUnit.FEET)));
        System.out.println(demonstrateDivision(new Quantity<>(24.0,LengthUnit.INCHES),new Quantity<>(2.0,LengthUnit.FEET)));
        try{
            System.out.println(demonstrateAddition(new Quantity<>(10.0,LengthUnit.FEET),null));
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        try{
            System.out.println(demonstrateSubtraction(new Quantity<>(10.0,LengthUnit.FEET),new Quantity<>(5.0,WeightUnit.KILOGRAM)));
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        try{
            System.out.println(demonstrateDivision(new Quantity<>(10.0, LengthUnit.FEET),new Quantity<>(0.0,LengthUnit.FEET)));
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
