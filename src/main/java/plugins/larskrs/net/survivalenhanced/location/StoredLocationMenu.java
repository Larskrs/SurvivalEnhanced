package plugins.larskrs.net.survivalenhanced.location;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import plugins.larskrs.net.survivalenhanced.gui.DynamicContentGUI;
import plugins.larskrs.net.survivalenhanced.tools.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class StoredLocationMenu extends DynamicContentGUI {

    private boolean showOffline;
    private OfflinePlayer target;
    StoredLocation[] locations = null;

    public StoredLocationMenu(int page, OfflinePlayer target, StoredLocation[] locations) {
        super("StoredLocationMenu", page, 5, target.getName() + "'s locations");
        this.target = target;
        showOffline = false;

        this.locations = locations;
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

        String timeAgo = TimeUtil.getRelativeTime(stored.getCreatedAt().getTime());

        meta.setLocalizedName(stored.getId() + "");
        meta.setDisplayName(ChatColor.GREEN + stored.getChange().name() + " " + "[ " + stored.getId() + " ]");

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<String> lore = new ArrayList<>();

            lore.add(ChatColor.DARK_GRAY + "" +
                    stored.getLocation().getBlockX() + " " +
                    stored.getLocation().getBlockY() + " " +
                    stored.getLocation().getBlockZ() + "  " +
                    stored.getLocation().getWorld().getName() + " )"
            );

        lore.add(ChatColor.AQUA + "Left Click " + ChatColor.GRAY + "to teleport to location.");
        lore.add(ChatColor.GRAY + "Created " + timeAgo);
        meta.setLore(lore);
        item.setItemMeta(meta);
        RegisterItemStack(item);
    }

    @Override
    public void onToolsRender() {

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

        String localName = item.getItemMeta().getLocalizedName();
        if (localName == null || localName == "") {
            return;
        }

        int id = Integer.parseInt(item.getItemMeta().getLocalizedName());
        StoredLocation clicked = Arrays.stream(locations).filter(l -> l.getId() == id).collect(Collectors.toList()).get(0);

        p.teleport(clicked.getLocation());


    }

}
