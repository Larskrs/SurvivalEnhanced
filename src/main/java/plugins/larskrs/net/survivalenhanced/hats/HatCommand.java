package plugins.larskrs.net.survivalenhanced.hats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;

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

            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage: /hat give <hat>");
                return;
            }

            ItemRarity rarity = ItemRarity.GetRandomRarity();

            HatItem hat = HatModule.GetHat(args[1]);
            player.getInventory().addItem(hat.getItem(rarity));
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> options = new ArrayList<>();

        if (args.length > 1 && args[0].equalsIgnoreCase("give")) {
            for (HatItem h : HatModule.GetHats()
            ) {
                options.add(h.getIdentity());
            }

            return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
        }

        return null;
    }
}
