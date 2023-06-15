package plugins.larskrs.net.survivalenhanced.prefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.FileManager;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.dependencies.VaultDependency;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

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

        if (!Bukkit.getPluginManager().isPluginEnabled("vault")) {
            Messanger.ErrorConsole("You have to have vault installed to be able to use the prefix system");
            return;
        }

        SurvivalEnhanced.GetFileManager().LoadFile("prefix.yml");
        this.holders = new HashMap<>();
        this.prefixes = new ArrayList<>();
        this.prefixConfig = SurvivalEnhanced.GetFileManager().GetYamlFromString("prefix.yml");

        SetDefaultConfigValues();

        if (!prefixConfig.getBoolean("enabled")) {

            return;
        }

        LoadPrefixes();
        new PrefixCommand(survivalEnhanced);
        Bukkit.getPluginManager().registerEvents(new PrefixListener(), survivalEnhanced);
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
            Bukkit.getConsoleSender().sendMessage("   " +  ChatColor.GRAY + " - " + s + " - " + display + " - " + prefix.GetIcon().toString());

        }

        this.prefixes = prefixes;

    }

    public Prefix GetPrefix ( String name ) {
        for (Prefix prefix : prefixes) {
            if (name.equals(prefix.GetName())) {
                return prefix;
            }
        }
        return null;
    }

    public ArrayList<Prefix> GetPrefixes() {
        return prefixes;
    }

    public void SetPrefix(UUID holderID, Prefix prefix) {

        if (holders.containsKey(holderID)) {
            holders.remove(holderID);
        }
        holders.put(holderID, prefix);

        VaultDependency.GetChat().setPlayerPrefix(Bukkit.getPlayer(holderID), prefix.GetDisplay());
    }
    public void ClearPrefix (UUID holderID) {
        holders.remove(holderID);

        VaultDependency.GetChat().setPlayerPrefix(Bukkit.getPlayer(holderID), "");
    }
    public Prefix GetPrefix(Player player) {
        return holders.get(player.getUniqueId());
    }

    private void SetDefaultConfigValues () {
        if (!prefixConfig.contains("enabled")) {
            prefixConfig.set("enabled", true);
        }


        FileManager.getInstance().SaveData("prefix.yml");


    }
}
