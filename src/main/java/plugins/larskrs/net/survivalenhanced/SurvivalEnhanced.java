package plugins.larskrs.net.survivalenhanced;

import org.bstats.bukkit.Metrics;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import plugins.larskrs.net.survivalenhanced.commands.*;
import plugins.larskrs.net.survivalenhanced.database.Database;
import plugins.larskrs.net.survivalenhanced.database.SQLite;
import plugins.larskrs.net.survivalenhanced.dependencies.VaultDependency;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.general.ModuleManager;
import plugins.larskrs.net.survivalenhanced.gui.GUIManagar;
import plugins.larskrs.net.survivalenhanced.interaction.InteractionListener;
import plugins.larskrs.net.survivalenhanced.interaction.InteractionManager;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixManager;
import plugins.larskrs.net.survivalenhanced.skull.SkullCommand;
import plugins.larskrs.net.survivalenhanced.steed.SteedModule;
import plugins.larskrs.net.survivalenhanced.tools.LocationTools;
import plugins.larskrs.net.survivalenhanced.watchover.WatchoverCommand;
import plugins.larskrs.net.survivalenhanced.world.WorldCommand;

import java.util.ArrayList;
import java.util.List;

public final class SurvivalEnhanced extends JavaPlugin implements Listener {

    public static FileManager fileManager;
    public static InteractionManager interactionManager;

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


        // ------------------
        //   Setup Managers
        // ------------------

        new PrefixManager().Setup(this);
        new GUIManagar().Setup(this);

        // ------------------
        //      Database
        // ------------------
        this.db = new SQLite(this);
        this.db.load();



        // ------------------
        //    Dependencies1
        // ------------------

        new VaultDependency().Setup(this);

        // ------------------
        //     LISTENERS
        // ------------------
        Bukkit.getPluginManager().registerEvents(new InteractionListener(this), this);
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
