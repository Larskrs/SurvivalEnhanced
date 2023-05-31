package plugins.larskrs.net.survivalenhanced.stable;

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

}
