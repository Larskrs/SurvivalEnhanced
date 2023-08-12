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
            sender.sendMessage(ChatColor.RED + "Invalid Usage: /heal <player> ");
            return;
        }

        target.setHealth(target.getMaxHealth());
        sender.sendMessage(ChatColor.GREEN + (target.equals(sender)
                ? "You healed " + target.getName() + "!"
                : "You healed yourself!"));
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