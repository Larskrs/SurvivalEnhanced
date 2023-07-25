package plugins.larskrs.net.survivalenhanced.watchover;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.dungeons.PartyManager;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.general.Module;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.*;

public class WatchoverModule extends Module implements Listener {


    private static List<UUID> watchovers;
    private static List<UUID> vanished;
    private YamlConfiguration config;
    private static HashMap<UUID, Location> lastLocations;


    public WatchoverModule() {
        super("WatchoverModule");
    }



    @Override
    public boolean onLoadModule() {

        watchovers = new ArrayList<>();
        vanished = new ArrayList<>();
        lastLocations = new HashMap<>();


        new WatchoverCommand(SurvivalEnhanced.getInstance());
        new VanishCommand(SurvivalEnhanced.getInstance());

        Bukkit.getPluginManager().registerEvents(this, SurvivalEnhanced.getInstance());
        FileManager.getInstance().LoadFile("watchover.yml");
        this.config = SurvivalEnhanced.GetFileManager().GetYamlFromString("watchover.yml");

        SetDefaultConfigValues();

        for (String s : config.getConfigurationSection("last-locations").getKeys(false)
             ) {
            Messanger.InfoConsole("Loading: " + s);
            Location loc = FileManager.getInstance().ReadLocation("watchover.yml", "last-locations." + s);
            lastLocations.put(UUID.fromString(s), loc);
        }

        return false;
    }

    private void SetDefaultConfigValues () {
        boolean changed = false;
        if (!config.contains("last-locations")) {
            config.createSection("last-locations"); changed = true;
        }

        if (changed) {
            FileManager.getInstance().SaveData("watchover.yml");
        }


    }

    public static void WatchoverPlayer (Player player, Player target) {
        if (watchovers.contains(player.getUniqueId())) {
            watchovers.remove(player.getUniqueId());
        }
        if (!isVanished(player.getUniqueId())) {
            Vanish(player);
        }
        watchovers.add(player.getUniqueId());

    }

    private static void Vanish(Player player) {
        if (isVanished(player.getUniqueId())) {
            vanished.remove(player.getUniqueId());
        }
        vanished.add(player.getUniqueId());
        HidePlayer(player);
    }

    public static void UnWatchoverPlayer (Player player) {

    }

    public static boolean isVanished (UUID uuid) {
        return vanished.contains(uuid);
    }
    public static List<UUID> getVanished () {
        return vanished;
    }
    public static void HidePlayer (Player player) {
        for (Player p : Bukkit.getOnlinePlayers()
             ) {
            if (p.equals(player)) {
                continue;
            }
            if (p.hasPermission("survivalenhanced.vanish.see")) {
                continue;
            }

            p.hidePlayer(player);
        }
    }
    public static void UnHidePlayer (Player player) {
        if (!isVanished(player.getUniqueId())) {
            return;
        }

        player.sendMessage(ChatColor.YELLOW +
                "You vanished!");

    }
    public static void SaveLastLocation (Player player, boolean save, boolean bypass) {


        if (PartyManager.HasParty(player.getUniqueId()) || bypass) {
            return;
        }

        Messanger.InfoConsole("Saving last location of player: " + player.getName() + " in world: " + player.getLocation());

        if (lastLocations.containsKey(player.getUniqueId())) {
            lastLocations.remove(player.getUniqueId());
        }
        lastLocations.put(player.getUniqueId(), player.getLocation());
        if (save) {

            FileManager.getInstance().SaveLocation("watchover.yml", "last-locations." + player.getUniqueId(), player.getLocation());
            FileManager.getInstance().SaveData("watchover.yml");

        }

    }

    public static Location getLastLocation(UUID uuid) {
        return lastLocations.get(uuid);
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent e) {
        SaveLastLocation(e.getPlayer(), true, false);
    }
    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e) {
        SaveLastLocation(e.getEntity(), false, false);
    }
    @EventHandler
    public void onPlayerTeleport (PlayerTeleportEvent e) {
        SaveLastLocation(e.getPlayer(), false, false);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        if (isVanished(e.getPlayer().getUniqueId())) {
            HidePlayer(e.getPlayer());
            e.setJoinMessage("");
        }
    }
}
