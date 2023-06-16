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

    private Inventory inv;
    private Prefix playerPrefix;

    public PrefixMenu ( int page) {
        super(
                "prefix-menu",
                page,
                6,
                "Prefix > Select Prefix"
        );
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
            lore.add("   " + prefix.GetDisplay() + ChatColor.GRAY + " | " + ChatColor.WHITE + getPlayer().getName());

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

    @Override
    public void onItemClick(ItemStack item, Player p) {
        Messanger.InfoConsole(item.getItemMeta().getDisplayName());

        String localizedName = item.getItemMeta().getLocalizedName();
        if (localizedName == null) {
            return;
        }

        Prefix prefix = PrefixManager.getInstance().GetPrefix(localizedName);

        if (prefix == null) {
            return;
        }


        p.performCommand("prefix set " + localizedName);

        PrefixMenu menu = new PrefixMenu(getPage());
        menu.OpenGUI(p);


    }

    @Override
    public void onItemsRender() {
        Messanger.InfoConsole(getPlayer().getName() + " - is here in prefix menu");
        playerPrefix = PrefixManager.getInstance().GetPrefix(getPlayer());
        AddPrefixItems();
    }

}
