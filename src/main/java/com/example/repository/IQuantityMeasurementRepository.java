package com.example.repository;

import com.example.entity.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);
    List<QuantityMeasurementEntity> getAllMeasurements();

    // UC16 New Methods
    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation);
    List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType);
    int getTotalCount();
    void deleteAll();

    default String getPoolStatistics() {
        return "Not available for this repository";
    }

    default void releaseResources() {
        // No-op for some implementations
    }
}

