package plugins.larskrs.net.survivalenhanced.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

public class SpawnerListener implements Listener {

    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {

        if ( ! e.getBlock().getType().equals(Material.SPAWNER)) { return; }

        ItemStack itemUsed = e.getPlayer().getItemInHand();
        if (itemUsed == null) { return; }

        if (!itemUsed.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) { return; }

        e.setExpToDrop(0);
        Location spawnLocation = e.getBlock().getLocation();

        CreatureSpawner sb = (CreatureSpawner) e.getBlock().getState();

        ItemStack dropped_item = new ItemStack(Material.SPAWNER);
        BlockStateMeta meta = (BlockStateMeta) dropped_item.getItemMeta();
        CreatureSpawner dropped_spawner = (CreatureSpawner) meta.getBlockState();

        dropped_spawner.setSpawnedType(sb.getSpawnedType());
        meta.setBlockState(dropped_spawner);
        dropped_item.setItemMeta(meta);

        spawnLocation.getWorld().dropItemNaturally(spawnLocation, dropped_item);

    }

}
