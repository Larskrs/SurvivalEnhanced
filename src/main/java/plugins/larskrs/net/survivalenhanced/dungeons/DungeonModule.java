package plugins.larskrs.net.survivalenhanced.dungeons;

import org.bukkit.Bukkit;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.general.Module;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.HashMap;

public class DungeonModule extends Module {

    public DungeonModule() {
        super("DungeonModule");
    }

    @Override
    public boolean onLoadModule() {

        if (!Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
            Messanger.ErrorConsole("The DungeonModule requires the Citizens plugin.");
            return true;
        }
        new PartyManager();


        return false;
    }
}
