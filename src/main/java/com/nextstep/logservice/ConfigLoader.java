package com.nextstep.logservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);
    private static final Properties config = new Properties();

    static {
        loadProperties();
        printLoadedValues(); // Print after load
    }

    private static void loadProperties() {
        try (InputStream input = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("LogConfig.properties")) {

            if (input == null) {
                log.error("❌ LogConfig.properties NOT FOUND in resources!");
                throw new RuntimeException("Config file missing");
            }

            config.load(input);

            log.info("✅ LogConfig.properties loaded successfully");

        } catch (Exception e) {
            log.error("❌ Failed to load configuration", e);
            throw new RuntimeException(e);
        }
    }

    // 🔍 PRINT ALL PROPERTIES (Trimmed)
    private static void printLoadedValues() {
        log.info("========= Loaded Configuration Values =========");

        config.forEach((k, v) -> {
            String value = String.valueOf(v).trim();
            log.info("{} = {}", k, value);
        });

        log.info("===============================================");
    }

    // Getter with trim()
    public static String get(String key) {
        String value = config.getProperty(key);

        if (value == null) {
            log.warn("⚠ Missing property key: {}", key);
            return null;
        }

        return value.trim();
    }
}
