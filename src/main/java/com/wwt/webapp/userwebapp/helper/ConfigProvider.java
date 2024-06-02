/* Copyright 2018-2021 Wehe Web Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wwt.webapp.userwebapp.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigProvider {

    private static final Logger logger = LoggerFactory.getLogger(ConfigProvider.class);

    private static final String propFileName = "/config.properties";
    private static Properties prop;


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

