package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class StaticContentGUI extends GeneralGUI {

    private Inventory inv;

    public StaticContentGUI(String id, int rows) {
        super(
           id,
           rows
        );
    }


    public abstract void onRenderItems();


    public abstract void onItemClick(int slotId, ItemStack item, Player p, InventoryAction action, InventoryType type);
    @Override
    public Inventory getInventory () {
        return this.inv;
    }
}
