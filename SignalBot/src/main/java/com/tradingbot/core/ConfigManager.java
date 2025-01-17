package com.tradingbot.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigManager handles loading and managing application configurations.
 * It supports fetching configuration values for API keys, trading pairs, intervals, etc.
 */
public class ConfigManager {

    private static ConfigManager instance;
    private final Properties properties;

    /**
     * Private constructor to enforce singleton pattern.
     * Loads configurations from a properties file.
     */
    private ConfigManager() {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file.", e);
        }
    }

    /**
     * Provides a global access point to the ConfigManager instance.
     *
     * @return the singleton ConfigManager instance
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves a configuration value as a String.
     *
     * @param key the configuration key
     * @return the configuration value
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * Retrieves a configuration value as an integer.
     *
     * @param key the configuration key
     * @return the configuration value as an integer
     */
    public int getInt(String key) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value for key: " + key, e);
        }
    }

    /**
     * Retrieves a configuration value as a double.
     *
     * @param key the configuration key
     * @return the configuration value as a double
     */
    public double getDouble(String key) {
        try {
            return Double.parseDouble(properties.getProperty(key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid double value for key: " + key, e);
        }
    }

    /**
     * Retrieves a configuration value as a boolean.
     *
     * @param key the configuration key
     * @return the configuration value as a boolean
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    /**
     * Retrieves a configuration value or returns a default value if the key does not exist.
     *
     * @param key the configuration key
     * @param defaultValue the default value
     * @return the configuration value or the default value
     */
    public String getStringOrDefault(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Checks if a specific configuration key exists.
     *
     * @param key the configuration key
     * @return true if the key exists, false otherwise
     */
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }
}
