package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import plugins.larskrs.net.survivalenhanced.Interaction;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SteedAddInteraction extends Interaction {

    public Entity target;
    public String horseName;

    public SteedAddInteraction(Player holder, String horseName) {
        super(holder);
        holder.sendMessage(ChatColor.GREEN + "Selecting Steed...");
        holder.sendMessage(ChatColor.YELLOW + "Right Click on the horse/donkey/mule you want to select.");
        this.horseName = horseName;
    }

    @Override
    public void onInteractEntity(Entity entity) {

        List<EntityType> entityTypeList = Arrays.asList(EntityType.HORSE, EntityType.MULE, EntityType.DONKEY);

        if (!entityTypeList.contains(entity.getType())) {
            holder.sendMessage(ChatColor.RED + "Wrong mob!");
            return;
        }

        if (SteedManager.getInstance().isSteedTaken(entity)) {
            holder.sendMessage(ChatColor.RED + "Whoops! This steed is already taken. Don't worry, there are plenty of " + entity.getType().getName() + " in the sea.");
            return;
        }

        // Set horse.
        SteedManager.getInstance().AddSteed(holder.getUniqueId(), entity.getUniqueId(), horseName);
        SteedManager.getInstance().HighlightSteed(SteedManager.getInstance().GetSteed(holder));

        if (entity.getCustomName() == null) {
            holder.sendMessage(ChatColor.GREEN + "Changed your main steed.");
        } else {
            holder.sendMessage(ChatColor.GREEN + "Set " + ChatColor.AQUA + entity.getCustomName() + ChatColor.GREEN + " as your main horse.");
        }
    }

    @Override
    public void onInteractBlock(Block block) {

    }
}
