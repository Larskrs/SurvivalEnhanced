package plugins.larskrs.net.survivalenhanced;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FileManager {

    private SurvivalEnhanced se;
    private HashMap<String, YamlConfiguration> fileMap;

    public FileManager (SurvivalEnhanced se) {
        this.se = se;

        this.fileMap = new HashMap<>();

        LoadDataFolder();
    }

    // ------------------------
    //      Handle Lists
    // ------------------------

    public YamlConfiguration GetYamlFromString (String fileName) {
        return fileMap.get(fileName);
    }

    // ------------------------
    //      LOAD & SAVE
    // ------------------------
    public void LoadDataFolder () {
        // Check if the data_folder exists, if not we will create the folder.
        File dataFolder = se.getDataFolder();
        if (!dataFolder.exists()) {
            try {
                dataFolder.mkdir();
                dataFolder.createNewFile();
            } catch (IOException e) {
                System.out.println("Failed to load datafolder");
                throw new RuntimeException(e);
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Loaded DataFolder");
    }
    public void LoadFile (String fileName) {
        // This is the logic for loading the data folder and setting its parameters
        // Usage: Reload Plugin and Start Logic.

        File file = new File(se.getDataFolder(), fileName);
        if (!file.exists()) {
            // File does not exist, lets make it.
            try {
                file.createNewFile();
            } catch (IOException e) {

                System.out.println("Can't load data file! Error.");
                throw new RuntimeException(e);
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Loaded " + fileName);
        // Attempt to load file and put it in the fileMap
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        fileMap.put(fileName, config);
    }

    public void SaveData (String fileName) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Attempting " + fileName);
        File file = new File(se.getDataFolder(), fileName);

        try {
            GetYamlFromString(fileName).save(file);
        } catch (IOException e) {
            System.out.println("Can't save data file! Error.");
            throw new RuntimeException(e);
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Saved and loaded the config: " + fileName);
    }


}
