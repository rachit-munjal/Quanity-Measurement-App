package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConnectionPool {

    private static final Logger logger = Logger.getLogger(ConnectionPool.class.getName());
    private static ConnectionPool instance;
    private List<Connection> availableConnections;
    private List<Connection> usedConnections;
    private final int poolSize;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String driverClass;
    private final String testQuery;

    private ConnectionPool() throws SQLException {
        ApplicationConfig config = ApplicationConfig.getInstance();
        this.poolSize = config.getIntProperty(ApplicationConfig.ConfigKey.DB_POOL_SIZE.getKey(), 10);
        this.dbUrl = config.getProperty(ApplicationConfig.ConfigKey.DB_URL.getKey());
        this.dbUsername = config.getProperty(ApplicationConfig.ConfigKey.DB_USERNAME.getKey());
        this.dbPassword = config.getProperty(ApplicationConfig.ConfigKey.DB_PASSWORD.getKey());
        this.driverClass = config.getProperty(ApplicationConfig.ConfigKey.DB_DRIVER_CLASS.getKey());
        this.testQuery = config.getProperty(ApplicationConfig.ConfigKey.HIKARI_CONNECTION_TEST_QUERY.getKey(), "SELECT 1");

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver class not found: " + driverClass, e);
        }

        availableConnections = new ArrayList<>(poolSize);
        usedConnections = new ArrayList<>();

        initializeConnections();
        logger.info("ConnectionPool initialized with " + poolSize + " connections");
    }

    public static synchronized ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private void initializeConnections() throws SQLException {
        for (int i = 0; i < poolSize; i++) {
            availableConnections.add(createConnection());
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    public synchronized Connection getConnection() throws SQLException {
        if (availableConnections.isEmpty()) {
            if (usedConnections.size() < poolSize) {
                availableConnections.add(createConnection());
            } else {
                throw new SQLException("Maximum pool size reached, no available connections.");
            }
        }

        Connection connection = availableConnections.remove(availableConnections.size() - 1);

        // Basic validation before handing out
        if (connection.isClosed()) {
            connection = createConnection();
        }

        usedConnections.add(connection);
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection != null && usedConnections.remove(connection)) {
            availableConnections.add(connection);
        }
    }

    public boolean validateConnection(Connection connection) {
        try (var stmt = connection.createStatement()) {
            stmt.execute(this.testQuery);
            return true;
        } catch (SQLException e) {
            logger.warning("Connection validation failed: " + e.getMessage());
            return false;
        }
    }

    public synchronized void closeAll() {
        for (Connection c : availableConnections) {
            try {
                c.close();
            } catch (SQLException e) {
                logger.warning("Error closing available connection: " + e.getMessage());
            }
        }
        for (Connection c : usedConnections) {
            try {
                c.close();
            } catch (SQLException e) {
                logger.warning("Error closing used connection: " + e.getMessage());
            }
        }
        availableConnections.clear();
        usedConnections.clear();
    }

    public int getAvailableConnectionCount() {
        return availableConnections.size();
    }

    public int getUsedConnectionCount() {
        return usedConnections.size();
    }

    public int getTotalConnectionCount() {
        return availableConnections.size() + usedConnections.size();
    }

    @Override
    public String toString() {
        return "ConnectionPool{" +
                "available=" + availableConnections.size() +
                ", used=" + usedConnections.size() +
                ", total=" + getTotalConnectionCount() +
                '}';
    }

    public static void main(String[] args) {
        try {
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection conn1 = pool.getConnection();
            logger.info("Validate connection: " + (pool.validateConnection(conn1) ? "Success" : "Failure"));
            logger.info("Available connections after acquiring 1: " + pool.getAvailableConnectionCount());
            logger.info("Used connections after acquiring 1: " + pool.getUsedConnectionCount());
            pool.releaseConnection(conn1);
            logger.info("Available connections after releasing 1: " + pool.getAvailableConnectionCount());
            logger.info("Used connections after releasing 1: " + pool.getUsedConnectionCount());
            pool.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

