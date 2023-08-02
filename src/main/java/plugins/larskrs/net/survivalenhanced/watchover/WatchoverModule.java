package plugins.larskrs.net.survivalenhanced.watchover;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.dungeons.DungeonModule;
import plugins.larskrs.net.survivalenhanced.dungeons.PartyManager;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.general.Module;
import plugins.larskrs.net.survivalenhanced.general.ModuleManager;
import plugins.larskrs.net.survivalenhanced.location.LocationChange;
import plugins.larskrs.net.survivalenhanced.location.LocationManager;
import plugins.larskrs.net.survivalenhanced.location.StoredLocation;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.*;

public class WatchoverModule extends Module implements Listener {


    private static List<UUID> watchovers;
    private static List<UUID> vanished;
    private YamlConfiguration config;
    private static HashMap<UUID, StoredLocation> lastLocations;


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
        StoredLocation[] storedLocations = LocationManager.GetAllStoredLocations();
        Collections.reverse(Arrays.asList(storedLocations));

        for (StoredLocation stored : storedLocations)
        {
            if (!lastLocations.containsKey(stored.getPlayer())) {
                lastLocations.put(stored.getPlayer(), stored);
            }
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
    public static void SaveLastLocation (Player player, Location location, boolean bypass, LocationChange changeReason) {

        if (ModuleManager.isModuleLoaded("DungeonModule")) {
            Messanger.InfoConsole("DUNGEON IS ONLINE");
            if (PartyManager.GetParty(player.getUniqueId()) != null) {
                return;
            }
        }

        LocationManager.StoreLocation(location, player, changeReason);

        if (lastLocations.containsKey(player.getUniqueId())) {
            lastLocations.remove(player.getUniqueId());
        }
        StoredLocation stored = new StoredLocation(player.getUniqueId(), location, changeReason);
        lastLocations.put(player.getUniqueId(), stored);

    }

    public static StoredLocation getLastLocation(UUID uuid) {
        return lastLocations.get(uuid);
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent e) {
        SaveLastLocation(e.getPlayer(),  e.getPlayer().getLocation(), false, LocationChange.QUIT_GAME);
    }
    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e) {
        SaveLastLocation(e.getEntity(),  e.getEntity().getLocation(), false, LocationChange.DEATH);
    }
    @EventHandler
    public void onPlayerTeleport (PlayerTeleportEvent e) {
        SaveLastLocation(e.getPlayer(), e.getFrom(), false, LocationChange.TELEPORT);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        if (isVanished(e.getPlayer().getUniqueId())) {
            HidePlayer(e.getPlayer());
            e.setJoinMessage("");
        }
    }
}
