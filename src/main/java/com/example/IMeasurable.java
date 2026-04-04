package com.example;

@FunctionalInterface
interface SupportsArithmetic{
    boolean isSupported();
}
public interface IMeasurable {
    public double getConversionFactor();
    public double convertToBaseUnit(double value);
    public double convertFromBaseUnit(double baseValue);
    public String getUnitName();
    default boolean supportsArithmetic(){
        return true;
    }
    default void validateOperationSupport(String operation){}
}