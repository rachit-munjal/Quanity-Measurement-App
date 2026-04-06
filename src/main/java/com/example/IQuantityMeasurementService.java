package com.example;

public interface IQuantityMeasurementService {
    boolean compare(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO convert(QuantityDTO q1, QuantityDTO targetDTO);
    QuantityDTO add(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO add(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetDTO);
    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetDTO);
    double divide(QuantityDTO q1, QuantityDTO q2);
}
