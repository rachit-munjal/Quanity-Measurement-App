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
    public static void main(String[] args){
        Quantity<LengthUnit> l1=new Quantity<>(1.0,LengthUnit.FEET);
        Quantity<LengthUnit> l2=new Quantity<>(12.0,LengthUnit.INCHES);
        System.out.println("Length Equality: "+demonstrateEquality(l1,l2));
        System.out.println("Length Conversion: "+demonstrateConversion(l1,LengthUnit.INCHES));
        System.out.println("Length Addition: "+demonstrateAddition(l1,l2,LengthUnit.FEET));
        Quantity<WeightUnit> w1=new Quantity<>(1.0,WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2=new Quantity<>(1000.0,WeightUnit.GRAM);
        System.out.println("Weight Equality: "+demonstrateEquality(w1,w2));
        System.out.println("Weight Conversion: "+demonstrateConversion(w1,WeightUnit.GRAM));
        System.out.println("Weight Addition: "+demonstrateAddition(w1,w2,WeightUnit.KILOGRAM));
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> v3 = new Quantity<>(1.0, VolumeUnit.GALLON);
        System.out.println("Volume Equality (L vs mL): "+demonstrateEquality(v1,v2));
        System.out.println("Volume Equality (L vs gallon approx): "+demonstrateEquality(v1,new Quantity<>(0.264172,VolumeUnit.GALLON)));
        System.out.println("Volume Conversion (L → mL): "+demonstrateConversion(v1,VolumeUnit.MILLILITRE));
        System.out.println("Volume Conversion (gallon → L): "+demonstrateConversion(v3,VolumeUnit.LITRE));
        System.out.println("Volume Addition (L + mL): "+demonstrateAddition(v1,v2,VolumeUnit.LITRE));
        System.out.println("Volume Addition (gallon + L): "+demonstrateAddition(v3,v1,VolumeUnit.GALLON));
    }
}