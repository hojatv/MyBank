package org.simplebank.common;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Configs {

    private static final Properties properties = new Properties();
    private static final Logger log = Logger.getLogger(Configs.class);

    static {
        loadConfig("application.properties");
    }


    public static String getProperty(String key, String defaultVal) {
        String value = getProperty(key);
        return value != null ? value : defaultVal;
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return value;
    }

    public static void loadConfig(String fileName) {
        try {
            log.info("loadConfig(): Loading config file: " + fileName);
            final InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            properties.load(config);

        } catch (FileNotFoundException fne) {
            log.error("loadConfig(): file name not found " + fileName, fne);
        } catch (IOException ioe) {
            log.error("loadConfig(): error when reading the config " + fileName, ioe);
        }
    }


}
