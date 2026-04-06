package com.example.repository;

import com.example.exception.DatabaseException;
import com.example.entity.QuantityMeasurementEntity;
import com.example.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = Logger.getLogger(QuantityMeasurementDatabaseRepository.class.getName());
    private static QuantityMeasurementDatabaseRepository instance;

    private static final String INSERT_QUERY =
            "INSERT INTO quantity_measurement_entity " +
                    "(this_value, this_unit, this_measurement_type, that_value, that_unit, " +
                    "that_measurement_type, operation, result_value, result_unit, " +
                    "result_measurement_type, result_string, is_error, error_message, " +
                    "created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

    private static final String SELECT_ALL_QUERY =
            "SELECT * FROM quantity_measurement_entity ORDER BY created_at DESC";

    private static final String SELECT_BY_OPERATION =
            "SELECT * FROM quantity_measurement_entity WHERE operation = ? ORDER BY created_at DESC";

    private static final String SELECT_BY_MEASUREMENT_TYPE =
            "SELECT * FROM quantity_measurement_entity " +
                    "WHERE this_measurement_type = ? ORDER BY created_at DESC";

    private static final String DELETE_ALL_QUERY =
            "DELETE FROM quantity_measurement_entity";

    private static final String COUNT_QUERY =
            "SELECT COUNT(*) FROM quantity_measurement_entity";

    private ConnectionPool connectionPool;

    private QuantityMeasurementDatabaseRepository() {
        try {
            this.connectionPool = ConnectionPool.getInstance();
            initializeDatabase();
        } catch (SQLException e) {
            throw DatabaseException.connectionFailed("Could not initialize connection pool", e);
        }
    }

    public static synchronized QuantityMeasurementDatabaseRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementDatabaseRepository();
        }
        return instance;
    }

    private void initializeDatabase() {
        // In a real app, this would be handled by Flyway/Liquibase
        // Here we just ensure the table exists (though schema.sql should have done it)
        try (Connection conn = connectionPool.getConnection();
             Statement stmt = conn.createStatement()) {

            // Read schema from classpath if needed, but for simplicity we'll assume it's created or we create a minimal one
            String createTable = "CREATE TABLE IF NOT EXISTS quantity_measurement_entity (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "this_value DOUBLE, this_unit VARCHAR(255), this_measurement_type VARCHAR(255), " +
                    "that_value DOUBLE, that_unit VARCHAR(255), that_measurement_type VARCHAR(255), " +
                    "operation VARCHAR(255), result_value DOUBLE, result_unit VARCHAR(255), " +
                    "result_measurement_type VARCHAR(255), result_string VARCHAR(255), " +
                    "is_error BOOLEAN, error_message VARCHAR(255), " +
                    "created_at TIMESTAMP, updated_at TIMESTAMP)";
            stmt.execute(createTable);
            logger.info("Database schema initialized successfully");
        } catch (SQLException e) {
            logger.warning("Database initialization warning: " + e.getMessage());
        }
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setDouble(1, entity.thisValue);
            pstmt.setString(2, entity.thisUnit);
            pstmt.setString(3, entity.thisMeasurementType);
            pstmt.setDouble(4, entity.thatValue);
            pstmt.setString(5, entity.thatUnit);
            pstmt.setString(6, entity.thatMeasurementType);
            pstmt.setString(7, entity.operation);
            pstmt.setDouble(8, entity.resultValue);
            pstmt.setString(9, entity.resultUnit);
            pstmt.setString(10, entity.resultMeasurementType);
            pstmt.setString(11, entity.resultString);
            pstmt.setBoolean(12, entity.isError);
            pstmt.setString(13, entity.errorMessage);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating measurement failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
            logger.info("Saved measurement to database: " + entity);
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(INSERT_QUERY, e);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return getMeasurements(SELECT_ALL_QUERY, null);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        return getMeasurements(SELECT_BY_OPERATION, operation);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return getMeasurements(SELECT_BY_MEASUREMENT_TYPE, measurementType);
    }

    private List<QuantityMeasurementEntity> getMeasurements(String query, String parameter) {
        List<QuantityMeasurementEntity> measurements = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (parameter != null) {
                pstmt.setString(1, parameter);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    measurements.add(mapResultSetToEntity(rs));
                }
            }
            return measurements;
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(query, e);
        }
    }

    private QuantityMeasurementEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setId(rs.getInt("id"));
        entity.thisValue = rs.getDouble("this_value");
        entity.thisUnit = rs.getString("this_unit");
        entity.thisMeasurementType = rs.getString("this_measurement_type");
        entity.thatValue = rs.getDouble("that_value");
        entity.thatUnit = rs.getString("that_unit");
        entity.thatMeasurementType = rs.getString("that_measurement_type");
        entity.operation = rs.getString("operation");
        entity.resultValue = rs.getDouble("result_value");
        entity.resultUnit = rs.getString("result_unit");
        entity.resultMeasurementType = rs.getString("result_measurement_type");
        entity.resultString = rs.getString("result_string");
        entity.isError = rs.getBoolean("is_error");
        entity.errorMessage = rs.getString("error_message");
        return entity;
    }

    @Override
    public int getTotalCount() {
        try (Connection conn = connectionPool.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(COUNT_QUERY)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(COUNT_QUERY, e);
        }
    }

    @Override
    public void deleteAll() {
        try (Connection conn = connectionPool.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(DELETE_ALL_QUERY);
            logger.info("Deleted all measurements from database");
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(DELETE_ALL_QUERY, e);
        }
    }

    @Override
    public String getPoolStatistics() {
        return connectionPool.toString();
    }

    @Override
    public void releaseResources() {
        connectionPool.closeAll();
    }
}
