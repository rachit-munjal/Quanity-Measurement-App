package com.example;

import com.example.controller.QuantityMeasurementController;
import com.example.entity.QuantityDTO;
import com.example.entity.QuantityMeasurementEntity;
import com.example.exception.QuantityMeasurementException;
import com.example.repository.QuantityMeasurementCacheRepository;
import com.example.repository.IQuantityMeasurementRepository;
import com.example.service.IQuantityMeasurementService;
import com.example.service.QuantityMeasurementServiceImpl;

import java.util.List;

public class QuantityMeasurementApp {

    private static QuantityMeasurementApp instance;
    public QuantityMeasurementController controller;
    public IQuantityMeasurementRepository repository;

    private QuantityMeasurementApp() {
        this.repository = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(this.repository);
        this.controller = new QuantityMeasurementController(service);
    }

    public static QuantityMeasurementApp getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementApp();
        }
        return instance;
    }

    public static void main(String[] args) {
        QuantityMeasurementApp app = QuantityMeasurementApp.getInstance();

        // 1. Length Equality Demonstration
        System.out.println("\nExample 1: Length Equality Demonstration");
        QuantityDTO q1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        System.out.println("Compare 1.0 FEET and 12.0 INCHES: " + app.controller.performComparison(q1, q2));

        // 2. Temperature Addition Attempt
        System.out.println("\nExample 2: Temperature Addition Attempt");
        QuantityDTO t1 = new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO t2 = new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT);
        try {
            app.controller.performAddition(t1, t2);
        } catch (QuantityMeasurementException e) {
            System.out.println("Expected Error: " + e.getMessage());
        }

        // 3. Cross-Category Operation Prevention
        System.out.println("\nExample 3: Cross-Category Operation Prevention");
        QuantityDTO length = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO weight = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        try {
            app.controller.performComparison(length, weight);
        } catch (QuantityMeasurementException e) {
            System.out.println("Expected Error: " + e.getMessage());
        }

        // 4. Print Repository History
        System.out.println("\n--- Operation History from Repository ---");
        List<QuantityMeasurementEntity> history = app.repository.getAllMeasurements();
        for (QuantityMeasurementEntity entity : history) {
            System.out.println(entity);
        }
    }
}