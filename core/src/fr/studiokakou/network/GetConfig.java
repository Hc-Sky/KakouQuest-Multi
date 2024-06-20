package fr.studiokakou.network;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe utilitaire pour récupérer les propriétés du serveur.
 */
public class GetConfig {

    /**
     * Récupère une propriété de type int.
     * @param key
     * @return
     */
    public static int getIntProperty(String key) {
        Properties prop = loadPropertiesFile();
        String info = prop.getProperty(key);
        return Integer.parseInt(info);
    }

    /**
     * Récupère une propriété de type String.
     * @param key
     * @return
     */
    public static String getStringProperty(String key) {
        Properties prop = loadPropertiesFile();
        return prop.getProperty(key);
    }

    /**
     * Charge le fichier de  du server.
     * @return
     */
    private static Properties loadPropertiesFile() {
        Properties prop = new Properties();
        try (FileReader reader = new FileReader("server_config.properties")) {
            prop.load(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Le fichier config.properties n'a pas été trouvé", e);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier config.properties", e);
        }
        return prop;
    }
}
