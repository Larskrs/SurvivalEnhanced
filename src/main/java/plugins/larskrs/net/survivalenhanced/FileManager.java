package plugins.larskrs.net.survivalenhanced;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FileManager {

    private SurvivalEnhanced se;
    private HashMap<String, YamlConfiguration> fileMap;
    public static FileManager instance;

    public FileManager (SurvivalEnhanced se) {
        this.se = se;

        this.fileMap = new HashMap<>();
        instance = this;

        LoadDataFolder();
    }

    public YamlConfiguration GetYamlConfig(String identity) {

        if (!fileMap.containsKey(identity)) {
            return null;
        }

        return fileMap.get(identity);
    }

    public static FileManager getInstance() {
        return instance;
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
//        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Loaded " + fileName);

        // Attempt to load file and put it in the fileMap
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        fileMap.put(fileName, config);
    }

    public void SaveData (String fileName) {
//        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Attempting " + fileName);
        File file = new File(se.getDataFolder(), fileName);

        try {
            GetYamlFromString(fileName).save(file);
        } catch (IOException e) {
            System.out.println("Can't save data file! Error.");
            throw new RuntimeException(e);
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Saved and loaded the config: " + fileName);
    }


    public Location ReadBlockLocation(ConfigurationSection location) {
        return new Location(
                Bukkit.getWorld(location.getString("world")),
                location.getDouble("x"),
                location.getDouble("y"),
                location.getDouble("z"),
                0f,
                0f
        );
    }
    public Location ReadLocation (String identity, String address ) {

        YamlConfiguration config = GetYamlConfig(identity);
        ConfigurationSection section = config.getConfigurationSection(address);

        return new Location(
                Bukkit.getWorld(section.getString("world")),
                        section.getDouble("x"),
                        section.getDouble("y"),
                        section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch")
        );
    }

    public void SaveLocation(String identity, String address, Location location) {

        YamlConfiguration config = GetYamlConfig(identity);

        ConfigurationSection section = config.createSection(address);
        Messanger.InfoConsole(address);


        // Serialization
        section.set("world", location.getWorld().getName());
        //
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("pitch", location.getPitch());
        section.set("yaw", location.getYaw());
        //
        SaveData(identity);
    }
}
