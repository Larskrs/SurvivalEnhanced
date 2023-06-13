package plugins.larskrs.net.survivalenhanced.prefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PrefixManager {


    public HashMap<UUID, Prefix> holders;
    private ArrayList<Prefix> prefixes;
    private YamlConfiguration prefixConfig;
    private static PrefixManager instance;

    public void Setup(SurvivalEnhanced survivalEnhanced) {
        instance = this;
        SurvivalEnhanced.GetFileManager().LoadFile("prefix.yml");
        this.holders = new HashMap<>();
        this.prefixes = new ArrayList<>();
        this.prefixConfig = SurvivalEnhanced.GetFileManager().GetYamlFromString("prefix.yml");



        LoadPrefixes();
        new PrefixCommand(survivalEnhanced);
    }
    public static PrefixManager getInstance() {
        return instance;
    }

    private void LoadPrefixes () {

        ArrayList<Prefix> prefixes = new ArrayList<>();

        if (!prefixConfig.contains("prefixes")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "No prefixes found in prefix.yml");
            return;
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "-- Prefixes Loaded --");
        for (String s : prefixConfig.getConfigurationSection("prefixes").getKeys(false)) {

            // Prefix
            ConfigurationSection section = prefixConfig.getConfigurationSection("prefixes." + s);

            String display = ChatColor.translateAlternateColorCodes('&', section.getString("display"));
            Material material = Material.getMaterial(section.getString("icon"));

            Prefix prefix = new Prefix(
                    display, //
                    s,       //
                    material // Prefix icon in the menu.
            );

            prefixes.add(prefix);
            Bukkit.getConsoleSender().sendMessage("   " +  ChatColor.GRAY + " - " + s + " - " + display);

        }

    }

    public ArrayList<Prefix> GetPrefixes() {
        return prefixes;
    }
}
