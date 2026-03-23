package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {
    private static final double EPSILON=0.0001;
    @Test
    void testSubtraction_SameUnit_FeetMinusFeet(){
        Quantity<LengthUnit> result=new Quantity<>(10.0,LengthUnit.FEET).subtract(new Quantity<>(5.0,LengthUnit.FEET));
        assertEquals(5.0,result.getValue(),EPSILON);
    }
    @Test
    void testSubtraction_SameUnit_LitreMinusLitre(){
        Quantity<VolumeUnit> result=new Quantity<>(10.0,VolumeUnit.LITRE).subtract(new Quantity<>(3.0,VolumeUnit.LITRE));
        assertEquals(7.0,result.getValue(),EPSILON);
    }
    @Test
    void testSubtraction_CrossUnit_FeetMinusInches(){
        Quantity<LengthUnit> result=new Quantity<>(10.0,LengthUnit.FEET).subtract(new Quantity<>(6.0,LengthUnit.INCHES));
        assertEquals(9.5,result.getValue(),EPSILON);
    }
    @Test
    void testSubtraction_ExplicitTargetUnit_Inches(){
        Quantity<LengthUnit> result=new Quantity<>(10.0,LengthUnit.FEET).subtract(new Quantity<>(6.0,LengthUnit.INCHES),LengthUnit.INCHES);
        assertEquals(114.0,result.getValue(),EPSILON);
    }
    @Test
    void testSubtraction_ResultingInNegative(){
        Quantity<LengthUnit> result=new Quantity<>(5.0,LengthUnit.FEET).subtract(new Quantity<>(10.0,LengthUnit.FEET));
        assertEquals(-5.0,result.getValue(),EPSILON);
    }
    @Test
    void testSubtraction_ResultingInZero(){
        Quantity<LengthUnit> result=new Quantity<>(10.0,LengthUnit.FEET).subtract(new Quantity<>(120.0,LengthUnit.INCHES));
        assertEquals(0.0,result.getValue(),EPSILON);
    }
    @Test
    void testSubtraction_WithZeroOperand(){
        Quantity<LengthUnit> result=new Quantity<>(5.0,LengthUnit.FEET).subtract(new Quantity<>(0.0,LengthUnit.INCHES));
        assertEquals(5.0,result.getValue(),EPSILON);
    }
    @Test
    void testSubtraction_NonCommutative(){
        double a=new Quantity<>(10.0,LengthUnit.FEET).subtract(new Quantity<>(5.0,LengthUnit.FEET)).getValue();
        double b=new Quantity<>(5.0,LengthUnit.FEET).subtract(new Quantity<>(10.0,LengthUnit.FEET)).getValue();
        assertNotEquals(a,b);
    }
    @Test
    void testSubtraction_NullOperand(){
        assertThrows(IllegalArgumentException.class,()->new Quantity<>(10.0,LengthUnit.FEET).subtract(null));
    }
    @Test
    void testSubtraction_CrossCategory(){
        Quantity<?> q1=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<?> q2=new Quantity<>(5.0,WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class,()->{
            ((Quantity) q1).subtract((Quantity) q2);
        });
    }
    @Test
    void testDivision_SameUnit_FeetDividedByFeet(){
        double result=new Quantity<>(10.0,LengthUnit.FEET).divide(new Quantity<>(2.0,LengthUnit.FEET));
        assertEquals(5.0,result,EPSILON);
    }
    @Test
    void testDivision_SameUnit_LitreDividedByLitre(){
        double result=new Quantity<>(10.0,VolumeUnit.LITRE).divide(new Quantity<>(5.0,VolumeUnit.LITRE));
        assertEquals(2.0,result,EPSILON);
    }
    @Test
    void testDivision_CrossUnit_FeetDividedByInches(){
        double result=new Quantity<>(24.0,LengthUnit.INCHES).divide(new Quantity<>(2.0,LengthUnit.FEET));
        assertEquals(1.0,result,EPSILON);
    }
    @Test
    void testDivision_CrossUnit_KilogramDividedByGram(){
        double result=new Quantity<>(2.0,WeightUnit.KILOGRAM).divide(new Quantity<>(2000.0,WeightUnit.GRAM));
        assertEquals(1.0,result,EPSILON);
    }
    @Test
    void testDivision_RatioLessThanOne(){
        double result=new Quantity<>(5.0,LengthUnit.FEET).divide(new Quantity<>(10.0,LengthUnit.FEET));
        assertEquals(0.5,result,EPSILON);
    }
    @Test
    void testDivision_RatioEqualToOne(){
        double result=new Quantity<>(10.0,LengthUnit.FEET).divide(new Quantity<>(10.0,LengthUnit.FEET));
        assertEquals(1.0,result,EPSILON);
    }
    @Test
    void testDivision_NonCommutative(){
        double a=new Quantity<>(10.0,LengthUnit.FEET).divide(new Quantity<>(5.0,LengthUnit.FEET));
        double b=new Quantity<>(5.0,LengthUnit.FEET).divide(new Quantity<>(10.0,LengthUnit.FEET));
        assertNotEquals(a,b);
    }
    @Test
    void testDivision_ByZero(){
        assertThrows(ArithmeticException.class,()->new Quantity<>(10.0,LengthUnit.FEET).divide(new Quantity<>(0.0,LengthUnit.FEET)));
    }
    @Test
    void testDivision_NullOperand(){
        assertThrows(IllegalArgumentException.class,()->new Quantity<>(10.0,LengthUnit.FEET).divide(null));
    }
    @Test
    void testDivision_CrossCategory(){
        Quantity<?> q1=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<?> q2=new Quantity<>(5.0,WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class,()->{
            ((Quantity) q1).divide((Quantity)q2);
        });
    }
    @Test
    void testSubtractionAndDivision_Integration(){
        double result=new Quantity<>(10.0,LengthUnit.FEET).subtract(new Quantity<>(2.0,LengthUnit.FEET)).divide(new Quantity<>(2.0,LengthUnit.FEET));
        assertEquals(4.0,result,EPSILON);
    }
    @Test
    void testSubtractionAddition_Inverse(){
        Quantity<LengthUnit> a=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit> b=new Quantity<>(5.0,LengthUnit.FEET);
        Quantity<LengthUnit> result=a.add(b).subtract(b);
        assertTrue(a.equals(result));
    }
    @Test
    void testSubtraction_Immutability(){
        Quantity<LengthUnit> a=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit> b=new Quantity<>(5.0,LengthUnit.FEET);
        a.subtract(b);
        assertEquals(10.0,a.getValue(),EPSILON);
    }
    @Test
    void testDivision_Immutability(){
        Quantity<LengthUnit> a=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit> b=new Quantity<>(2.0,LengthUnit.FEET);
        a.divide(b);
        assertEquals(10.0, a.getValue(),EPSILON);
    }
}