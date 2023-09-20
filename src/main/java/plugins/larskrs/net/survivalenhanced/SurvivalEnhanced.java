package plugins.larskrs.net.survivalenhanced;

import org.bstats.bukkit.Metrics;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import plugins.larskrs.net.survivalenhanced.bounty.BountyCommand;
import plugins.larskrs.net.survivalenhanced.commands.*;
import plugins.larskrs.net.survivalenhanced.database.Database;
import plugins.larskrs.net.survivalenhanced.database.SQLite;
import plugins.larskrs.net.survivalenhanced.dependencies.VaultDependency;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.general.ModuleManager;
import plugins.larskrs.net.survivalenhanced.gui.GUIManager;
import plugins.larskrs.net.survivalenhanced.hats.HatCommand;
import plugins.larskrs.net.survivalenhanced.interaction.InteractionListener;
import plugins.larskrs.net.survivalenhanced.interaction.InteractionManager;
import plugins.larskrs.net.survivalenhanced.items.CustomItemManager;
import plugins.larskrs.net.survivalenhanced.location.BackCommand;
import plugins.larskrs.net.survivalenhanced.location.LocationManager;
import plugins.larskrs.net.survivalenhanced.location.StoredLocation;
import plugins.larskrs.net.survivalenhanced.misc.SpawnerListener;
import plugins.larskrs.net.survivalenhanced.motd.MotdCommand;
import plugins.larskrs.net.survivalenhanced.skull.SkullCommand;
import plugins.larskrs.net.survivalenhanced.location.LocationTools;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;
import plugins.larskrs.net.survivalenhanced.tools.TimeUtil;
import plugins.larskrs.net.survivalenhanced.world.WorldCommand;

import java.time.Instant;
import java.time.temporal.TemporalField;

public final class SurvivalEnhanced extends JavaPlugin implements Listener {

    public static FileManager fileManager;
    public static InteractionManager interactionManager;
    public static CustomItemManager itemManager;

    public static FileManager GetFileManager() {
        return fileManager;
    }
    public static InteractionManager GetInteractionManager() {
        return interactionManager;
    }

    private static Database db;
    public static Database getDatabase () {
        return db;
    }

    public static SurvivalEnhanced instance;
    public static SurvivalEnhanced getInstance () {
        return instance;
    }


    @Override
    public void onEnable() {

        instance = this;

        // Plugin startup logic
        fileManager = new FileManager(this);
        itemManager = new CustomItemManager();
        CustomItemManager.Setup();


        // ------------------
        //      Database
        // ------------------
        this.db = new SQLite(this);
        this.db.load();

        new LocationManager().Setup();
        interactionManager = new InteractionManager(this);

        int pluginId = 18851; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);



        // ------------------
        //   Load Managers
        // ------------------

        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        new ModuleManager().Load();

        new SkullCommand();
        new EnchantCommand();
        new SpawnMobCommand();
        new RandomTPCommand();
        new HealCommand();
        new FeedCommand();
        new WorldCommand();
        new CustomModelCommand();
        new FlyCommand();
        new FlySpeedCommand();
        new BackCommand();
        new HatCommand();
        new BountyCommand();

        new MotdCommand();

        // ------------------
        //   Setup Managers
        // ------------------

        new GUIManager().Setup(this);



        // ------------------
        //    Dependencies1
        // ------------------

        new VaultDependency().Setup(this);

        // ------------------
        //     LISTENERS
        // ------------------
        Bukkit.getPluginManager().registerEvents(itemManager, this);
        Bukkit.getPluginManager().registerEvents(new InteractionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SpawnerListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {

        if (!e.getPlayer().hasPlayedBefore()) {


            World world = e.getPlayer().getWorld();
            Location loc = LocationTools.getRandomLocation(world, -1000, 1000);
            e.getPlayer().teleport(loc);

            e.getPlayer().sendMessage(ChatColor.GREEN + "Randomly Teleported to: " +
                    loc.getBlockX() + " " +
                    loc.getBlockY() + " " +
                    loc.getBlockZ() + " )"
            );
        }
        }


    @Override
    public void onDisable() {
        // Plugin shutdown logic

        for (Player player : Bukkit.getOnlinePlayers()
             ) {
            player.closeInventory();
        }

    }
}
