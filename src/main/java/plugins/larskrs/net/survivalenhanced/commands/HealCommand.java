package plugins.larskrs.net.survivalenhanced.commands;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.watchover.WatchoverModule;

import java.util.ArrayList;
import java.util.List;

public class HealCommand extends Command {
    public HealCommand () {

        super(
                "heal",
                "survivalenhanced.command.heal",
                "Heal players",
                "heal"
            );
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = null;
        if (!(sender instanceof Player)) {
            if (args.length > 0) {
                target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid Usage: There is not player with the name of: " + args[1]);
                    return;
                }
            }
        } else {
            target = (Player) sender;
        }

        target.setHealth(target.getMaxHealth());
        sender.sendMessage(ChatColor.GREEN + "You healed " +
                (sender instanceof Player
                ? "yourself."
                : target.getName() + "!"
                ));


        if (sender instanceof Player) {
            if (!WatchoverModule.isVanished(((Player) sender).getUniqueId())
                    && (!(sender instanceof Player))) {
                target.sendMessage(ChatColor.GREEN + "You have been healed by " + ((Player) sender).getName());
            }

        }
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
