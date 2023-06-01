package plugins.larskrs.net.survivalenhanced.stable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StableManager {

    public HashMap<String, Stable> stables;
    private YamlConfiguration stableConfig;

    public StableManager () {
        SurvivalEnhanced.GetFileManager().LoadFile("stable.yml");
        this.stables = new HashMap<>();
        this.stableConfig = SurvivalEnhanced.GetFileManager().GetYamlFromString("stable.yml");

        LoadStables();
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
    public float GetStableRange (Stable stable) {
        return (float) stableConfig.getDouble("stables."+stable.getName()+".radius");
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

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Loading stables:");
        for (String stableName : stableConfig.getConfigurationSection("stables").getKeys(false)) {
            ConfigurationSection section = stableConfig.getConfigurationSection("stables." + stableName);
            UUID owner = UUID.fromString(section.getString("owner"));
            Location location = SurvivalEnhanced.GetFileManager().ReadBlockLocation(section.getConfigurationSection("location"));
            Stable stable = new Stable(stableName, owner, location);
            SetStable(stableName, stable);
        }

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
}
