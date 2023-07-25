package plugins.larskrs.net.survivalenhanced.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.skull.SkullEnum;
import plugins.larskrs.net.survivalenhanced.tools.NumberTools;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand extends Command {

    public EnchantCommand () {
        super(
                "enchant",
                "survivalenhanced.command.enchant",
                "Enchant any gear in hand.",
                "enchant"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {



        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: You have to set an , /enchantment <enchantment> <level>");
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
        ItemStack item = target.getInventory().getItemInMainHand();

        if (item == null) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: " + (sender instanceof Player
                    ? "You are not holding anything to enchant."
                    : target.getName() + " is not holding anything to enchant."));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (args.length > 0 && args[0].equalsIgnoreCase("clear")) {

            for (Enchantment es : meta.getEnchants().keySet()
                 ) {
                meta.removeEnchant(es);
            }
            item.setItemMeta(meta);
            sender.sendMessage(
                    ChatColor.GREEN + "Removed Enchantments from your " +
                            (meta.hasDisplayName() ? meta.getDisplayName() : item.getType().name().toLowerCase())
            );
            return;
        }

        Enchantment enchant = null;
        if (args.length > 0) {
            enchant = Enchantment.getByName(args[0].toUpperCase());
            if (enchant == null) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage: There is no enchantment with the name of: " + args[0]);
                return;
            }
        }

        int level = enchant.getMaxLevel();
        if (args.length > 1) {
            if (NumberTools.isNumeric(args[1])) {
                level = Integer.parseInt(args[1]);
            }
        }

        meta.addEnchant(enchant, level, true);
        item.setItemMeta(meta);
        sender.sendMessage(
                ChatColor.GREEN + "Enchanted your " +
                        (meta.hasDisplayName() ? meta.getDisplayName() : item.getType().name().toLowerCase()) +
                        " with " + enchant.getName() + " " + NumberTools.intToRoman(level)
        );


    }


    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {


        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.add("clear");
            for (Enchantment en : Enchantment.values()
            ) {
                options.add(en.getName().toLowerCase());
            }

            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }
        Enchantment enchant;
        if (args.length > 1) {
            enchant = Enchantment.getByName(args[0].toUpperCase());
            if (enchant == null) {
                return null;
            }
            for (int i = 1; i < enchant.getMaxLevel(); i++) {
                options.add(String.valueOf(i));
            }
            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }


        return null;
    }
}
