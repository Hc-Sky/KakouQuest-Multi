package fr.studiokakou.network;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GetConfig {
    public static int getIntProperty(String key) {
        Properties prop = loadPropertiesFile();
        String info = prop.getProperty(key);
        return Integer.parseInt(info);
    }

    public static String getStringProperty(String key) {
        Properties prop = loadPropertiesFile();
        return prop.getProperty(key);
    }

    private static Properties loadPropertiesFile() {
        Properties prop = new Properties();
        try (FileReader reader = new FileReader("core/src/server_config.properties")) {
            prop.load(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Le fichier config.properties n'a pas été trouvé", e);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier config.properties", e);
        }
        return prop;
    }
}
