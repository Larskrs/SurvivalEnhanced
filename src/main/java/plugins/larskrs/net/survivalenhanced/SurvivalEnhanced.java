package plugins.larskrs.net.survivalenhanced;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugins.larskrs.net.survivalenhanced.debug.DebugCommand;

public final class SurvivalEnhanced extends JavaPlugin {

    public static FileManager fileManager;
    public static HorseManager horseManager;
    public static InteractionManager interactionManager;

    public static HorseManager GetHorseManager() {
        return horseManager;
    }
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
        horseManager = new HorseManager(this);
        interactionManager = new InteractionManager(this);


        // ------------------
        //     COMMANDS
        // ------------------
        new HorseCommand(this);
        new DebugCommand(this);

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
