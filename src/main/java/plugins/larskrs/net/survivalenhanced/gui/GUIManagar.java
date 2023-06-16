package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixManager;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GUIManagar {

    HashMap<UUID, GeneralGUI> guis;
    private SurvivalEnhanced se;
    private static GUIManagar instance;

    public GUIManagar () {
    }

    public void Setup (SurvivalEnhanced se) {
        this.se = se;
        Bukkit.getPluginManager().registerEvents(new GUIListener(), se);
        guis = new HashMap<>();

        instance = this;
    }

    public static GUIManagar getInstance() {
        return instance;
    }


    public void LinkGUI (Player p, GeneralGUI gui) {
        guis.put(p.getUniqueId(),gui);
        Messanger.All("Linked you to a gui");
    }

    public void UnlinkGUI(Player p) {
        guis.remove(p.getUniqueId());
        Messanger.All("UnLinked you to a gui");
    }
    public boolean isGUILinked (Player p) {
        return guis.containsKey(p.getUniqueId());
    }

    public GeneralGUI GetGUI (Player p) {
        return guis.get(p.getUniqueId());
    }

}
