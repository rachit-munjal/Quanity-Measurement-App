package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class QuantityMeasurementAppTest {
    private static final double EPSILON=0.01;
    @Test
    void testTemperatureEquality_CelsiusToCelsius_SameValue(){
        assertTrue(new Quantity<>(0.0,TemperatureUnit.CELSIUS).equals(new Quantity<>(0.0,TemperatureUnit.CELSIUS)));
    }
    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_0Celsius32Fahrenheit(){
        assertTrue(new Quantity<>(0.0,TemperatureUnit.CELSIUS).equals(new Quantity<>(32.0,TemperatureUnit.FAHRENHEIT)));
    }
    @Test
    void testTemperatureEquality_CelsiusToKelvin(){
        assertTrue(new Quantity<>(0.0,TemperatureUnit.CELSIUS).equals(new Quantity<>(273.15,TemperatureUnit.KELVIN)));
    }
    @Test
    void testTemperatureEquality_Negative40Equal(){
        assertTrue(new Quantity<>(-40.0,TemperatureUnit.CELSIUS).equals(new Quantity<>(-40.0,TemperatureUnit.FAHRENHEIT)));
    }
    @Test
    void testTemperatureEquality_SymmetricProperty(){
        Quantity<TemperatureUnit> a=new Quantity<>(0.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b=new Quantity<>(32.0,TemperatureUnit.FAHRENHEIT);
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
    }
    @Test
    void testTemperatureConversion_CelsiusToFahrenheit(){
        Quantity<TemperatureUnit> result=new Quantity<>(100.0,TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(212.0,result.getValue(),EPSILON);
    }
    @Test
    void testTemperatureConversion_FahrenheitToCelsius(){
        Quantity<TemperatureUnit> result=new Quantity<>(32.0,TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.CELSIUS);
        assertEquals(0.0,result.getValue(),EPSILON);
    }

    @Test
    void testTemperatureConversion_CelsiusToKelvin(){
        Quantity<TemperatureUnit> result=new Quantity<>(0.0,TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.KELVIN);
        assertEquals(273.15,result.getValue(),EPSILON);
    }
    @Test
    void testTemperatureConversion_RoundTrip(){
        Quantity<TemperatureUnit> original=new Quantity<>(50.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> converted=original.convertTo(TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.CELSIUS);
        assertEquals(original.getValue(),converted.getValue(),EPSILON);
    }
    @Test
    void testTemperatureUnsupportedOperation_Add(){
        Quantity<TemperatureUnit> t1=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> t2=new Quantity<>(50.0,TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class,()->t1.add(t2));
    }
    @Test
    void testTemperatureUnsupportedOperation_Subtract(){
        Quantity<TemperatureUnit> t1=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> t2=new Quantity<>(50.0,TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class,()->t1.subtract(t2));
    }
    @Test
    void testTemperatureUnsupportedOperation_Divide(){
        Quantity<TemperatureUnit> t1=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> t2=new Quantity<>(50.0,TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class,()->t1.divide(t2));
    }
    @Test
    void testTemperatureVsLengthIncompatibility(){
        assertFalse(new Quantity<>(100.0,TemperatureUnit.CELSIUS).equals(new Quantity<>(100.0,LengthUnit.FEET)));
    }
    @Test
    void testTemperatureVsWeightIncompatibility(){
        assertFalse(new Quantity<>(50.0,TemperatureUnit.CELSIUS).equals(new Quantity<>(50.0,WeightUnit.KILOGRAM)));
    }
    @Test
    void testTemperatureVsVolumeIncompatibility(){
        assertFalse(new Quantity<>(25.0,TemperatureUnit.CELSIUS).equals(new Quantity<>(25.0,VolumeUnit.LITRE)));
    }
    @Test
    void testTemperatureAbsoluteZero(){
        Quantity<TemperatureUnit> result=new Quantity<>(-273.15,TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.KELVIN);
        assertEquals(0.0,result.getValue(),EPSILON);
    }
    @Test
    void testTemperatureEqualPointMinus40(){
        Quantity<TemperatureUnit> result=new Quantity<>(-40.0,TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(-40.0,result.getValue(),EPSILON);
    }
    @Test
    void testTemperatureNullUnitValidation(){
        assertThrows(IllegalArgumentException.class,()->new Quantity<>(100.0,null));
    }
    @Test
    void testTemperatureNullOperandValidation(){
        Quantity<TemperatureUnit> t=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        assertFalse(t.equals(null));
    }
}
