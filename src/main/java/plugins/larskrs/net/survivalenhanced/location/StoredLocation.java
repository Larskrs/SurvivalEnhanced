package plugins.larskrs.net.survivalenhanced.location;

import org.bukkit.Location;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class StoredLocation {

    int id;
    UUID player;
    Location location;
    LocationChange change;
    Timestamp created_at;

    public StoredLocation (int id, UUID player, Location location, LocationChange change, Timestamp created_at) {
        this.id = id;
        this.player = player;
        this.location = location;
        this.change = change;
        this.created_at = created_at;
    }
    public StoredLocation (UUID player, Location location, LocationChange change, Timestamp created_at) {
        this.player = player;
        this.location = location;
        this.change = change;
        this.created_at = created_at;
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
    public Timestamp getCreatedAt() {
        return this.created_at;
    }
}
