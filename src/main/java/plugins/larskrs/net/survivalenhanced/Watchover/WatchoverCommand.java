package plugins.larskrs.net.survivalenhanced.Watchover;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class WatchoverCommand {






    private void ShowPlayers (CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) { return ; }

        // Is player
        Player player = (Player) sender;

        Inventory inv = Bukkit.createInventory(player, 54, "Summon Steed");

        ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = teleport.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Random Teleport");
        meta.setLore(Arrays.asList("Teleport to random player"));

    }
}
