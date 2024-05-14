package ru.kogtev.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigManager {
    private static final String PROPERTIES_FILE_NAME = "config.properties";
    private static final String PORT = "PORT";
    private static final int DEFAULT_PORT_VALUE = 8899;


    private ConfigManager() {

    }

    public static int getProperty() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE_NAME)) {
            prop.load(input);
            return Integer.parseInt(prop.getProperty(PORT));
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error with properties file : " + e.getMessage()); // Исправить формулировку
            // Устанавливаем порт по умолчанию, если возникла ошибка при загрузке конфигурации
            return DEFAULT_PORT_VALUE;
        }
    }

    public static int getProperty(String propertiesFileName) {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(propertiesFileName)) {
            prop.load(input);
            return Integer.parseInt(prop.getProperty(PORT));
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error with properties file : " + e.getMessage()); // Исправить формулировку
            // Устанавливаем порт по умолчанию, если возникла ошибка при загрузке конфигурации
            return DEFAULT_PORT_VALUE;
        }
    }
}
