package plugins.larskrs.net.survivalenhanced.location;

import org.bukkit.Material;

public enum LocationChange {
    QUIT_GAME (Material.OAK_SIGN),
    DEATH (Material.SKELETON_SKULL),
    TELEPORT (Material.ENDER_PEARL),
    SPECIAL (Material.NETHER_STAR);


    public final Material icon;

    private LocationChange (Material icon) {
        this.icon = icon;
    }
}
