package plugins.larskrs.net.survivalenhanced.motd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plugins.larskrs.net.survivalenhanced.gui.StaticContentGUI;

public class MotdMenu extends StaticContentGUI {

    private String messageChar;
    private Inventory inv;

    public MotdMenu(String messageChar) {
        super("motd_menu", 1);
        this.messageChar = messageChar;
    }

    @Override
    public void onRenderGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.WHITE + messageChar);
        setInventory(inv);
        p.openInventory(inv);
        inv.clear();
    }

    @Override
    public void onRenderUpdate() {

    }

    @Override
    public void onRenderItems() {

    }

    @Override
    public void onItemClick(int slotId, ItemStack item, Player p, InventoryAction action, InventoryType type) {

    }
}
