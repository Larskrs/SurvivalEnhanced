package plugins.larskrs.net.survivalenhanced.interaction;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class Interaction {

    public Interaction (Player holder) {
        this.holder = holder;
    }

    public abstract void onInteractEntity (Entity entity);
    public abstract void onInteractBlock (Block block);

    public Player holder;


}
