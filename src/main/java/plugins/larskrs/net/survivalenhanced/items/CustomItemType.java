package plugins.larskrs.net.survivalenhanced.items;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public enum CustomItemType {
    HAT ("Hat"),
    WEAPON ("Weapon"),
    TOOL ("Tool"),
    ;

    public final String name;

    private CustomItemType (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



}
