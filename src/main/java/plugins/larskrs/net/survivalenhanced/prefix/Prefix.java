package plugins.larskrs.net.survivalenhanced.prefix;

import org.bukkit.Color;
import org.bukkit.Material;

public class Prefix {

    private String display;
    private String name;
    private Material material;

    public Prefix (String display, String name, Material material) {
        this.display = display;
        this.name = name;
        this.material = material;
    }

    public String GetName() {
        return name;
    }
    public String GetDisplay() {
        return display;
    }
    public Material GetIcon() {
        return material;
    }
}
