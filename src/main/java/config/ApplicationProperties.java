package config;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationProperties {

    private static Properties properties;

    public ApplicationProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.ALL, "IOException Occurred while loading properties file " + e.getMessage());
        }
    }

    public static String getProperty(String name) {
        return System.getProperty("user.dir") + properties.getProperty(name);
    }
    public static String getJdbcProperty(String name) {
        return properties.getProperty(name);
    }

}
