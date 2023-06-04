package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.Interaction;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

public class SteedAddInteraction extends Interaction {

    public Entity target;
    public String horseName;

    public SteedAddInteraction(Player holder, String horseName) {
        super(holder);
        holder.sendMessage(ChatColor.GREEN + "Selecting Horse...");
        holder.sendMessage(ChatColor.YELLOW + "Right Click on the horse you want to select.");
        this.horseName = horseName;
    }

    @Override
    public void onInteractEntity(Entity entity) {

        if (entity.getType() != EntityType.HORSE) {
            holder.sendMessage(ChatColor.RED + "Wrong mob!");
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
