package plugins.larskrs.net.survivalenhanced.general;

import com.sun.org.apache.xpath.internal.operations.Mod;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.bounty.BountyModule;
import plugins.larskrs.net.survivalenhanced.dungeons.DungeonModule;
import plugins.larskrs.net.survivalenhanced.hats.HatModule;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixModule;
import plugins.larskrs.net.survivalenhanced.quest.QuestModule;
import plugins.larskrs.net.survivalenhanced.stable.StableModule;
import plugins.larskrs.net.survivalenhanced.steed.SteedModule;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;
import plugins.larskrs.net.survivalenhanced.watchover.WatchoverModule;

import java.util.HashMap;

public class ModuleManager extends Module {
    public ModuleManager() {
        super("ModuleManager");
    }

    private static HashMap<String, Module> modules;

    public static Module[] GetModules() {
        return modules.values().toArray(new Module[0]);
    }

    public static void RegisterModule(Module module) {
        modules.put(module.getId(), module);
        boolean isEnabled = SurvivalEnhanced.getInstance().getConfig().getBoolean("modules." + module.getId(), false);

        module.SetEnabled(isEnabled);
        if (isEnabled) {
            module.Load();
        }
    }

    @Override
    public boolean onLoadModule() {
        new SurvivalEnchancedCommand();
        LoadModules();
        Messanger.Console(" ");

        return false;
    }

    @Override
    public boolean onReloadModule() {
        return false;
    }

    public static void UnloadModules () {
        for (Module m : modules.values()
             ) {
            m.Unload();
        }
    }
    public static void ReloadModules () {
        SurvivalEnhanced.instance.reloadConfig();
        for (Module m : modules.values()
             ) {
            if (m.isEnabled())  m.Reload();
        }

        for (Module mod : modules.values()
        ) {
            Messanger.Console(ChatColor.GRAY + mod.getId() + " - " + (mod.isEnabled() ? ChatColor.GREEN + "[Enabled]" : ChatColor.RED + "[Disabled]"));
        }
    }

    private static void LoadModules() {
        modules = new HashMap<>();

        // Manually add a modules to load here.
        RegisterModule(new StableModule());
        RegisterModule(new SteedModule());
        RegisterModule(new QuestModule());
        RegisterModule(new WatchoverModule());
        RegisterModule(new DungeonModule());
        RegisterModule(new PrefixModule());
        RegisterModule(new HatModule());
        RegisterModule(new BountyModule());


        // List all modules
        for (Module mod : modules.values()
        ) {
            Messanger.Console(ChatColor.GRAY + mod.getId() + " - " + (mod.isEnabled() ? ChatColor.GREEN + "[Enabled]" : ChatColor.RED + "[Disabled]"));
        }

    }

    @Override
    public boolean onUnloadModule() {
        return false;
    }

    public static boolean isModuleLoaded (String id) {
        for (Module module : modules.values())
        {
            if (module.getId().equals(id)) {
                return module.isEnabled();
            }
        }
        return false;
    }
}
