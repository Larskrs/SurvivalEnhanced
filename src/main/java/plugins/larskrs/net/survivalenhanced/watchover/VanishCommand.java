package plugins.larskrs.net.survivalenhanced.watchover;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.general.Command;

import java.util.List;

public class VanishCommand extends Command {

    public VanishCommand(SurvivalEnhanced survivalEnhanced) {
        super(
                "vanish",
                "survivalenhanced.command.vanish",
                "Hides you from normal players, but not other staff members.",
                "vanish",
                (new String[]{"v"})

        );
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: only players can run this command!");
            return ; }

        Player p = (Player) sender;

        WatchoverModule.isVanished(p.getUniqueId());

        if (WatchoverModule.isVanished(p.getUniqueId())) {
            Vanish(p, args);
        } else {
            UnVanish(p, args);
        }


    }

    private void UnVanish(Player p, String[] args) {
    }

    private void Vanish(Player p, String[] args) {
    }

    public Player getPlayer (String str) {
        for (Player p : Bukkit.getOnlinePlayers()
        ) {
            if (str.equalsIgnoreCase(p.getName())) {
                return p;
            }
        }
        return null;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
