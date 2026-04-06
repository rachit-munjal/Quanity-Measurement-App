package com.example;

public enum LengthUnit implements IMeasurable {
    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    METERS(39.3701),
    CENTIMETERS(0.393701);
    private final double conversionFactor;
    LengthUnit(double conversionFactor){
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
    @Override
    public String getMeasurementType(){
        return "LENGTH";
    }
    @Override
    public IMeasurable getUnitInstance(String unitName){
        return LengthUnit.valueOf(unitName);
    }
    @Override
    public double fromBase(double resultBase) {
        return convertFromBaseUnit(resultBase);
    }
    @Override
    public double toBase(double value) {
        return convertToBaseUnit(value);
    }
}