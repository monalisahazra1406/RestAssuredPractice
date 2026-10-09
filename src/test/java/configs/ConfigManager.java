package configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Config.properties file not found");
            }
            properties.load(input);
        } catch (IOException e){
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getBaseUrl(){

        return System.getProperty("baseUrl",properties.getProperty("baseUrl"));
    }
}
