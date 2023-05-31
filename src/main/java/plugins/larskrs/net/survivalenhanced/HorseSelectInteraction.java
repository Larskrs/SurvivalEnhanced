package plugins.larskrs.net.survivalenhanced;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class HorseSelectInteraction extends Interaction {

    public Entity target;

    public HorseSelectInteraction(Player holder) {
        super(holder);
        holder.sendMessage(ChatColor.GREEN + "Selecting Horse...");
        holder.sendMessage(ChatColor.YELLOW + "Right Click on the horse you want to select.");
    }

    @Override
    public void onInteractEntity(Entity entity) {

        if (entity.getType() != EntityType.HORSE) {
            holder.sendMessage(ChatColor.RED + "Wrong mob!");
            return;
        }



        // Set horse.
        SurvivalEnhanced.GetHorseManager().AddHorse(holder.getUniqueId(), entity.getUniqueId());
        SurvivalEnhanced.GetHorseManager().GlowHorse(entity.getUniqueId());

        if (entity.getCustomName() == null) {
            holder.sendMessage(ChatColor.GREEN + "Changed your main horse.");
        } else {
            holder.sendMessage(ChatColor.GREEN + "Set " + ChatColor.AQUA + entity.getCustomName() + ChatColor.GREEN + " as your main horse.");
        }
    }

    @Override
    public void onInteractBlock(Block block) {

    }
}
