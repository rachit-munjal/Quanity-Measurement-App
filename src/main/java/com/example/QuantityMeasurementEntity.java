package com.example;

import java.io.Serializable;
import java.util.Objects;

/**
 * QuantityMeasurementEntity - stores complete operation details
 */
public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // INPUT 1
    public double thisValue;
    public String thisUnit;
    public String thisMeasurementType;

    // INPUT 2
    public double thatValue;
    public String thatUnit;
    public String thatMeasurementType;

    // OPERATION
    public String operation;

    // RESULT (numeric)
    public double resultValue;
    public String resultUnit;
    public String resultMeasurementType;

    // RESULT (string - for comparison)
    public String resultString;

    // ERROR
    public boolean isError;
    public String errorMessage;

    // ================= COMMON INITIALIZER =================
    private void initCommon(QuantityModel<?> thisQ,
                            QuantityModel<?> thatQ,
                            String operation) {

        if (thisQ != null) {
            this.thisValue = thisQ.getValue();
            this.thisUnit = thisQ.getUnit().toString();
            this.thisMeasurementType = thisQ.getUnit().getMeasurementType();
        }

        if (thatQ != null) {
            this.thatValue = thatQ.getValue();
            this.thatUnit = thatQ.getUnit().toString();
            this.thatMeasurementType = thatQ.getUnit().getMeasurementType();
        }

        this.operation = operation;
        this.isError = false;
    }

    // ================= CONSTRUCTORS =================

    // 1. Comparison (Equal / Not Equal)
    public QuantityMeasurementEntity(QuantityModel<?> thisQ,
                                     QuantityModel<?> thatQ,
                                     String operation,
                                     String resultString) {
        initCommon(thisQ, thatQ, operation);
        this.resultString = resultString;
    }

    // 2. Arithmetic (ADD, SUBTRACT)
    public QuantityMeasurementEntity(QuantityModel<?> thisQ,
                                     QuantityModel<?> thatQ,
                                     String operation,
                                     QuantityModel<?> result) {
        initCommon(thisQ, thatQ, operation);

        this.resultValue = result.getValue();
        this.resultUnit = result.getUnit().toString();
        this.resultMeasurementType = result.getUnit().getMeasurementType();
    }

    // 3. Conversion (single operand)
    public QuantityMeasurementEntity(QuantityModel<?> thisQ,
                                     String operation,
                                     QuantityModel<?> result) {
        initCommon(thisQ, null, operation);

        this.resultValue = result.getValue();
        this.resultUnit = result.getUnit().toString();
        this.resultMeasurementType = result.getUnit().getMeasurementType();
    }

    // 4. Error case
    public QuantityMeasurementEntity(QuantityModel<?> thisQ,
                                     QuantityModel<?> thatQ,
                                     String operation,
                                     String errorMessage,
                                     boolean isError) {
        initCommon(thisQ, thatQ, operation);
        this.errorMessage = errorMessage;
        this.isError = isError;
    }

    // ================= EQUALS =================
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityMeasurementEntity)) return false;

        QuantityMeasurementEntity other = (QuantityMeasurementEntity) obj;

        return Double.compare(thisValue, other.thisValue) == 0 &&
                Double.compare(thatValue, other.thatValue) == 0 &&
                Objects.equals(thisUnit, other.thisUnit) &&
                Objects.equals(thatUnit, other.thatUnit) &&
                Objects.equals(operation, other.operation);
    }

    // ================= toString =================
    @Override
    public String toString() {

        if (isError) {
            return "ERROR [" + operation + "]: " + errorMessage;
        }

        if (resultString != null) {
            return operation + " → Result: " + resultString;
        }

        return operation + " → Result: " +
                resultValue + " " + resultUnit;
    }

    // ================= TEST =================
    public static void main(String[] args) {

        QuantityModel<LengthUnit> q1 =
                new QuantityModel<>(10, LengthUnit.FEET);

        QuantityModel<LengthUnit> q2 =
                new QuantityModel<>(3, LengthUnit.METERS);

        QuantityModel<LengthUnit> result =
                new QuantityModel<>(13, LengthUnit.FEET);
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(q1, q2, "ADD", result);
        System.out.println(entity);
    }
}