package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class SteedManageMenu extends SteedMenu {
    public SteedManageMenu(int i) {
        super(i);
    }

    @Override
    public void onClickedSteed(Steed steed, int slotId, ItemStack item, Player p, InventoryAction action, InventoryType type) {
        if (action.equals(InventoryAction.PICKUP_ALL)) {

        }
        if (action.equals(InventoryAction.PICKUP_HALF)) {
            p.sendMessage(ChatColor.GRAY + "Setting " + steed.custom_name + " as your main steed.");
            SteedModule.getInstance().SetMainSteed(p, steed);


            RefreshItems();
        }
    }

}
