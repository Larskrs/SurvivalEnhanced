package plugins.larskrs.net.survivalenhanced.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomItemManager implements Listener {

    public static List<CustomItem> items;
    public static CustomItem[] GetCustomItem (CustomItemType type) {
        return items.stream().filter((i) -> type.equals(i.GetType())).collect(Collectors.toList()).toArray(items.toArray(new CustomItem[0]));
    }
    public static void RegisterItem(CustomHatItem item) {
        for (CustomItem i : items
             ) {
            if (i.GetID().equals(item.GetID())) {

                Messanger.ErrorConsole("Replaced Item with same id: (" + item.GetType().getName() + ") " + item.GetID().toString()); ;
            }
        }
        items.add(item);
        Messanger.SuccessConsole("Registered item: (" + item.GetType().getName() + ") " + item.GetID().toString());
    }

    public static void Setup() {
        items = new ArrayList<>();
    }

    @EventHandler
    public void onItemMove (InventoryOpenEvent e) {
        for (int index = 0; index < e.getInventory().getContents().length; index++) {

        ItemStack i = e.getInventory().getContents()[index];

        if (i == null) { continue; }
        if (!i.hasItemMeta()) { continue; }
        if (!i.getItemMeta().hasLocalizedName()) { continue; }
        if (!i.getItemMeta().getLocalizedName().contains("custom_item")) { return; }

        String[] lName = i.getItemMeta().getLocalizedName().split(",");
        String itemType = lName[0]; // Should be Custom Item, if not we skip.
        String id = lName[1];
        ItemRarity rarity = ItemRarity.values()[Integer.parseInt(lName[2])];
        int formatVersion = Integer.parseInt(lName[3]);
        for (CustomItem customItem : items
             ) {
            if (!customItem.GetID().equals(id)) { continue; }
            ItemStack item = customItem.CreateItem(rarity);

            e.getInventory().setItem(index, item);
        }
        }
    }

    public static boolean isCustom (ItemStack i) {
        if (i == null) { return false; }
        if (!i.hasItemMeta()) { return false; }
        if (!i.getItemMeta().hasLocalizedName()) { return false; }
        if (!i.getItemMeta().getLocalizedName().contains("custom_item")) { return false; }

        return true;
    }
    public static CustomItem GetCustomItem (ItemStack i) {

        if (!isCustom(i)) { return null; }

        String[] lName = i.getItemMeta().getLocalizedName().split(",");
        String itemType = lName[0]; // Should be Custom Item, if not we skip.
        String id = lName[1];
        ItemRarity rarity = ItemRarity.values()[Integer.parseInt(lName[2])];
        int formatVersion = Integer.parseInt(lName[2]);
        for (CustomItem customItem : items
        ) {
            if (!customItem.GetID().equals(id)) { continue; }
            return customItem;
        }
        return null;
    }
    public static ItemStack GetUpdatedVersion (ItemStack i) {
        CustomItem customItem = GetCustomItem (i);
        if (customItem == null) { return null; }
        String[] lName = i.getItemMeta().getLocalizedName().split(",");
        ItemRarity rarity = ItemRarity.values()[Integer.parseInt(lName[2])];
        return customItem.CreateItem(rarity);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        for (int index = 0; index < e.getPlayer().getInventory().getContents().length; index++) {

            ItemStack i = e.getPlayer().getInventory().getContents()[index];

            if (i == null) { continue; }
            if (!i.hasItemMeta()) { continue; }
            if (!i.getItemMeta().hasLocalizedName()) { continue; }
            if (!i.getItemMeta().getLocalizedName().contains("custom_item")) { return; }

            String[] lName = i.getItemMeta().getLocalizedName().split(",");
            String itemType = lName[0]; // Should be Custom Item, if not we skip.
            String id = lName[1];
            ItemRarity rarity = ItemRarity.values()[Integer.parseInt(lName[2])];
            int formatVersion = Integer.parseInt(lName[2]);
            for (CustomItem customItem : items
            ) {
                if (!customItem.GetID().equals(id)) { continue; }
                ItemStack item = customItem.CreateItem(rarity);

                e.getPlayer().getInventory().setItem(index, item);
            }
        }
    }
}
