package plugins.larskrs.net.survivalenhanced.motd;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.tools.InventoryTool;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.io.IOException;
import java.util.List;

public class MotdCommand extends Command {

    public MotdCommand () {

        super(
                "motd",
                "survivalenhanced.command.motd",
                "Message of the day",
                "motd"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        MotdMenu menu = new MotdMenu(args[0]);
        menu.OpenGUI(player);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
