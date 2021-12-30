package com.wwt.webapp.userwebapp.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProvider {

    private static final Logger logger = LoggerFactory.getLogger(ConfigProvider.class);

    private static final String propFileName = "/config.properties";
    private static Properties prop;

    private ConfigProvider() {}

    private static Properties getPropValues() {
        Properties prop = new Properties();
        try(InputStream inputStream = ConfigProvider.class.getResourceAsStream(propFileName)) {

            if (inputStream != null) {
                prop.load(inputStream);
            }
            else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        }
        catch (IOException e) {
            logger.error("prop file not found",e);
        }
        return(prop);
    }

    public static String getConfigValue(String key) {
        if(prop == null) {
            prop = getPropValues();
        }
        return prop.getProperty(key);
    }

    public static int getConfigIntValue(String key) {
        if(prop == null) {
            prop = getPropValues();
        }
        return Integer.parseInt(prop.getProperty(key));
    }

    public static boolean getConfigBoolean(String key) {
        if (prop == null) {
            prop = getPropValues();
        }
        return Boolean.parseBoolean(prop.getProperty(key));
    }

    public static void overrideProperty(String key, String value) {
        if (prop == null) {
            prop = getPropValues();
        }
        prop.setProperty(key,value);
    }


}

