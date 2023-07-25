package plugins.larskrs.net.survivalenhanced.watchover;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import plugins.larskrs.net.survivalenhanced.gui.DynamicContentGUI;
import plugins.larskrs.net.survivalenhanced.gui.InventoryGUI;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerWatchoverMenu extends DynamicContentGUI {

    private boolean showOffline;

    public PlayerWatchoverMenu(int page) {
        super("TeleportToPlayerMenu", page, 5, "Watchover - Online: " + Bukkit.getOnlinePlayers().size());
        showOffline = false;
    }

    @Override
    public void onItemsRender() {

        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (p.isOnline())
                RegisterPlayer(p, Bukkit.getOnlinePlayers().contains(p));
        }
        if (showOffline) {

            for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                if (!p.isOnline())
                    RegisterPlayer(p, Bukkit.getOnlinePlayers().contains(p));
            }

        }

    }

    @Override
    public void onToolsRender() {
        RegisterToggleOfflineViewButton();
    }

    private void RegisterToggleOfflineViewButton() {

        ItemStack item = new ItemStack(
                showOffline ? Material.ENDER_PEARL : Material.ENDER_EYE
        );
        ItemMeta meta = item.getItemMeta();

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLocalizedName("toggle_offline_players");
        meta.setDisplayName(
                ChatColor.YELLOW +
                (showOffline ? "Hide Offline Players" : "Show Offline Players")
        );
        List<String> lore = new ArrayList<>();

                    lore.add(ChatColor.AQUA+ "[ Left Click ] " + ChatColor.YELLOW + (showOffline ? "To Hide Offline Players" : "To Show Offline Players"));

        meta.setLore(lore);

        item.setItemMeta(meta);
        getInventory().setItem((getRows() * 9) - 5, item);
    }

    private void RegisterPlayer(OfflinePlayer p, boolean isOnline) {
        Location lastLoc = WatchoverModule.getLastLocation(p.getUniqueId());

//            if (p.equals(getPlayer())) {
//                continue; // Skip this player.
//            }

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwningPlayer(p);
        meta.setLocalizedName(p.getName());

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(
                (isOnline ? ChatColor.GREEN : ChatColor.RED) +
                p.getName()
        );
        List<String> lore = new ArrayList<>();

        if (isOnline) {
            Player player = (Player) p;
            lore.add(ChatColor.DARK_GRAY + "Position: ( " +
                    player.getLocation().getBlockX() + " " +
                    player.getLocation().getBlockY() + " " +
                    player.getLocation().getBlockZ() + "  " +
                getCardinalDirection(player) + " )"
            );

        }
        if (isOnline)   lore.add(ChatColor.AQUA+ "[ Left Click ] " + ChatColor.YELLOW + "To Watchover " + p.getName());
        if (isOnline)   lore.add(ChatColor.AQUA+ "[ Right Click ] " + ChatColor.YELLOW + "Inspect inventory.");
        if (isOnline)   lore.add(ChatColor.AQUA+ "[ Q ] " + ChatColor.YELLOW + "Teleport player to you.");


        if (lastLoc != null) {
            lore.add(ChatColor.AQUA+ "[ Ctrl + Q ] " + ChatColor.YELLOW + "To teleport leave location. " + ChatColor.GRAY + "( " + lastLoc.getWorld().getName() + " )");
        }

        meta.setLore(lore);

        skull.setItemMeta(meta);
        RegisterItemStack(skull);
    }

    public String getCardinalDirection(Entity e) {

        double rotation = (e.getLocation().getYaw() - 90.0F) % 360.0F;

        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 45.0D))
            return "W ⬅";
        if ((45.0D <= rotation) && (rotation < 135.0D))
            return "N ⬆";
        if ((135.0D <= rotation) && (rotation < 225.0D))
            return "E ➡";
        if ((225.0D <= rotation) && (rotation < 315.0D))
            return "S ⬇";
        if ((315.0D <= rotation) && (rotation < 360.0D)) {
            return "W ⬅";
        }
        return null;
    }

    @Override
    public void onPageItemClick(int slotId, ItemStack item, Player p, InventoryAction action,  InventoryType type) {

        OfflinePlayer target = Bukkit.getOfflinePlayer(item.getItemMeta().getLocalizedName());


        if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("toggle_offline_players")) {

            showOffline = !showOffline;
            Update();

            return;
        }


        if (action.equals(InventoryAction.PICKUP_ALL)) {  // Left Click

            TeleportAction (item, p);

        }

        if (action.equals(InventoryAction.PICKUP_HALF)) { // Right Click

            InspectGUI(item, p);

        }

        if (Arrays.asList(InventoryAction.DROP_ONE_CURSOR, InventoryAction.DROP_ONE_SLOT)
                .contains(action))
        {
            if (target == null && target.isOnline()) {
                return;
            }
            ((Player) target).teleport(p);
            p.sendMessage(ChatColor.GREEN + "Teleported " + target.getName() +" to you!");
            if (!WatchoverModule.isVanished(p.getUniqueId())) {
                ((Player) target).sendMessage(ChatColor.GREEN + "You were teleported to " + p.getName() + "!");
            }
        }

        Location lastLoc = WatchoverModule.getLastLocation(target.getUniqueId());
        if (Arrays.asList(InventoryAction.DROP_ALL_CURSOR, InventoryAction.DROP_ALL_SLOT)
                .contains(action))
        {
            if (lastLoc != null) {
                p.teleport(lastLoc);
                p.sendMessage(ChatColor.GREEN + "Teleported to " + target.getName() +"'s last location.");
            }
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
