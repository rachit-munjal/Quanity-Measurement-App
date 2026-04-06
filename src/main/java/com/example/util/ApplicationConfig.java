package com.example.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * DatabaseConfig is a singleton class responsible for loading and managing database *
 * configuration properties. It supports loading configurations from a properties file,
 * system properties, or environment variables. The class provides methods to retrieve
 * configuration values and manage different environments (development, testing, production).
 */
public class ApplicationConfig {

    private static final Logger logger = Logger.getLogger(ApplicationConfig.class.getName());
    private static ApplicationConfig instance;
    private Properties properties;
    private Environment environment;

    // Create enum for Environment to manage different environments like development,
    // testing, and production derived from system properties or environment variables
    public enum Environment {
        DEVELOPMENT, TESTING, PRODUCTION
    }

    // Creating Enum for all the configuration keys to avoid hardcoding and typos
    public enum ConfigKey {
        REPOSITORY_TYPE("repository.type"),
        DB_DRIVER_CLASS("db.driver"),
        DB_URL("db.url"),
        DB_USERNAME("db.username"),
        DB_PASSWORD("db.password"),
        DB_POOL_SIZE("db.pool-size"),
        HIKARI_MAX_POOL_SIZE("db.hikari.maximum-pool-size"),
        HIKARI_MIN_IDLE("db.hikari.minimum-idle"),
        HIKARI_CONNECTION_TIMEOUT("db.hikari.connection-timeout"),
        HIKARI_IDLE_TIMEOUT("db.hikari.idle-timeout"),
        HIKARI_MAX_LIFETIME("db.hikari.max-lifetime"),
        HIKARI_POOL_NAME("db.hikari.pool-name"),
        HIKARI_CONNECTION_TEST_QUERY("db.hikari.connection-test-query");

        private final String key;

        ConfigKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    private ApplicationConfig() {
        loadConfiguration();
    }

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    /**
     * Loads configuration properties from a properties file, system properties, or
     * environment variables.
     */
    private void loadConfiguration() {
        properties = new Properties();
        try {
            // Try to load from system property or default to application.properties
            String env = System.getProperty("app.env");
            if (env == null || env.isEmpty()) {
                env = System.getenv("APP_ENV");
            }

            String configFile = "application.properties";
            InputStream input = ApplicationConfig.class
                    .getClassLoader()
                    .getResourceAsStream(configFile);

            if (input != null) {
                properties.load(input);
                logger.info("Configuration loaded from " + configFile);

                // loading environment from properties file if not set in system properties or environment variables
                if (env == null || env.isEmpty()) {
                    env = properties.getProperty("app.env", "development");
                }
                this.environment = Environment.valueOf(env.toUpperCase());
            } else {
                logger.warning("Configuration file not found, using defaults");
                loadDefaults();
            }
        } catch (Exception e) {
            logger.severe("Error loading configuration: " + e.getMessage());
            loadDefaults();
        }
    }

    /**
     * Loads default configuration values.
     */
    private void loadDefaults() {
        this.environment = Environment.DEVELOPMENT;
        properties.setProperty(ConfigKey.DB_DRIVER_CLASS.getKey(), "org.h2.Driver");
        properties.setProperty(ConfigKey.DB_URL.getKey(), "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        properties.setProperty(ConfigKey.DB_USERNAME.getKey(), "sa");
        properties.setProperty(ConfigKey.DB_PASSWORD.getKey(), "");
        properties.setProperty(ConfigKey.DB_POOL_SIZE.getKey(), "5");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            String value = properties.getProperty(key);
            return (value != null) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String getEnvironment() {
        return environment.name();
    }

    /**
     * Checks if the provided key is a valid configuration key defined in the ConfigKey enum.
     */
    public boolean isConfigKey(String key) {
        for (ConfigKey configKey : ConfigKey.values()) {
            if (configKey.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Print all configuration properties for debugging purposes.
     */
    public void printAllProperties() {
        properties.forEach((key, value) -> logger.info(key + "=" + value));
    }

    public static void main(String[] args) {
        ApplicationConfig config = ApplicationConfig.getInstance();
        config.printAllProperties();
    }
}

