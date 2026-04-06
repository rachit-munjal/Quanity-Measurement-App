package com.example.service;

import com.example.entity.QuantityDTO;
import com.example.entity.QuantityMeasurementEntity;
import com.example.entity.QuantityModel;
import com.example.exception.QuantityMeasurementException;
import com.example.repository.IQuantityMeasurementRepository;
import com.example.unit.*;

import java.util.logging.Logger;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = Logger.getLogger(QuantityMeasurementServiceImpl.class.getName());
    private IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
        logger.info("QuantityMeasurementServiceImpl init with repository: " + repository.getClass().getSimpleName());
    }

    @Override
    public boolean compare(QuantityDTO q1, QuantityDTO q2) {
        QuantityModel<IMeasurable> m1 = null;
        QuantityModel<IMeasurable> m2 = null;
        try {
            m1 = toModel(q1);
            m2 = toModel(q2);
            validateSameType(m1, m2);

            boolean result = Double.compare(m1.getUnit().toBase(m1.getValue()),
                    m2.getUnit().toBase(m2.getValue())) == 0;

            repository.save(new QuantityMeasurementEntity(m1, m2, "COMPARE",
                    result ? "Equal" : "Not Equal"));

            return result;
        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity(m1, m2, "COMPARE", e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage());
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO q1, QuantityDTO targetDTO) {
        QuantityModel<IMeasurable> m1 = null;
        QuantityModel<IMeasurable> target = null;
        try {
            m1 = toModel(q1);
            target = toModel(targetDTO);
            validateSameType(m1, target);

            double baseValue = m1.getUnit().toBase(m1.getValue());
            double resultValue = target.getUnit().fromBase(baseValue);

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(resultValue, target.getUnit());
            repository.save(new QuantityMeasurementEntity(m1, null, "CONVERT", resultModel));

            return new QuantityDTO(resultValue, q1.getUnit(), q1.getMeasurementType());
        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity(m1, null, "CONVERT", e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage());
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        return add(q1, q2, q1);
    }

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetDTO) {
        return performArithmetic(q1, q2, targetDTO, "ADD");
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        return subtract(q1, q2, q1);
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2, QuantityDTO targetDTO) {
        return performArithmetic(q1, q2, targetDTO, "SUBTRACT");
    }

    @Override
    public double divide(QuantityDTO q1, QuantityDTO q2) {
        QuantityModel<IMeasurable> m1 = null;
        QuantityModel<IMeasurable> m2 = null;
        try {
            m1 = toModel(q1);
            m2 = toModel(q2);
            validateSameType(m1, m2);

            double base1 = m1.getUnit().toBase(m1.getValue());
            double base2 = m2.getUnit().toBase(m2.getValue());

            if (base2 == 0) throw new ArithmeticException("Division by zero");

            double result = base1 / base2;
            repository.save(new QuantityMeasurementEntity(m1, m2, "DIVIDE", String.valueOf(result)));

            return result;
        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity(m1, m2, "DIVIDE", e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage());
        }
    }

    // ================= HELPERS =================

    private QuantityModel<IMeasurable> toModel(QuantityDTO dto) {
        if (dto == null) return null;
        IMeasurable unit;
        try {
            switch (dto.getMeasurementType()) {
                case "LENGTH": unit = LengthUnit.valueOf(dto.getUnit()); break;
                case "WEIGHT": unit = WeightUnit.valueOf(dto.getUnit()); break;
                case "VOLUME": unit = VolumeUnit.valueOf(dto.getUnit()); break;
                case "TEMPERATURE": unit = TemperatureUnit.valueOf(dto.getUnit()); break;
                default: throw new QuantityMeasurementException("Invalid Category: " + dto.getMeasurementType());
            }
            return new QuantityModel<>(dto.getValue(), unit);
        } catch (IllegalArgumentException e) {
            throw new QuantityMeasurementException("Invalid Unit: " + dto.getUnit());
        }
    }

    private void validateSameType(QuantityModel<IMeasurable> m1, QuantityModel<IMeasurable> m2) {
        if (m1 == null || m2 == null) return;
        if (!m1.getUnit().getMeasurementType().equals(m2.getUnit().getMeasurementType())) {
            throw new QuantityMeasurementException("Incompatible types: " +
                    m1.getUnit().getMeasurementType() + " vs " + m2.getUnit().getMeasurementType());
        }
    }

    private QuantityDTO performArithmetic(QuantityDTO q1,
                                          QuantityDTO q2,
                                          QuantityDTO targetDTO,
                                          String op) {
        QuantityModel<IMeasurable> m1 = null;
        QuantityModel<IMeasurable> m2 = null;
        QuantityModel<IMeasurable> target = null;
        try {
            m1 = toModel(q1);
            m2 = toModel(q2);
            target = toModel(targetDTO);

            validateSameType(m1, m2);
            validateSameType(m1, target);

            m1.getUnit().validateOperationSupport(op);
            m2.getUnit().validateOperationSupport(op);

            double v1 = m1.getUnit().toBase(m1.getValue());
            double v2 = m2.getUnit().toBase(m2.getValue());

            double resBase = op.equals("ADD") ? v1 + v2 : v1 - v2;
            double resValue = target.getUnit().fromBase(resBase);

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(resValue, target.getUnit());
            repository.save(new QuantityMeasurementEntity(m1, m2, op, resultModel));

            return new QuantityDTO(resValue, targetDTO.getUnit(), targetDTO.getMeasurementType());
        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity(m1, m2, op, e.getMessage(), true));
            throw new QuantityMeasurementException(e.getMessage());
        }
    }
}

