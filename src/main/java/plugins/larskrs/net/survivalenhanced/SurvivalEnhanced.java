package plugins.larskrs.net.survivalenhanced;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plugins.larskrs.net.survivalenhanced.database.Database;
import plugins.larskrs.net.survivalenhanced.database.SQLite;
import plugins.larskrs.net.survivalenhanced.dependencies.VaultDependency;
import plugins.larskrs.net.survivalenhanced.gui.GUIManagar;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixManager;
import plugins.larskrs.net.survivalenhanced.stable.StableManager;
import plugins.larskrs.net.survivalenhanced.steed.SteedManager;
import plugins.larskrs.net.survivalenhanced.watchover.WatchoverCommand;

public final class SurvivalEnhanced extends JavaPlugin {

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


    @Override
    public void onEnable() {
        // Plugin startup logic
        fileManager = new FileManager(this);
        interactionManager = new InteractionManager(this);

        // ------------------
        //   Setup Managers
        // ------------------

        new SteedManager().Setup(this);
        new StableManager().Setup(this);
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


        new WatchoverCommand(this);




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
