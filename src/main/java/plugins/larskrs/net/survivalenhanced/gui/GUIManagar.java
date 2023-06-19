package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixManager;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GUIManagar extends BukkitRunnable {

    HashMap<UUID, GeneralGUI> guis;
    private SurvivalEnhanced se;
    private static GUIManagar instance;
    private BukkitTask task;
    public GUIManagar () {
    }

    public void Setup (SurvivalEnhanced se) {
        this.se = se;
        Bukkit.getPluginManager().registerEvents(new GUIListener(), se);
        guis = new HashMap<>();

        instance = this;

        this.task = runTaskTimer(se, 0, 5);
    }
    public void Unset (SurvivalEnhanced se) {
        this.task.cancel();
        this.task = null;
    }

    public static GUIManagar getInstance() {
        return instance;
    }


    public void LinkGUI (Player p, GeneralGUI gui) {
        guis.put(p.getUniqueId(),gui);
//        Messanger.All("Linked you to a gui");
    }

    public void UnlinkGUI(Player p) {
        guis.remove(p.getUniqueId());
//        Messanger.All("UnLinked you to a gui");
    }
    public boolean isGUILinked (Player p) {
        return guis.containsKey(p.getUniqueId());
    }

    public GeneralGUI GetGUI (Player p) {
        return guis.get(p.getUniqueId());
    }

    @Override
    public void run() {
        for (int i = 0; i < guis.size(); i++) {
            GeneralGUI gui = guis.values().toArray(new GeneralGUI[guis.size()])[i];

//            Messanger.InfoConsole("Updating GUI.");
            gui.Update();

        }
    }
}
