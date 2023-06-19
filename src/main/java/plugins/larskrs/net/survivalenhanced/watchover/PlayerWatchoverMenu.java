package plugins.larskrs.net.survivalenhanced.watchover;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import plugins.larskrs.net.survivalenhanced.gui.DynamicContentGUI;
import plugins.larskrs.net.survivalenhanced.gui.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

public class PlayerWatchoverMenu extends DynamicContentGUI {


    public PlayerWatchoverMenu(int page) {
        super("TeleportToPlayerMenu", page, 5, "Teleport to player");
    }

    @Override
    public void onItemsRender() {
        for (Player p : Bukkit.getOnlinePlayers()) {

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwningPlayer(p);
            meta.setLocalizedName(p.getName());

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.setDisplayName(ChatColor.YELLOW + p.getName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.AQUA+ "[ Left Click ] " + ChatColor.YELLOW + "To teleport.");
            lore.add(ChatColor.AQUA+ "[ Right Click ] " + ChatColor.YELLOW + "Inspect inventory.");

            meta.setLore(lore);

            skull.setItemMeta(meta);
            RegisterItemStack(skull);

        }
    }

    @Override
    public void onItemClick(int slotId, ItemStack item, Player p, InventoryAction action,  InventoryType type) {

        if (action.equals(InventoryAction.PICKUP_ALL)) {  // Left Click

            TeleportAction (item, p);

        }

        if (action.equals(InventoryAction.PICKUP_HALF)) { // Right Click

            InspectGUI(item, p);

        }



    }

    private void TeleportAction(ItemStack item, Player p) {
        if (!item.hasItemMeta()) {
            return;
        }
        Player target = Bukkit.getPlayer(item.getItemMeta().getLocalizedName());
        if (target == null) {
            return;
        }

        p.teleport(target);
        p.closeInventory();
    }

    private void InspectGUI (ItemStack item, Player p) {
        if (!item.hasItemMeta()) {
            return;
        }
        Player target = Bukkit.getPlayer(item.getItemMeta().getLocalizedName());
        if (target == null) {
            return;
        }
        p.closeInventory();
        new InventoryGUI(target).OpenGUI(p);
    }

}
