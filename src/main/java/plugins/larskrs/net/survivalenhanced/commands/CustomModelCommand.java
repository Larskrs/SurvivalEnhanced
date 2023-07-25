package plugins.larskrs.net.survivalenhanced.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.watchover.WatchoverModule;

import java.util.ArrayList;
import java.util.List;

public class CustomModelCommand extends Command {
    public CustomModelCommand() {

        super(
                "custommodel",
                "survivalenhanced.command.custommodel",
                "Give items with custom model data",
                "heal"
            );
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = null;
        if (!(sender instanceof Player)) {
            if (args.length > 2) {
                target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid Usage: There is not player with the name of: " + args[1]);
                    return;
                }
            }
        } else {
            target = (Player) sender;
        }

        ItemStack item = new ItemStack(Material.valueOf(args[0]));
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(Integer.parseInt(args[1]));
        item.setItemMeta(meta);

        target.getInventory().addItem(item);
        target.sendMessage("Gave you a custom item.");
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
