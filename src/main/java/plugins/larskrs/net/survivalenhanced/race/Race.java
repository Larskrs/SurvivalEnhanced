package plugins.larskrs.net.survivalenhanced.race;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public abstract class Race {

    private String raceID;
    private ItemStack icon;

    public abstract Location getRaceSpawnOrigin ();


    public String getID() {
        return raceID;
    }
}
