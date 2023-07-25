package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.gui.DynamicContentGUI;
import plugins.larskrs.net.survivalenhanced.skull.SkullTool;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SteedMenu extends DynamicContentGUI {



    public SteedMenu(int page) {
        super("SteedMenu", page, 3, "Select Steed");
    }


    @Override
    public void onItemsRender() {
        for (Steed steed : SteedModule.getInstance().GetSteedList()
             ) {

            if (!steed.owner_id.equals(getPlayer().getUniqueId())) {
                continue;
            }

            if (steed.isAlive) {
                AddSteedItem(steed);
            }
        }
        for (Steed steed : SteedModule.getInstance().GetSteedList()
        ) {

            if (!steed.owner_id.equals(getPlayer().getUniqueId())) {
                continue;
            }

            if (!steed.isAlive) {
                AddSteedItem(steed);
            }
        }
    }

    private void AddSteedItem(Steed steed) {
        ItemStack item = SkullTool.getSteedSkull(steed);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.YELLOW + steed.custom_name);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "  Type: " + steed.type + " " + (steed.type.equals(EntityType.HORSE) ? steed.horse_color.toString() : ""));
        lore.add(ChatColor.GRAY + "  Owner: " + Bukkit.getOfflinePlayer(steed.owner_id).getName());
        lore.add((steed.isAlive ? ChatColor.GREEN + "  Is alive." : ChatColor.RED + "  Is Dead."));


        //identity
        meta.setLocalizedName(steed.uuid.toString());

        Steed mainSteed = SteedModule.getInstance().GetMainSteed(Bukkit.getPlayer(steed.owner_id));


        if (mainSteed != null && mainSteed.equals(steed)) {

            lore.add(" ");
            lore.add(ChatColor.GREEN + "  This is your main steed.");
            lore.add(" ");
            meta.addEnchant(Enchantment.DURABILITY, 1, true);

        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        RegisterItemStack(item);
    }

    @Override
    public void onPageItemClick(int slotId, ItemStack item, Player p, InventoryAction action, InventoryType type) {
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLocalizedName()) { return; }
        UUID uuid = UUID.fromString(meta.getLocalizedName());
        Steed steed = SteedModule.getInstance().GetSteed(uuid);
        if (steed == null) {
            return;
        }
        // We clicked on a steed.
        onClickedSteed(steed, slotId, item, p, action, type);
    }

    public abstract void onClickedSteed (Steed steed, int slotId, ItemStack item, Player p, InventoryAction action, InventoryType type);
}
