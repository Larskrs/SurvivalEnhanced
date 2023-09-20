package plugins.larskrs.net.survivalenhanced.items;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import plugins.larskrs.net.survivalenhanced.tools.ColorTool;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomHatItem extends CustomItem {

    private Material type;
    private String identity;
    private String display;
    private List<String> lore;
    private int model;
    private Biome[] biomes;

    public CustomHatItem  (
            // CustomItem
            String id,
            // CustomHatItem
            Material type,
            String display,
            List<String> lore,
            int model,
            Biome[] biomes) {
        super(
                "hat",
                "Hats can be worn and have multiple rarities",
                CustomItemType.HAT,
                id
        );

        this.type = type;
        this.identity = identity;
        this.display = display;
        this.lore = lore;

        this.model = model;
        this.biomes = biomes;
    }

    @Override
    public ItemStack CreateItem (ItemRarity rarity) {
        List<String> tempLore = new ArrayList<>(lore);

        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(model);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        StringBuilder builder = new StringBuilder();
        builder.append(rarity.color.toString());
        net.md_5.bungee.api.ChatColor rarityColor = net.md_5.bungee.api.ChatColor.of(ColorTool.toHex(rarity.color.asRGB()));
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display) + " " + rarityColor + "("+rarity.display+ rarityColor + ")");


        tempLore.add("");
        tempLore.add(ChatColor.DARK_AQUA + "Attributes");
        tempLore.add("");
        tempLore.add(ChatColor.DARK_GRAY + "     Hold this item in your hand");
        tempLore.add(ChatColor.DARK_GRAY + "     and right click to equip the hat.");
        tempLore.add(" ");

        if (type.equals(Material.LEATHER_HORSE_ARMOR)) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            leatherArmorMeta.setColor(rarity.color);
            leatherArmorMeta.setLocalizedName(
                    GetLocalizedFormat(rarity) + "," +
                    "equippable_hat"
            );

            leatherArmorMeta.setLore(tempLore);

            item.setItemMeta(leatherArmorMeta);
            return item;
        }
        meta.setLocalizedName(
                GetLocalizedFormat(rarity) + "," +
                "equippable_hat"
        );
        meta.setLore(tempLore);
        item.setItemMeta(meta);
        return item;

    }


    @Override
    public ItemStack UpdateItem (String data) {
        return CreateItem(rarity);
    }

    public Biome[] GetBiomes () {
        return biomes;
    }
    public String GetDisplay () {
        return display;
    }

}
