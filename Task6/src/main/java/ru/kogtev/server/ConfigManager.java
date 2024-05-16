package ru.kogtev.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigManager {
    private static final String PROPERTIES_FILE_NAME = "chat.properties";
    private static final String PORT_VALUE_IN_PROPERTIES_FILE = "PORT";
    private static final int DEFAULT_PORT_VALUE = 8899;

    private ConfigManager() {

    }

    public static int getProperty() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE_NAME)) {
            prop.load(input);
            return Integer.parseInt(prop.getProperty(PORT_VALUE_IN_PROPERTIES_FILE));
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error in the configuration file: " + e.getMessage());
            return DEFAULT_PORT_VALUE;
        }
    }
}
