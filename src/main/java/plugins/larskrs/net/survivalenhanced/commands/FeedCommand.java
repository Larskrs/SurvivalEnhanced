package plugins.larskrs.net.survivalenhanced.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;

import java.util.ArrayList;
import java.util.List;

public class FeedCommand extends Command {



    public FeedCommand () {

        super(
                "feed",
                "survivalenhanced.command.feed",
                "Feed players",
                "feed"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player target = null;
        if (args.length > 0)
        {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Could not find " + args[0]);
                return;
            }
        }
        if (sender instanceof Player && target == null) {
            target = (Player) sender;
        }
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: /feed <player> ");
            return;
        }

        target.setFoodLevel(20);
        sender.sendMessage(ChatColor.GREEN + (target.equals(sender)
                ? "You fed yourself!"
                : "You fed " + target.getName() + "!"));
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {

        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            for (Player pl : Bukkit.getOnlinePlayers()
            ) {
                options.add(pl.getName());
            }

            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }

        return null;
    }
}
