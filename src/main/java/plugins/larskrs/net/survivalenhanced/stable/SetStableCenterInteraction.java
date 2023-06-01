package plugins.larskrs.net.survivalenhanced.stable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.Interaction;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

public class SetStableCenterInteraction extends Interaction {


    public SetStableCenterInteraction(Player holder) {
        super(holder);

        holder.sendMessage(ChatColor.GREEN + "Setting stable station...");
        holder.sendMessage(ChatColor.YELLOW + "Right Click on the block you want your stable to be interacted with.");

    }

    @Override
    public void onInteractEntity(Entity entity) {

    }

    @Override
    public void onInteractBlock(Block block) {



        Stable stable = SurvivalEnhanced.GetStableManager().GetStable(holder.getUniqueId());
        SurvivalEnhanced.GetStableManager().SetStableCenter(stable.getName(), block.getLocation());

        holder.sendMessage(ChatColor.GREEN + "Set the center of the stable.");


    }
}
