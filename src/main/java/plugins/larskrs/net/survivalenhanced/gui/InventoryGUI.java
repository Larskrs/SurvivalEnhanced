package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plugins.larskrs.net.survivalenhanced.gui.GUIPageUtil;
import plugins.larskrs.net.survivalenhanced.gui.GeneralGUI;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

public class InventoryGUI extends GeneralGUI {

    private Player target;
    private Inventory inv;

    public InventoryGUI(Player target) {
        super("InventoryGUI", 6);

        this.target = target;
    }

    @Override
    public void onRenderGUI(Player p) {

        inv = Bukkit.createInventory(null, 6 * 9, target.getName() + "'s inventory");
        RenderPlayerItems();

        p.openInventory(inv);

    }

    private void RenderPlayerItems() {
        for (int i = 0; i < target.getInventory().getContents().length; i++) {

            ItemStack item = target.getInventory().getContents()[i];
            if (item == null) {
                continue;
            }

            inv.setItem(i, item);

        }
    }

    @Override
    public void onItemClick(int slotId,ItemStack item, Player p, InventoryAction action, InventoryType type) {
        if (type.equals(InventoryType.PLAYER)) {
            // Press on the viewers inventory, to give an item to the target.
            target.getInventory().addItem(item);
            p.getInventory().setItem(slotId, new ItemStack(Material.AIR));
        } else {
            target.getInventory().setItem(slotId, new ItemStack(Material.AIR));
            p.getInventory().addItem(item);
        }



    }

    @Override
    public void onRenderUpdate() {
        getInventory().clear();
        RenderPlayerItems();
    }

    @Override
    public Inventory getInventory () {
        return this.inv;
    }
}
