package plugins.larskrs.net.survivalenhanced.stable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.UUID;

public class Stable {

    private Location centerPoint;
    private String name;
    private UUID owner;


    public Stable (String name, UUID owner, Location centerPoint) {
            this.centerPoint = centerPoint;
            this.name = name;
            this.owner = owner;
    }

    public String getName () {
        return name;
    }
    public Location getLocation () {
        return centerPoint;
    }
    public UUID getOwner () {
        return owner;
    }

    public void setLocation(Location location) {
        centerPoint = location;
    }
}
