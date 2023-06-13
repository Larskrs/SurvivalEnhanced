package plugins.larskrs.net.survivalenhanced;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugins.larskrs.net.survivalenhanced.dependencies.VaultDependency;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixCommand;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixListener;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixManager;
import plugins.larskrs.net.survivalenhanced.stable.StableCommand;
import plugins.larskrs.net.survivalenhanced.stable.StableManager;
import plugins.larskrs.net.survivalenhanced.steed.SteedCommand;
import plugins.larskrs.net.survivalenhanced.steed.SteedListener;
import plugins.larskrs.net.survivalenhanced.steed.SteedManager;

public final class SurvivalEnhanced extends JavaPlugin {

    public static FileManager fileManager;
    public static InteractionManager interactionManager;

    public static FileManager GetFileManager() {
        return fileManager;
    }
    public static InteractionManager GetInteractionManager() {
        return interactionManager;
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


        // ------------------
        //    Dependencies
        // ------------------

        new VaultDependency().Setup(this);

        // ------------------
        //     LISTENERS
        // ------------------
        Bukkit.getPluginManager().registerEvents(new InteractionListener(this), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
