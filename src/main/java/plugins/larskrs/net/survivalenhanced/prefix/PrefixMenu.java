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
import plugins.larskrs.net.survivalenhanced.gui.DynamicContentGUI;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefixMenu extends DynamicContentGUI {


    private Player holder;
    private Inventory inv;
    private Prefix playerPrefix;

    public PrefixMenu ( Player player ) {
        super(
                6,
                "Prefix > Select Prefix"
        );

        this.holder = player;
        playerPrefix = PrefixManager.getInstance().GetPrefix(player);

        AddPrefixItems ();
    }

    private void AddPrefixItems() {


        for (Prefix prefix : PrefixManager.getInstance().GetPrefixes()) {

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

            RegisterItemStack(item);
        }

    }

}
