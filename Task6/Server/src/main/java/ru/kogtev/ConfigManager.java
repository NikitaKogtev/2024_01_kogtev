package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    
    private static final String PROPERTIES_FILE_NAME = "chat.properties";
    private static final String PORT_VALUE_IN_PROPERTIES_FILE = "PORT";
    private static final int DEFAULT_PORT_VALUE = 8899;

    private ConfigManager() {

    }

    public static int getProperty() {
        logger.info("Получение порта из файла конфигурации");
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE_NAME)) {
            prop.load(input);
            return Integer.parseInt(prop.getProperty(PORT_VALUE_IN_PROPERTIES_FILE));
        } catch (IOException | NumberFormatException e) {
            logger.warn("Не удалось считать порт с конфигурационного файла {}. " +
                    "Порт будет принят {}", e.getMessage(), DEFAULT_PORT_VALUE);
            return DEFAULT_PORT_VALUE;
        }
    }
}
