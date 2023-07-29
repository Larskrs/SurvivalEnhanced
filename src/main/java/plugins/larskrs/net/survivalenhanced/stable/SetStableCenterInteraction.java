package plugins.larskrs.net.survivalenhanced.stable;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.interaction.Interaction;

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



        Stable stable = StableModule.getInstance().GetStable(holder.getUniqueId());
        StableModule.getInstance().SetStableCenter(stable.getName(), block.getLocation());

//        holder.sendMessage(ChatColor.YELLOW + LocationTools.StringifyLocation(block.getLocation()));

        holder.sendMessage(ChatColor.GREEN + "Set the center of the stable.");


    }
}
