package com.example.unit;

public enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);
    private final double conversionFactor;
    VolumeUnit(double conversionFactor){
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
        return "VOLUME";
    }
    @Override
    public IMeasurable getUnitInstance(String unitName){
        return VolumeUnit.valueOf(unitName);
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