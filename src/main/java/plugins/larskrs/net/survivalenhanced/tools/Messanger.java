package plugins.larskrs.net.survivalenhanced.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messanger {




    public static void Console (String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
    }
    public static void ErrorConsole(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR: " + msg);
    }

    public static void InfoConsole(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "INFO: " + msg);
    }
    public static void InfoAll (String msg) {
        All(ChatColor.YELLOW + msg);
    }
    public static void ErrorAll (String msg) {
        All(ChatColor.RED + msg);
    }
    public static void All (String msg) {
        for (Player p : Bukkit.getOnlinePlayers()
             ) {
            p.sendMessage(msg);
        }
    }
}
