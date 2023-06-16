package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

public class GUIListener implements Listener {

    @EventHandler
    public void CloseInventory (InventoryCloseEvent e) {

        Player p = (Player) e.getPlayer();

        if (GUIManagar.getInstance().isGUILinked(p)) {
            GUIManagar.getInstance().UnlinkGUI(p);
        }
    }


    @EventHandler
    public void OnInventoryInteract (InventoryInteractEvent e) {

        Player p = (Player) e.getWhoClicked();

        if (GUIManagar.getInstance().isGUILinked(p)) {
            p.sendMessage("You linked");
            return;
        }

        GeneralGUI gui = GUIManagar.getInstance().GetGUI(p);


        e.setCancelled(true);

        gui.InteractItem(e.getView().getCursor());
    }

}
