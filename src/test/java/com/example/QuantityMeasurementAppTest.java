package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {
    private static final double EPSILON=0.01;
    @Test
    void testEquality_LitreToLitre_SameValue(){
        assertTrue(new Quantity<>(1.0,VolumeUnit.LITRE).equals(new Quantity<>(1.0,VolumeUnit.LITRE)));
    }
    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        assertFalse(new Quantity<>(1.0,VolumeUnit.LITRE).equals(new Quantity<>(2.0,VolumeUnit.LITRE)));
    }
    @Test
    void testEquality_LitreToMillilitre_EquivalentValue(){
        assertTrue(new Quantity<>(1.0,VolumeUnit.LITRE).equals(new Quantity<>(1000.0,VolumeUnit.MILLILITRE)));
    }
    @Test
    void testEquality_MillilitreToLitre_EquivalentValue(){
        assertTrue(new Quantity<>(1000.0,VolumeUnit.MILLILITRE).equals(new Quantity<>(1.0,VolumeUnit.LITRE)));
    }
    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(0.264172,VolumeUnit.GALLON)));
    }
    @Test
    void testEquality_GallonToLitre_EquivalentValue(){
        assertTrue(new Quantity<>(1.0,VolumeUnit.GALLON).equals(new Quantity<>(3.78541,VolumeUnit.LITRE)));
    }
    @Test
    void testEquality_VolumeVsLength_Incompatible(){
        assertFalse(new Quantity<>(1.0,VolumeUnit.LITRE).equals(new Quantity<>(1.0,LengthUnit.FEET)));
    }
    @Test
    void testEquality_VolumeVsWeight_Incompatible(){
        assertFalse(new Quantity<>(1.0,VolumeUnit.LITRE).equals(new Quantity<>(1.0,WeightUnit.KILOGRAM)));
    }
    @Test
    void testEquality_NullComparison(){
        assertFalse(new Quantity<>(1.0,VolumeUnit.LITRE).equals(null));
    }
    @Test
    void testEquality_SameReference(){
        Quantity<VolumeUnit> v=new Quantity<>(1.0,VolumeUnit.LITRE);
        assertTrue(v.equals(v));
    }
    @Test
    void testEquality_TransitiveProperty(){
        Quantity<VolumeUnit> a=new Quantity<>(1.0,VolumeUnit.LITRE);
        Quantity<VolumeUnit> b=new Quantity<>(1000.0,VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> c=new Quantity<>(1.0,VolumeUnit.LITRE);
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }
    @Test
    void testEquality_ZeroValue(){
        assertTrue(new Quantity<>(0.0,VolumeUnit.LITRE).equals(new Quantity<>(0.0,VolumeUnit.MILLILITRE)));
    }
    @Test
    void testEquality_NegativeVolume(){
        assertTrue(new Quantity<>(-1.0,VolumeUnit.LITRE).equals(new Quantity<>(-1000.0,VolumeUnit.MILLILITRE)));
    }
    @Test
    void testEquality_LargeVolumeValue(){
        assertTrue(new Quantity<>(1000000.0,VolumeUnit.MILLILITRE).equals(new Quantity<>(1000.0,VolumeUnit.LITRE)));
    }
    @Test
    void testEquality_SmallVolumeValue(){
        assertTrue(new Quantity<>(0.001, VolumeUnit.LITRE).equals(new Quantity<>(1.0,VolumeUnit.MILLILITRE)));
    }
    @Test
    void testConversion_LitreToMillilitre(){
        assertEquals(1000.0,new Quantity<>(1.0,VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).getValue());
    }
    @Test
    void testConversion_MillilitreToLitre(){
        assertEquals(1.0,new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE).getValue());
    }
    @Test
    void testConversion_GallonToLitre(){
        assertEquals(3.78541,new Quantity<>(1.0,VolumeUnit.GALLON).convertTo(VolumeUnit.LITRE).getValue(),EPSILON);
    }
    @Test
    void testConversion_LitreToGallon(){
        assertEquals(1.0,new Quantity<>(3.78541,VolumeUnit.LITRE).convertTo(VolumeUnit.GALLON).getValue(),EPSILON);
    }
    @Test
    void testConversion_MillilitreToGallon(){
        assertEquals(0.264172,new Quantity<>(1000.0,VolumeUnit.MILLILITRE).convertTo(VolumeUnit.GALLON).getValue(),EPSILON);
    }
    @Test
    void testConversion_SameUnit(){
        assertEquals(5.0,new Quantity<>(5.0,VolumeUnit.LITRE).convertTo(VolumeUnit.LITRE).getValue());
    }
    @Test
    void testConversion_RoundTrip(){
        double value=new Quantity<>(1.5,VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE).getValue();
        assertEquals(1.5,value,EPSILON);
    }
    @Test
    void testAddition_SameUnit_LitrePlusLitre(){
        assertEquals(3.0,new Quantity<>(1.0,VolumeUnit.LITRE).add(new Quantity<>(2.0,VolumeUnit.LITRE)).getValue());
    }
    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre(){
        assertEquals(2.0,new Quantity<>(1.0,VolumeUnit.LITRE).add(new Quantity<>(1000.0,VolumeUnit.MILLILITRE)).getValue());
    }
    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre(){
        assertEquals(2000.0,new Quantity<>(1000.0,VolumeUnit.MILLILITRE).add(new Quantity<>(1.0,VolumeUnit.LITRE)).getValue());
    }
    @Test
    void testAddition_ExplicitTargetUnit_Gallon(){
        assertEquals(2.0,new Quantity<>(3.78541,VolumeUnit.LITRE).add(new Quantity<>(3.78541,VolumeUnit.LITRE),VolumeUnit.GALLON).getValue(),EPSILON);
    }
    @Test
    void testAddition_Commutativity(){
        Quantity<VolumeUnit> a=new Quantity<>(1.0,VolumeUnit.LITRE).add(new Quantity<>(1000.0,VolumeUnit.MILLILITRE));
        Quantity<VolumeUnit> b=new Quantity<>(1000.0,VolumeUnit.MILLILITRE).add(new Quantity<>(1.0,VolumeUnit.LITRE));
        assertEquals(a.convertTo(VolumeUnit.LITRE).getValue(),b.convertTo(VolumeUnit.LITRE).getValue());
    }
    @Test
    void testVolumeUnitEnum_LitreConstant(){
        assertEquals(1.0,VolumeUnit.LITRE.getConversionFactor());
    }
    @Test
    void testVolumeUnitEnum_MillilitreConstant(){
        assertEquals(0.001,VolumeUnit.MILLILITRE.getConversionFactor());
    }
    @Test
    void testVolumeUnitEnum_GallonConstant(){
        assertEquals(3.78541,VolumeUnit.GALLON.getConversionFactor());
    }
    @Test
    void testConvertToBaseUnit(){
        assertEquals(1.0,VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0));
    }
    @Test
    void testConvertFromBaseUnit(){
        assertEquals(1000.0,VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0));
    }
    @Test
    void testGenericQuantity_VolumeOperations_Consistency(){
        Quantity<VolumeUnit> v=new Quantity<>(1.0, VolumeUnit.LITRE);
        assertNotNull(v.convertTo(VolumeUnit.MILLILITRE));
    }
    @Test
    void testScalability_VolumeIntegration(){
        assertTrue(true);
    }
}
