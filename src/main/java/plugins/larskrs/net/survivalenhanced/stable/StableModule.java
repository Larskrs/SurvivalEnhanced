package plugins.larskrs.net.survivalenhanced.stable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.general.Module;
import plugins.larskrs.net.survivalenhanced.steed.Steed;
import plugins.larskrs.net.survivalenhanced.steed.SteedModule;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class StableModule extends Module {

    public HashMap<String, Stable> stables;
    private YamlConfiguration stableConfig;
    private static StableModule instance;

    public StableModule() {
        super("StableModule");
    }

    @Override
    public boolean onLoadModule() {

        instance = this;
        SurvivalEnhanced.GetFileManager().LoadFile("stable.yml");
        this.stables = new HashMap<>();
        this.stableConfig = SurvivalEnhanced.GetFileManager().GetYamlFromString("stable.yml");
        SetDefaultConfigValues();

        if (!stableConfig.getBoolean("enabled")) {

            return true; // Module failed to load
        }

        LoadStables();

        new StableCommand(SurvivalEnhanced.getInstance());

        return false; // Module successfully loaded
    }
    public static StableModule getInstance() {
        return instance;
    }

    public Stable GetStable (UUID playerID) {
        for (Stable stb : stables.values()
             ) {
            if (stb.getOwner() == playerID) {
                return stb;
            }
        }
        return null;
    }
    public Stable GetStable (String name) {
        return stables.get(name);
    }
    public float GetStableRadius () {
        return (float) stableConfig.getDouble("settings.stable-radius");
    }

    public void RegisterStable (String name, UUID owner, Location location) {
        if (StableExists(name)) { return; }

        Stable stable = new Stable(name, owner, location);
        SetStable(name, stable);
        SaveStable(stable);

    }
    public void SaveStable (Stable stable) {
        ConfigurationSection section = stableConfig.createSection("stables."+stable.getName());
        section.set("owner", stable.getOwner().toString());
        section.set("location.world", stable.getLocation().getWorld().getName());
        section.set("location.x", stable.getLocation().getBlockX());
        section.set("location.y", stable.getLocation().getBlockY());
        section.set("location.z", stable.getLocation().getBlockZ());
        section.set("radius", 3.5f);

        SurvivalEnhanced.GetFileManager().SaveData("stable.yml");
    }

    public void LoadStables () {

        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "-- Stables Loaded --");
        ConfigurationSection stableSection = stableConfig.getConfigurationSection("stables");
        if (stableSection == null) { return; }
        for (String stableName : stableSection.getKeys(false)) {
            ConfigurationSection section = stableConfig.getConfigurationSection("stables." + stableName);
            UUID owner = UUID.fromString(section.getString("owner"));
            Location location = SurvivalEnhanced.GetFileManager().ReadBlockLocation(section.getConfigurationSection("location"));
            Stable stable = new Stable(stableName, owner, location);
            Bukkit.getConsoleSender().sendMessage("   " +  ChatColor.GRAY + " - " + stableName);
            SetStable(stableName, stable);
        }

    }

    public void StoreSteed (Steed steed) {
        Messanger.InfoConsole("Storing steed, " + steed.custom_name);
        SteedModule.getInstance().StoreSteed(steed);
    }

    public Stable GetClosestStable (Location location) {

        boolean defined = false;
        double shortest = 0;
        Stable closest = null;
        for (Stable stable : stables.values()
             ) {


            Location stableLocation = stable.getLocation();
            double distance = location.distance(stableLocation);

            if (!defined) {
                defined = true;
                shortest = distance;
                closest = stable;
                continue;
            }

            if (distance < shortest) {
                shortest = distance;
                closest = stable;
            }

        }

        return closest;



    }

    private void SetDefaultConfigValues () {
        if (!stableConfig.contains("settings.stable-radius")) {
            stableConfig.set("settings.stable-radius", 5);
        }
        if (!stableConfig.contains("enabled")) {
            stableConfig.set("enabled", true);
        }


        FileManager.getInstance().SaveData("stable.yml");


    }

    public boolean StableExists (String stableName) {
        return stables.containsKey(stableName);
    }


    public void SetStableCenter(String name, Location location) {
        Stable stable = GetStable(name);
        if (stable == null) { return; }

        stable.setLocation(location);
        SetStable(name, stable);
        SaveStable(stable);
    }

    public void SetStable (String name, Stable stable) {
        if (StableExists(name)) {
            stables.remove(name);
        }
        stables.put(name, stable);
    }


    public Stable[] GetStables() {
        return Arrays.asList(stables.values().toArray()).toArray(new Stable[0]);
    }

}
