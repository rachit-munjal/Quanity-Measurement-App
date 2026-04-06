package com.example;

import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable{
    CELSIUS(0),
    FAHRENHEIT(1),
    KELVIN(2);
    final Function<Double,Double> FAHRENHEIT_TO_CELSIUS =f->(f-32)*5/9;
    final Function<Double,Double> CELSIUS_TO_CELSIUS =c->c;
    final Function<Double,Double> CELSIUS_TO_FAHRENHEIT =c->(c*9/5)+32;
    final Function<Double,Double> KELVIN_TO_CELSIUS =k->k-273.15;
    final Function<Double,Double> CELSIUS_TO_KELVIN =c->c+273.15;
    private Function<Double,Double> conversionValue;
    SupportsArithmetic supportsArithmetic=()->false;
    TemperatureUnit(int type){
        if(type==1) this.conversionValue = FAHRENHEIT_TO_CELSIUS;
        else if(type==2) this.conversionValue = KELVIN_TO_CELSIUS;
        else this.conversionValue=CELSIUS_TO_CELSIUS;
    }
    @Override
    public double getConversionFactor(){
        return 1.0;
    }
    @Override
    public double convertToBaseUnit(double value){
        return conversionValue.apply(value);
    }
    @Override
    public double convertFromBaseUnit(double baseValue){
        if(this==CELSIUS) return baseValue;
        else if(this==FAHRENHEIT) return CELSIUS_TO_FAHRENHEIT.apply(baseValue);
        else if(this==KELVIN) return CELSIUS_TO_KELVIN.apply(baseValue);
        throw new IllegalStateException("Unsupported unit");
    }
    @Override
    public String getUnitName(){
        return name();
    }
    @Override
    public boolean supportsArithmetic(){
        return supportsArithmetic.isSupported();
    }
    @Override
    public void validateOperationSupport(String operation){
        if (!supportsArithmetic.isSupported()){
            throw new UnsupportedOperationException(name()+" does not support "+operation+" operations.");
        }
    }
    @Override
    public String getMeasurementType(){
        return "TEMPERATURE";
    }
    @Override
    public IMeasurable getUnitInstance(String unitName){
        return TemperatureUnit.valueOf(unitName);
    }
    @Override
    public double fromBase(double resultBase) {
        return convertFromBaseUnit(resultBase);
    }
    @Override
    public double toBase(double value) {
        return convertToBaseUnit(value);
    }
    @Override
    public String toString(){
        return name();
    }
}