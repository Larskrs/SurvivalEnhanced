package plugins.larskrs.net.survivalenhanced.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.tools.LocationTools;

import java.util.ArrayList;
import java.util.List;

public class RandomTPCommand extends Command {

    public RandomTPCommand () {
        super(
                "randomteleport",
                "survivalenhanced.command.rtp",
                "Teleport to a random location.",
                "spawnmob",
                (new String[]{"rtp", "randomtp"})
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player target = null;
        if (!(sender instanceof Player)) {
            if (args.length > 1) {
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid Usage: There is not player with the name of: " + args[1]);
                    return;
                }
            }
        } else {
            target = (Player) sender;
        }
        World world = target.getWorld();
        Location loc = LocationTools.getRandomLocation(world, -1000, 1000);
        target.teleport(loc);

        sender.sendMessage(ChatColor.GREEN + "Randomly Teleported to: " +
                        loc.getBlockX() + " " +
                        loc.getBlockY() + " " +
                        loc.getBlockZ() + " )"
        );


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
