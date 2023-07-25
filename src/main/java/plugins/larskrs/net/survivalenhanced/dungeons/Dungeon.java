package plugins.larskrs.net.survivalenhanced.dungeons;

import org.bukkit.Location;

public class Dungeon {

    private Party party;
    private Location[] spawnpoints;
    private String name;

    public Dungeon (Location[] spawnpoints) {
        this.spawnpoints = spawnpoints;
    }


}
