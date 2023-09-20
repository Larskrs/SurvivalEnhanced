package plugins.larskrs.net.survivalenhanced.hats;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import plugins.larskrs.net.survivalenhanced.items.ItemRarity;
import plugins.larskrs.net.survivalenhanced.tools.ColorTool;

import java.util.List;

public class HatItem {

    private Material type;
    private String identity;
    private String display;
    private List<String> lore;
    private int model;
    private Color color;
    private Biome[] biomes;

    public HatItem (String identity, String display, List<String> lore, int model, Color color) {
        type = Material.LEATHER_HORSE_ARMOR;
        this.identity = identity;
        this.display = display;
        this.lore = lore;
        this.model = model;
        this.color = color;
    }
    public HatItem (Material type, String identity, String display, List<String> lore, int model, Color color, Biome[] biomes) {
        this.type = type;
        this.identity = identity;
        this.display = display;
        this.lore = lore;
        this.model = model;
        this.color = color;
        this.biomes = biomes;
    }



    public ItemStack getItem(ItemRarity rarity) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setLocalizedName("custom_item="+identity+","+"equippable_hat");
        meta.setCustomModelData(model);
        StringBuilder builder = new StringBuilder();
        builder.append(rarity.color.toString());
        net.md_5.bungee.api.ChatColor rarityColor = net.md_5.bungee.api.ChatColor.of(ColorTool.toHex(rarity.color.asRGB()));
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display) + " " + rarityColor + "("+rarity.display+ rarityColor + ")");

        if (type.equals(Material.LEATHER_HORSE_ARMOR)) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            leatherArmorMeta.setColor(rarity.color);
            meta.setLore(lore);
            item.setItemMeta(leatherArmorMeta);
            return item;
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;

    }


    public String getIdentity() {
        return identity;
    }

    public String getDisplay() {
        return display;
    }

    public Biome[] getBiomes() { return biomes; }
}
