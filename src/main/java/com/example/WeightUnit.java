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
    @Override
    public String getMeasurementType(){
        return "WEIGHT";
    }
    @Override
    public IMeasurable getUnitInstance(String unitName){
        return WeightUnit.valueOf(unitName);
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