package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.Arrays;
import java.util.List;

public class SteedListener implements Listener {

    @EventHandler
    public void OnEntityDeath (EntityDeathEvent e) {


        // This is a lot more efficient way to do it, we skip the getSteed method if the entity is not a possible steed.
        List<EntityType> entityTypeList = Arrays.asList(EntityType.HORSE, EntityType.MULE, EntityType.DONKEY, EntityType.CAMEL);
        if (!(entityTypeList.contains(e.getEntity().getType()))) {
            return; }

        Steed steed = SteedManager.getInstance().GetSteed(e.getEntity());
        if (steed == null) {
            return;
        }
        steed.KillSteed();
        Bukkit.getConsoleSender().sendMessage("Steed killed");
    }

    @EventHandler
    public void OnEntityUnload (EntitiesUnloadEvent e) {
        for (Entity ent : e.getEntities()
             ) {

            Steed steed = SteedManager.getInstance().GetSteed(ent);
            if (steed == null ) { continue; }
            Messanger.InfoConsole("Unloading a steed with name of : " + steed.custom_name);
            steed.SaveLastLocation(ent.getLocation());

        }
    }


}

