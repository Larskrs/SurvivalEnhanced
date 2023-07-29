package plugins.larskrs.net.survivalenhanced.location;

import org.bukkit.Location;

import java.util.UUID;

public class StoredLocation {

    int id;
    UUID player;
    Location location;
    LocationChange change;

    public StoredLocation (int id, UUID player, Location location, LocationChange change) {
        this.id = id;
        this.player = player;
        this.location = location;
        this.change = change;
    }
    public StoredLocation (UUID player, Location location, LocationChange change) {
        this.id = -1;
        this.player = player;
        this.location = location;
        this.change = change;
    }

    public int getId() {
        return this.id;
    }
    public UUID getPlayer() {
        return this.player;
    }
    public Location getLocation() {
        return this.location;
    }
    public LocationChange getChange() {
        return this.change;
    }
}
