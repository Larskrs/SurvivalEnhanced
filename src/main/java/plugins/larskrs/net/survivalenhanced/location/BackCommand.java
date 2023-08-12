package plugins.larskrs.net.survivalenhanced.location;

import net.milkbowl.vault.chat.Chat;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.watchover.WatchoverModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BackCommand extends Command {
    public BackCommand () {

        super(
                "back",
                "survivalenhanced.command.back",
                "Back people",
                "back"
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
            sender.sendMessage(ChatColor.RED + "Invalid Usage: /back <player> ");
            return;
        }

        StoredLocation[] locations = LocationManager.GetPlayerLocations(target.getUniqueId());
        Collections.reverse(Arrays.asList(locations));
        if (locations.length <= 0) {
            sender.sendMessage(ChatColor.RED + target.getName() + "Has no where to go back to...");
            return;
        }
        target.teleport(locations[0].getLocation());


        sender.sendMessage(ChatColor.GREEN + (target.equals(sender)
                ? "You teleported " + target.getName() + " to their last known location!"
                : "You teleported to your last location"));
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