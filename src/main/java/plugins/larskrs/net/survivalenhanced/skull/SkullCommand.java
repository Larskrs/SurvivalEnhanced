package plugins.larskrs.net.survivalenhanced.skull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.prefix.Prefix;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixManager;

import java.util.ArrayList;
import java.util.List;

public class SkullCommand extends Command {

    public SkullCommand() {
        super(
                "skull",
                "survivalenhanced.command.skull",
                "This command will let you give and get skulls used by the plugin.",
                "skull"

        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player p;

        if (!(sender instanceof Player)) {
            if (!(args.length > 1)) {
                return;
            }

            p = Bukkit.getPlayer(args[1]);
            if (p == null) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage: There is not player with the name of: " + args[1]);
                return;
            }
        } else {
            // Player sent command.
            p = (Player) sender;
        }



        if (args.length > 0) { // Args.length == 0 or less

            for (SkullEnum se : SkullEnum.values()) {
                if (se.name().equalsIgnoreCase(args[0])) {
                    sender.sendMessage(ChatColor.GREEN + "Gave skull " + se.name() + " to player " + p.getName());
                    p.getInventory().addItem(SkullTool.getSkull(se.texture));
                }
            }


        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {


        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            for (SkullEnum se : SkullEnum.values()
            ) {
                options.add(se.name());
            }

            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }


        return null;
    }
}
