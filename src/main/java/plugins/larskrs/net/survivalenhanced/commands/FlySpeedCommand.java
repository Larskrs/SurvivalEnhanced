package plugins.larskrs.net.survivalenhanced.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.tools.NumberTools;

import java.util.List;

public class FlySpeedCommand extends Command {

    public FlySpeedCommand () {

        super(
                "flyspeed",
                "survivalenhanced.command.flyspeed",
                "Set flight speed",
                ""
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {


        float speed = 2f; // Remember to divide by 10 for it to work with vanilla implementation.

        if (args.length > 0) {
            if (!NumberTools.isNumeric(args[0])) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage: /flyspeed <speed:number> <player>");
                return;
            }
            speed = Float.parseFloat(args[0]);
        }

        Player target = null;
        if (args.length > 1)
        {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Could not find " + args[1]);
                return;
            }
        }
        if (sender instanceof Player && target == null) {
            target = (Player) sender;
        }
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: /flyspeed <speed> <player> ");
            return;
        }

        target.setFlySpeed(speed / 10);
        sender.sendMessage(ChatColor.YELLOW + "Changed flyspeed for " + ChatColor.DARK_AQUA + target.getName() + " to " + ChatColor.DARK_AQUA + speed);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
