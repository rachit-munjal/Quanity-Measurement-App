package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    @Test
    public void testQuantityEntity_SingleOperandConstruction() {
        QuantityModel<LengthUnit> q1 = new QuantityModel<>(10, LengthUnit.FEET);
        QuantityModel<LengthUnit> result = new QuantityModel<>(120, LengthUnit.INCHES);
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(q1, "CONVERT", result);

        assertEquals(10.0, entity.thisValue, 0.0);
        assertEquals("FEET", entity.thisUnit);
        assertEquals(120.0, entity.resultValue, 0.0);
        assertEquals("INCHES", entity.resultUnit);
        assertFalse(entity.isError);
    }

    @Test
    public void testQuantityEntity_BinaryOperandConstruction() {
        QuantityModel<LengthUnit> q1 = new QuantityModel<>(1, LengthUnit.FEET);
        QuantityModel<LengthUnit> q2 = new QuantityModel<>(12, LengthUnit.INCHES);
        QuantityModel<LengthUnit> result = new QuantityModel<>(24, LengthUnit.INCHES);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(q1, q2, "ADD", result);

        assertEquals(1.0, entity.thisValue, 0.0);
        assertEquals(12.0, entity.thatValue, 0.0);
        assertEquals("ADD", entity.operation);
        assertEquals(24.0, entity.resultValue, 0.0);
    }

    @Test
    public void testQuantityEntity_ErrorConstruction() {
        QuantityModel<TemperatureUnit> t1 = new QuantityModel<>(0, TemperatureUnit.CELSIUS);
        QuantityModel<TemperatureUnit> t2 = new QuantityModel<>(32, TemperatureUnit.FAHRENHEIT);

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(t1, t2, "ADD",
                        "Temperature does not support ADD", true);

        assertTrue(entity.isError);
        assertEquals("Temperature does not support ADD", entity.errorMessage);
    }

    @Test
    public void testService_CompareEquality_SameUnit_Success() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);

        QuantityDTO q1 = new QuantityDTO(10, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(10, QuantityDTO.LengthUnit.FEET);

        assertTrue(service.compare(q1, q2));
    }

    @Test
    public void testService_CompareEquality_DifferentUnit_Success() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);

        QuantityDTO q1 = new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES);

        assertTrue(service.compare(q1, q2));
    }

    @Test
    public void testService_CompareEquality_CrossCategory_Error() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);

        QuantityDTO length = new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);
        QuantityDTO weight = new QuantityDTO(1, QuantityDTO.WeightUnit.KILOGRAM);

        assertThrows(QuantityMeasurementException.class, () -> {
            service.compare(length, weight);
        });
    }

    @Test
    public void testService_Convert_Success() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);

        QuantityDTO q1 = new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.LengthUnit.INCHES);

        QuantityDTO result = service.convert(q1, target);

        assertEquals(12.0, result.getValue(), 0.0);
        assertEquals("INCHES", result.getUnit());
    }

    @Test
    public void testService_Add_Success() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);

        QuantityDTO q1 = new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.LengthUnit.INCHES);

        QuantityDTO result = service.add(q1, q2, target);

        assertEquals(24.0, result.getValue(), 0.1);
    }

    @Test
    public void testService_Add_UnsupportedOperation_Error() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);

        QuantityDTO t1 = new QuantityDTO(0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO t2 = new QuantityDTO(32, QuantityDTO.TemperatureUnit.FAHRENHEIT);

        assertThrows(QuantityMeasurementException.class, () -> {
            service.add(t1, t2);
        });
    }

    @Test
    public void testService_Divide_Success() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);

        QuantityDTO q1 = new QuantityDTO(10, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(5, QuantityDTO.LengthUnit.FEET);

        double result = service.divide(q1, q2);

        assertEquals(2.0, result, 0.0);
    }

    @Test
    public void testController_PerformComparison_Success() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);
        QuantityMeasurementController controller = new QuantityMeasurementController(service);

        QuantityDTO q1 = new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES);

        assertTrue(controller.performComparison(q1, q2));
    }

    @Test
    public void testLayerSeparation_ServiceIndependence() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);

        QuantityDTO q1 = new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.LengthUnit.FEET);

        QuantityDTO result = service.convert(q1, target);

        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test
    public void testIntegration_EndToEnd_LengthAddition() {
        QuantityMeasurementApp app = QuantityMeasurementApp.getInstance();

        QuantityDTO q1 = new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.LengthUnit.FEET);

        QuantityDTO result = app.controller.performAddition(q1, q2, target);

        assertEquals(2.0, result.getValue(), 0.1);
    }
}