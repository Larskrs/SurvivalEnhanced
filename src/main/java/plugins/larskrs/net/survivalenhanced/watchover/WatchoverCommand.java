package plugins.larskrs.net.survivalenhanced.watchover;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.quest.QuestModule;

import java.util.List;
import java.util.UUID;

public class WatchoverCommand extends Command {

    public WatchoverCommand(SurvivalEnhanced survivalEnhanced) {
        super(
                "watchover",
                "survivalenhanced.command.watchover",
                "This command will let you manage your players",
                "prefix",
                (new String[]{"wo"})

        );
    }

    private void ShowPlayers (CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) { return ; }

        Player p = (Player) sender;

        new PlayerWatchoverMenu(1).OpenGUI(p);




    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: only players can run this command!");
            return ; }

        Player p = (Player) sender;

        WatchoverModule.isVanished(p.getUniqueId());

        if (args.length <= 0) {
            ShowPlayers(sender, args);
            return;
        }
        if (args.length == 1) {
            Player target = getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Could not find a player with the name of: " + args[0]);
                return;
            }
            WatchoverModule.WatchoverPlayer(p, target);

        }

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
