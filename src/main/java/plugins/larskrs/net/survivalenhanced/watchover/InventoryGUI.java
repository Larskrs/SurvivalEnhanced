package plugins.larskrs.net.survivalenhanced.watchover;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plugins.larskrs.net.survivalenhanced.gui.GUIPageUtil;
import plugins.larskrs.net.survivalenhanced.gui.GeneralGUI;

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
    public void onItemClick(ItemStack item, Player p, InventoryAction action) {



    }
}
