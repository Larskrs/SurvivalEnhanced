package plugins.larskrs.net.survivalenhanced.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.location.LocationTools;
import plugins.larskrs.net.survivalenhanced.tools.NumberTools;

import java.util.ArrayList;
import java.util.List;

public class SpawnMobCommand extends Command {

    public SpawnMobCommand() {
        super(
                "spawnmob",
                "survivalenhanced.command.spawnmob",
                "Spawn any mob.",
                "spawnmob"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: You have to set an , /EntityType <EntityType> <amount>");
            return;
        }

        Player target = null;
        if (!(sender instanceof Player)) {
            if (args.length > 2) {
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid Usage: There is not player with the name of: " + args[1]);
                    return;
                }
            }
        } else {
            target = (Player) sender;
        }

        EntityType entityType = null;
        if (args.length > 0) {
            entityType = EntityType.valueOf(args[0].toUpperCase());
            if (entityType == null) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage: There is no Entity with the name of: " + args[0]);
                return;
            }
        }

        int amount = 1; // Default spawn amount
        if (args.length > 1) {
            if (NumberTools.isNumeric(args[1])) {
                amount = Integer.parseInt(args[1]);
            }
        }

        Location location = LocationTools.getTargetBlock(target, 25).getLocation().add(0.5D, 1D, 0.5D);
        for (int i = 0; i < amount; i++) {
            target.getWorld().spawnEntity(
                    location,
                    entityType
            );
        }

        sender.sendMessage(
                ChatColor.GREEN + "Spawned " + amount + " " + entityType.name().toLowerCase() +  "'s "
        );


    }


    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {


        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            for (EntityType type : EntityType.values()
            ) {
                options.add(type.name().toLowerCase());
            }

            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }

        return null;
    }
}
