package plugins.larskrs.net.survivalenhanced.location;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import plugins.larskrs.net.survivalenhanced.gui.DynamicContentGUI;
import plugins.larskrs.net.survivalenhanced.gui.InventoryGUI;
import plugins.larskrs.net.survivalenhanced.tools.TimeUtil;
import plugins.larskrs.net.survivalenhanced.watchover.WatchoverModule;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class StoredLocationMenu extends DynamicContentGUI {

    private boolean showOffline;
    private Player target;
    StoredLocation[] locations = null;

    public StoredLocationMenu(int page, Player target) {
        super("StoredLocationMenu", page, 5, target.getName() + "'s locations");
        this.target = target;
        showOffline = false;

        locations = LocationManager.GetPlayerLocations(target.getUniqueId());
        Collections.reverse(Arrays.asList(locations));
    }

    @Override
    public void onItemsRender() {


        for (StoredLocation stored : locations)
        {
            RegisterStoredLocation (stored);
        }

    }

    private void RegisterStoredLocation(StoredLocation stored) {
        ItemStack item = new ItemStack(stored.change.icon);
        ItemMeta meta = item.getItemMeta();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = new Date(stored.getCreatedAt().getTime());
        long startTime = System.currentTimeMillis();
        String timeAgo = TimeUtil.getRelativeTime(start.getTime());

        meta.setLocalizedName(stored.getId() + "");
        meta.setDisplayName(stored.getId() + " - " + ChatColor.YELLOW + timeAgo + ". - " + stored.getChange().name());

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<String> lore = new ArrayList<>();

            lore.add(ChatColor.DARK_GRAY + "Position: ( " +
                    stored.getLocation().getBlockX() + " " +
                    stored.getLocation().getBlockY() + " " +
                    stored.getLocation().getBlockZ() + "  " +
                    getCardinalDirection(stored.getLocation()) + " )"
            );


        lore.add(ChatColor.YELLOW + timeAgo + ".");
        lore.add(ChatColor.YELLOW + "Cause: " + stored.getChange().name());
        meta.setLore(lore);
        item.setItemMeta(meta);
        RegisterItemStack(item);
    }

    @Override
    public void onToolsRender() {

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
                    player.getLocation().getBlockZ() + " ) "
            );

        }
        if (isOnline)   lore.add(ChatColor.AQUA+ "[ Left Click ] " + ChatColor.YELLOW + "To Watchover " + p.getName());
        if (isOnline)   lore.add(ChatColor.AQUA+ "[ Right Click ] " + ChatColor.YELLOW + "Inspect inventory.");
        if (isOnline)   lore.add(ChatColor.AQUA+ "[ Q ] " + ChatColor.YELLOW + "Teleport player to you.");


        if (lastLoc != null) {
            lore.add(ChatColor.AQUA+ "[ Ctrl + Q ] " + ChatColor.YELLOW + "To teleport last location. " + ChatColor.GRAY + "( " + lastLoc.getChange().name()+ " " + lastLoc.getId() + " )");
        }

        meta.setLore(lore);

        skull.setItemMeta(meta);
        RegisterItemStack(skull);
    }

    public String getCardinalDirection(Location location) {

        double rotation = (location.getYaw() - 90.0F) % 360.0F;

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

        if (!type.equals(InventoryType.CHEST)) {
            return;
        }

        int id = Integer.parseInt(item.getItemMeta().getLocalizedName());
        StoredLocation clicked = Arrays.stream(locations).filter(l -> l.getId() == id).collect(Collectors.toList()).get(0);

        p.teleport(clicked.getLocation());


    }

}
