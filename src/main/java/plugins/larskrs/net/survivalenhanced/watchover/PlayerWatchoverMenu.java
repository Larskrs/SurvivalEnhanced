package plugins.larskrs.net.survivalenhanced.watchover;

import com.sun.jna.platform.win32.Sspi;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import plugins.larskrs.net.survivalenhanced.gui.DynamicContentGUI;
import plugins.larskrs.net.survivalenhanced.gui.InventoryGUI;
import plugins.larskrs.net.survivalenhanced.location.LocationManager;
import plugins.larskrs.net.survivalenhanced.location.StoredLocation;
import plugins.larskrs.net.survivalenhanced.location.StoredLocationMenu;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;
import plugins.larskrs.net.survivalenhanced.tools.TimeUtil;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerWatchoverMenu extends DynamicContentGUI {

    private boolean showOffline;
    private List<StoredLocation> locations;

    public PlayerWatchoverMenu(int page) {
        super("TeleportToPlayerMenu", page, 5, "Watchover - Online: " + Bukkit.getOnlinePlayers().size());

        locations = new ArrayList<>();
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            StoredLocation sl = WatchoverModule.getLastLocation(p.getUniqueId());
            if (sl == null) continue;
            locations.remove(p.getUniqueId());
            locations.add(sl);
        }

        Comparator<StoredLocation> reverseComparator = (c1, c2) -> {
            if (c1 == null && c2 == null) return 0;
            if (c1 == null && c2 != null) return 1;
            if (c1 != null && c2 == null) return -1;
            return c1.getCreatedAt().compareTo(c2.getCreatedAt());
        };
        Collections.sort(locations, reverseComparator);
        Collections.reverse(locations);

        showOffline = false;
    }

    @Override
    public void onItemsRender() {


        for (StoredLocation sl : locations
             ) {
            if (sl == null) continue;
            OfflinePlayer p = Bukkit.getOfflinePlayer(sl.getPlayer());

            if (showOffline || p.isOnline()) {
                RegisterPlayer(p, p.isOnline());
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

                    lore.add(ChatColor.AQUA+ "Left Click " + ChatColor.GRAY + (showOffline ? "To Hide Offline Players" : "To Show Offline Players"));

        meta.setLore(lore);

        item.setItemMeta(meta);
        getInventory().setItem((getRows() * 9) - 5, item);
    }

    private void RegisterPlayer(OfflinePlayer p, boolean isOnline) {
        StoredLocation[] storedLocations = LocationManager.GetPlayerLocations(p.getUniqueId());
        StoredLocation lastLoc = null;
        if (storedLocations != null && storedLocations.length > 0) {
            lastLoc = storedLocations[storedLocations.length - 1];
        }

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwningPlayer(p);
        meta.setLocalizedName(p.getUniqueId().toString());

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
            if (lastLoc != null) {
                Timestamp last_online = lastLoc.getCreatedAt();
                if (isOnline) lore.add(ChatColor.YELLOW + "Was online " + ChatColor.GRAY + TimeUtil.getRelativeTime(last_online.getTime()));
                else lore.add(ChatColor.YELLOW + "Was last online " + ChatColor.GRAY + TimeUtil.getRelativeTime(last_online.getTime()));
            }

        if (isOnline)   lore.add(ChatColor.AQUA+ "Left Click " + ChatColor.GRAY + "To Watchover " + p.getName());
        if (isOnline)   lore.add(ChatColor.AQUA+ "Right Click " + ChatColor.GRAY + "Inspect inventory.");
        if (isOnline)   lore.add(ChatColor.AQUA+ "Q " + ChatColor.GRAY + "Teleport player to you.");


        if (lastLoc != null) {
            lore.add(ChatColor.AQUA+ "Ctrl + Q " + ChatColor.GRAY + "To teleport last location. " + ChatColor.GRAY + "( " + lastLoc.getChange().name()+ " " + lastLoc.getId() + " )");
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



        if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("toggle_offline_players")) {

            showOffline = !showOffline;
            Update();

            return;
        }

        String localName = item.getItemMeta().getLocalizedName();
        if (localName == null || localName == "") {
            return;
        }
        Player target = Bukkit.getPlayer(UUID.fromString(localName));

        if (action.equals(InventoryAction.PICKUP_ALL)) {  // Left Click

            TeleportAction (item, p);
            return;
        }

        if (action.equals(InventoryAction.PICKUP_HALF)) { // Right Click

            InspectGUI(item, p);
            return;
        }

        if (Arrays.asList(InventoryAction.DROP_ONE_CURSOR, InventoryAction.DROP_ONE_SLOT)
                .contains(action))
        {
            if (target == null || !target.isOnline()) {
                return;
            }
            target.teleport(p);
            p.sendMessage(ChatColor.GREEN + "Teleported " + target.getName() +" to you!");
            if (!WatchoverModule.isVanished(p.getUniqueId())) {
                target.sendMessage(ChatColor.GREEN + "You were teleported to " + p.getName() + "!");
            }

            return;
        }

        if (Arrays.asList(InventoryAction.DROP_ALL_CURSOR, InventoryAction.DROP_ALL_SLOT)
                .contains(action))
        {
                StoredLocations (item, p);
                return;

        }


    }

    private void StoredLocations (ItemStack item, Player p) {
        if (!item.hasItemMeta()) {
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(item.getItemMeta().getLocalizedName()));
        if (target == null) {
            return;
        }

        p.closeInventory();
        new StoredLocationMenu(1, target, LocationManager.GetPlayerLocations(target.getUniqueId())).OpenGUI(p);
    }


    private void TeleportAction(ItemStack item, Player p) {

        String localName = item.getItemMeta().getLocalizedName();

        if (!item.hasItemMeta()) {
            return;
        }
        Player target = Bukkit.getPlayer(UUID.fromString(localName));
        if (target == null) {
            return;
        }
        Messanger.InfoConsole("TEST 3");

        p.teleport(target);
        p.closeInventory();

        p.setSpectatorTarget(target);
    }

    private void InspectGUI (ItemStack item, Player p) {

        String localName = item.getItemMeta().getLocalizedName();

        if (!item.hasItemMeta()) {
            return;
        }
        Player target = Bukkit.getPlayer(UUID.fromString(localName));
        if (target == null) {
            return;
        }
        p.closeInventory();
        new InventoryGUI(target).OpenGUI(p);
    }

}
