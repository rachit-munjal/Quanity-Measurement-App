package com.example;

public enum WeightUnit implements IMeasurable{
    MILLIGRAM(0.001),
    GRAM(1.0),
    KILOGRAM(1000.0),
    POUND(453.592),
    TONNE(1_000_000.0);
    private final double conversionFactor;
    WeightUnit(double conversionFactor){
        this.conversionFactor=conversionFactor;
    }
    @Override
    public double getConversionFactor(){
        return conversionFactor;
    }
    @Override
    public double convertToBaseUnit(double value){
        return value*getConversionFactor();
    }
    @Override
    public double convertFromBaseUnit(double baseValue){
        return baseValue/getConversionFactor();
    }
    @Override
    public String getUnitName(){
        return this.name();
    }
}