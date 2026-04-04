package com.example;

import java.util.*;
import java.util.function.*;

public class Quantity<U extends  IMeasurable> {
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
        validateArithmeticOperands(other,targetUnit,true);
        double resultBase=performBaseArithmetic(other,ArithmeticOperation.ADD);
        double result=targetUnit.convertFromBaseUnit(resultBase);
        return new Quantity<>(result,targetUnit);
    }
    public Quantity<U> subtract(Quantity<U> other){
        return subtract(other,this.unit);
    }
    public Quantity<U> subtract(Quantity<U> other,U targetUnit){
        validateArithmeticOperands(other,targetUnit,true);
        double resultBase=performBaseArithmetic(other,ArithmeticOperation.SUBTRACT);
        double result=targetUnit.convertFromBaseUnit(resultBase);
        return new Quantity<>(result,targetUnit);
    }
    public double divide(Quantity<U> other){
        validateArithmeticOperands(other,null,false);
        return performBaseArithmetic(other,ArithmeticOperation.DIVIDE);
    }
    @Override
    public boolean equals(Object obj){
        if(this==obj) return true;
        if(!(obj instanceof Quantity<?> other)) return false;
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
    private enum ArithmeticOperation{
        ADD((a,b)->a+b),
        SUBTRACT((a,b)->a-b),
        DIVIDE((a,b)->{
            if(b==0) throw new ArithmeticException("Division by zero");
            return a/b;
        });
        private final DoubleBinaryOperator operation;
        ArithmeticOperation(DoubleBinaryOperator operation){
            this.operation=operation;
        }
        public double compute(double a,double b){
            return operation.applyAsDouble(a,b);
        }
    }
    private void validateArithmeticOperands(Quantity<U> other,U targetUnit,boolean targetRequired){
        if(other==null) throw new IllegalArgumentException("Operand cannot be null");
        if(this.unit.getClass()!=other.unit.getClass()) throw new IllegalArgumentException("Incompatible unit types");
        if(!Double.isFinite(this.value) || !Double.isFinite(other.value)) throw new IllegalArgumentException("Values must be finite");
        if(targetRequired && targetUnit==null) throw new IllegalArgumentException("Target unit cannot be null");
    }
    private double performBaseArithmetic(Quantity<U> other,ArithmeticOperation operation){
        double base1=unit.convertToBaseUnit(value);
        double base2=other.unit.convertToBaseUnit(other.value);
        return operation.compute(base1,base2);
    }
}