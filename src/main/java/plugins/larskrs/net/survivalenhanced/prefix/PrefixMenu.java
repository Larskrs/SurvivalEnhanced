package plugins.larskrs.net.survivalenhanced.prefix;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefixMenu {


    private Player holder;
    private Inventory inv;
    private Prefix playerPrefix;

    public PrefixMenu ( Player player ) {
        this.holder = player;
        this.inv = Bukkit.createInventory(holder, 5 * 9, "Prefix > Select Prefix");
        playerPrefix = PrefixManager.getInstance().GetPrefix(player);

        AddBorderItems ();
        AddPrefixItems ();


        player.openInventory(inv);
    }

    private void AddPrefixItems() {

        for (int i = 0; i < PrefixManager.getInstance().GetPrefixes().size(); i++) {
            Prefix prefix = PrefixManager.getInstance().GetPrefixes().get(i);
            int id = i + 9;


            ItemStack item = new ItemStack(prefix.GetIcon());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(prefix.GetDisplay() + ChatColor.WHITE + " (" + prefix.GetName() + ")");
            meta.setLocalizedName(prefix.GetName());
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);


            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.AQUA + "[ Click ] " + ChatColor.WHITE + "to set your prefix.");
            lore.add("   " + prefix.GetDisplay() + ChatColor.GRAY + " | " + ChatColor.WHITE + holder.getName());

                if (playerPrefix != null && playerPrefix.GetName().equals(meta.getLocalizedName())) {

                meta.addEnchant(Enchantment.DURABILITY, 1, false);
                lore.add(" ");
                lore.add(ChatColor.GREEN + "   selected");
                lore.add(" ");

                }

            meta.setLore(lore);


            item.setItemMeta(meta);

            inv.setItem(id, item);
        }

    }

    private void AddBorderItems() {
        for (int i = 0; i < inv.getSize() / 9; i ++) {

            int lowLine = inv.getSize() / 9 - 1;
            List<Integer> lines = Arrays.asList(0, lowLine);

            if (!lines.contains(i)) {
                continue;
            }

            ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = pane.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + " ");
            pane.setItemMeta(meta);

            for (int j = 0; j < 9; j++) {
                inv.setItem(i * 9 + j, pane);
            }

        }
    }

}
