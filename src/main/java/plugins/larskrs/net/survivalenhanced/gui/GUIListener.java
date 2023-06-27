package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixMenu;

import java.util.Arrays;

public class GUIListener implements Listener {

    @EventHandler
    public void CloseInventory (InventoryCloseEvent e) {

        Player p = (Player) e.getPlayer();

        if (GUIManagar.getInstance().isGUILinked(p)) {
            GUIManagar.getInstance().UnlinkGUI(p);
        }
    }

    InventoryAction[] allowedPlayerAction = {

            InventoryAction.PICKUP_ALL,
            InventoryAction.PICKUP_HALF
    };

    @EventHandler
    public void OnInventoryInteract (InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if (!GUIManagar.getInstance().isGUILinked(p)) {
            return;
        }
        GeneralGUI gui = GUIManagar.getInstance().GetGUI(p);


        e.setCancelled(true);

        if (e.getCurrentItem() == null) {
            return;
        }

        ItemMeta meta = e.getCurrentItem().getItemMeta();

        if (gui instanceof DynamicContentGUI) {


            if (meta.getDisplayName().equals(ChatColor.AQUA + "Next Page") || meta.getDisplayName().equals(ChatColor.AQUA + "Last Page")) {
                int page;
                page = Integer.parseInt(meta.getLocalizedName());

                ((DynamicContentGUI) gui).SetPage(page);
                gui.OpenGUI(p);

                return;
            }
        }
        gui.InteractItem(e.getSlot(), e.getCurrentItem(), p, e.getAction(), e.getClickedInventory().getType());



    }

}
