package com.example;
import java.util.Objects;

public class Quantity<U extends  IMeasurable>{
    private double value;
    private U unit;
    public Quantity(double value,U unit){
        if(unit==null) throw new IllegalArgumentException("Unit cannot be null");
        if(Double.isNaN(value) || Double.isInfinite(value)) throw new IllegalArgumentException("Invalid value");
        this.value=value;
        this.unit=unit;
    }
    public double getValue(){
        return value;
    }
    public  U getUnit(){
        return unit;
    }
    public Quantity<U> convertTo(U targetUnit) {
        if(targetUnit==null) throw new IllegalArgumentException("Target unit cannot be null");
        double baseValue=unit.convertToBaseUnit(value);
        double convertedValue=targetUnit.convertFromBaseUnit(baseValue);
        return new Quantity<>(convertedValue,targetUnit);
    }
    public Quantity<U> add(Quantity<U> other){
        return add(other,this.unit);
    }
    public Quantity<U> add(Quantity<U> other,U targetUnit){
        if(this.unit.getClass()!=other.unit.getClass()) throw new IllegalArgumentException("Different measurement categories not allowed");
        double base1=this.unit.convertToBaseUnit(this.value);
        double base2=other.unit.convertToBaseUnit(other.value);
        double sum=base1+base2;
        double result=targetUnit.convertFromBaseUnit(sum);
        return new Quantity<>(result,targetUnit);
    }
    @Override
    public boolean equals(Object obj){
        if(this==obj) return true;
        if (!(obj instanceof Quantity<?>)) return false;
        Quantity<?> other = (Quantity<?>) obj;
        if(this.unit.getClass()!=other.unit.getClass()) return false;
        double base1=this.unit.convertToBaseUnit(this.value);
        double base2=((IMeasurable)other.unit).convertToBaseUnit(other.value);
        return Math.abs(base1-base2)<0.0001;
    }
    @Override
    public String toString(){
        return "Quantity("+value+", "+unit.getUnitName()+")";
    }
    @Override
    public int hashCode(){
        double base=unit.convertToBaseUnit(value);
        return Objects.hash(base,unit.getClass());
    }
}