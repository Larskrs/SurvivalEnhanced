package plugins.larskrs.net.survivalenhanced.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Messanger {




    public static void Console (String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
    }
    public static void ErrorConsole(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR: " + msg);
    }

}
