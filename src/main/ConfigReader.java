package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            System.out.println("Failed to load config.properties");
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}