package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class QuantityMeasurementAppTest {
    @Test
    void testValidation_NullOperand_ConsistentAcrossOperations(){
        Quantity<LengthUnit> q=new Quantity<>(10.0,LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class,()->q.add(null));
        assertThrows(IllegalArgumentException.class,()->q.subtract(null));
        assertThrows(IllegalArgumentException.class,()->q.divide(null));
    }
    @Test
    void testValidation_CrossCategory_ConsistentAcrossOperations(){
        Quantity<LengthUnit> length=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<WeightUnit> weight=new Quantity<>(5.0,WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class,()->length.add((Quantity) weight));
        assertThrows(IllegalArgumentException.class,()->length.subtract((Quantity) weight));
        assertThrows(IllegalArgumentException.class,()->length.divide((Quantity) weight));
    }
    @Test
    void testValidation_FiniteValue_ConsistentAcrossOperations(){
        assertThrows(IllegalArgumentException.class,()->new Quantity<>(Double.NaN,LengthUnit.FEET));
        assertThrows(IllegalArgumentException.class,()->new Quantity<>(Double.POSITIVE_INFINITY,LengthUnit.FEET));
    }
    @Test
    void testValidation_NullTargetUnit_AddSubtractReject(){
        Quantity<LengthUnit> q1=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit> q2=new Quantity<>(5.0,LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class,()->q1.add(q2,null));
        assertThrows(IllegalArgumentException.class,()->q1.subtract(q2,null));
    }
    @Test
    void testAdd_UC12_BehaviorPreserved(){
        Quantity<LengthUnit> result=new Quantity<>(1.0,LengthUnit.FEET).add(new Quantity<>(12.0,LengthUnit.INCHES));
        assertEquals(2.0,result.getValue(),0.001);
        assertEquals(LengthUnit.FEET,result.getUnit());
    }
    @Test
    void testSubtract_UC12_BehaviorPreserved(){
        Quantity<LengthUnit> result=new Quantity<>(10.0,LengthUnit.FEET).subtract(new Quantity<>(6.0,LengthUnit.INCHES));
        assertEquals(9.5,result.getValue(),0.001);
    }
    @Test
    void testDivide_UC12_BehaviorPreserved(){
        double result=new Quantity<>(24.0,LengthUnit.INCHES).divide(new Quantity<>(2.0,LengthUnit.FEET));
        assertEquals(1.0,result,0.001);
    }
    @Test
    void testArithmeticOperation_Add_EnumComputation(){
        double result=invokeEnum("ADD",10,5);
        assertEquals(15.0,result);
    }
    @Test
    void testArithmeticOperation_Subtract_EnumComputation(){
        double result = invokeEnum("SUBTRACT",10,5);
        assertEquals(5.0,result);
    }
    @Test
    void testArithmeticOperation_Divide_EnumComputation(){
        double result=invokeEnum("DIVIDE",10,5);
        assertEquals(2.0,result);
    }
    @Test
    void testArithmeticOperation_DivideByZero_EnumThrows(){
        assertThrows(ArithmeticException.class,()->invokeEnum("DIVIDE",10,0));
    }
    @Test
    void testImplicitTargetUnit_AddSubtract(){
        Quantity<LengthUnit> result=new Quantity<>(1.0,LengthUnit.FEET).add(new Quantity<>(12.0,LengthUnit.INCHES));
        assertEquals(LengthUnit.FEET,result.getUnit());
    }
    @Test
    void testExplicitTargetUnit_AddSubtract_Overrides(){
        Quantity<LengthUnit> result=new Quantity<>(1.0,LengthUnit.FEET).add(new Quantity<>(12.0,LengthUnit.INCHES),LengthUnit.INCHES);
        assertEquals(24.0,result.getValue(),0.001);
        assertEquals(LengthUnit.INCHES,result.getUnit());
    }
    @Test
    void testImmutability_AfterAdd_ViaCentralizedHelper(){
        Quantity<LengthUnit> q1=new Quantity<>(1.0,LengthUnit.FEET);
        Quantity<LengthUnit> q2=new Quantity<>(12.0,LengthUnit.INCHES);
        q1.add(q2);
        assertEquals(1.0,q1.getValue());
        assertEquals(12.0,q2.getValue());
    }
    @Test
    void testAllOperations_AcrossAllCategories(){
        assertNotNull(new Quantity<>(1.0,LengthUnit.FEET).add(new Quantity<>(12.0,LengthUnit.INCHES)));
        assertNotNull(new Quantity<>(10.0,WeightUnit.KILOGRAM).add(new Quantity<>(5000.0, WeightUnit.GRAM)));
        assertNotNull(new Quantity<>(5.0,VolumeUnit.LITRE).subtract(new Quantity<>(2.0,VolumeUnit.LITRE)));
    }
    @Test
    void testArithmetic_Chain_Operations(){
        double result = new Quantity<>(10.0,LengthUnit.FEET).add(new Quantity<>(12.0,LengthUnit.INCHES)).subtract(new Quantity<>(6.0,LengthUnit.INCHES)).divide(new Quantity<>(2.0,LengthUnit.FEET));
        assertEquals(5.25,result,0.001);
    }
    private double invokeEnum(String op,double a,double b){
        try{
            Class<?> clazz=Class.forName("org.example.UC13.Quantity$ArithmeticOperation");
            Object enumVal=Enum.valueOf((Class<Enum>) clazz, op);
            var method=clazz.getDeclaredMethod("compute",double.class,double.class);
            method.setAccessible(true);
            return (double) method.invoke(enumVal,a,b);
        }catch (java.lang.reflect.InvocationTargetException e){
            throw (RuntimeException) e.getCause();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
