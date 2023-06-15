package plugins.larskrs.net.survivalenhanced.prefix;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.dependencies.VaultDependency;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.List;

public class PrefixListener implements Listener {

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        VaultDependency.GetChat().setPlayerPrefix(e.getPlayer(),
                PrefixManager.getInstance().GetPrefix(e.getPlayer()).GetDisplay());
    }

    int page = 1;

    @EventHandler
    public void OnMenuInteract (InventoryClickEvent e) {

        if (!e.getView().getTitle().startsWith("Prefix > Select Prefix")) {
            return;
        }

        e.setCancelled(true);

        if (e.getAction().equals(InventoryAction.NOTHING)) {
            return;
        }

        ItemStack item = e.getCurrentItem();

        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        String name = meta.getLocalizedName();

        Player p = (Player) e.getWhoClicked();;

        if (meta.getDisplayName().equals(ChatColor.AQUA + "Next Page") || meta.getDisplayName().equals(ChatColor.AQUA + "Last Page")) {
            page = Integer.parseInt(name);

            PrefixMenu menu = new PrefixMenu(p);

            menu.OpenGUI(p, page);
            return;
        }


        Prefix prefix = PrefixManager.getInstance().GetPrefix(name);

        if (prefix == null) {
            return;
        }


        p.performCommand("prefix set " + name);

        PrefixMenu menu = new PrefixMenu(p);
        menu.OpenGUI(p, page);

    }

}
