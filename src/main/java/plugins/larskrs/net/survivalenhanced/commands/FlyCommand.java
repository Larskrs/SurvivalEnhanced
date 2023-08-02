package plugins.larskrs.net.survivalenhanced.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.general.Command;

import java.util.List;

public class FlyCommand extends Command {



    public FlyCommand () {

        super(
                "fly",
                "survivalenhanced.command.fly",
                "Toggle flight",
                "flytoggle"
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
            sender.sendMessage(ChatColor.RED + "Invalid Usage: /fly <player> ");
            return;
        }

        target.setAllowFlight(!target.getAllowFlight());
        sender.sendMessage(ChatColor.GREEN + target.getName() + (target.getAllowFlight()
                ? " is now flying."
                : " is no longer flying"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
