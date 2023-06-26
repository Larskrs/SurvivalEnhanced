package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.interaction.Interaction;

import java.util.Arrays;
import java.util.List;

public class SteedAddInteraction extends Interaction {

    public Entity target;
    public String horseName;

    public SteedAddInteraction(Player holder, String horseName) {
        super(holder);
        holder.sendMessage(ChatColor.GREEN + "Selecting Steed...");
        holder.sendMessage(ChatColor.YELLOW + "Right Click on the horse/donkey/mule/camel you want to select.");
        this.horseName = horseName;
    }

    @Override
    public void onInteractEntity(Entity entity) {

        List<EntityType> entityTypeList = Arrays.asList(EntityType.HORSE, EntityType.MULE, EntityType.DONKEY, EntityType.CAMEL);

        if (!entityTypeList.contains(entity.getType())) {
            holder.sendMessage(ChatColor.RED + "Wrong mob!");
            return;
        }

        if (SteedModule.getInstance().isSteedTaken(entity)) {
            holder.sendMessage(ChatColor.RED + "Whoops! This steed is already taken. Don't worry, there are plenty of " + entity.getType().getName() + " in the sea.");
            return;
        }

        // Set horse.
        SteedModule.getInstance().AddSteed(holder.getUniqueId(), entity.getUniqueId(), horseName);
        SteedModule.getInstance().HighlightSteed(SteedModule.getInstance().GetSteed(holder));

        if (entity.getCustomName() == null) {
            holder.sendMessage(ChatColor.GREEN + "Changed your main " + entity.getType().name() + ". ");
        } else {
            holder.sendMessage(ChatColor.GREEN + "Set " + ChatColor.AQUA + entity.getCustomName() + ChatColor.GREEN + " as your main " + entity.getType().name() + ". ");
        }
    }

    @Override
    public void onInteractBlock(Block block) {

    }
}
