package plugins.larskrs.net.survivalenhanced.hats;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.items.CustomHatItem;
import plugins.larskrs.net.survivalenhanced.items.ItemRarity;

import java.util.ArrayList;
import java.util.List;

public class HatCommand extends Command {


    public HatCommand() {
        super (
                "hat",
                "survivalenhanced.command.hat",
                "Manage your hats.",
                "hat"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;

        if (args.length > 0 && args[0].equalsIgnoreCase("give") && sender.hasPermission("survivalenhanced.command.hat.give")) {

            CustomHatItem hat = HatModule.GetRandomHat();
            ItemRarity rarity = ItemRarity.GetRandomRarity();
            if (args.length > 1) {
                hat = HatModule.GetHat(args[1]);
            }
            if (args.length > 2) {
                rarity = ItemRarity.valueOf(args[2]);
            }

            sender.sendMessage(ChatColor.YELLOW + "You gave yourself " + rarity.article + " " + rarity.getChatColor() + rarity.display + " " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', hat.GetDisplay()) + ChatColor.YELLOW );
            player.getInventory().addItem(hat.CreateItem(rarity));
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> options = new ArrayList<>();


        if (args.length == 0) {
            options.add("give");
            return StringUtil.copyPartialMatches(args[0 ], options, new ArrayList<>());
        }

        if (args.length > 1 && args[0].equalsIgnoreCase("give")) {
            for (CustomHatItem h : HatModule.GetHats()
            ) {
                options.add(h.GetID());
            }
            if (args.length > 2) {
                options.clear();
                for (ItemRarity r : ItemRarity.values()
                     ) {
                    options.add(r.name());
                }
                return StringUtil.copyPartialMatches(args[2], options, new ArrayList<>());
            }

            return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
        }

        return null;
    }
}
