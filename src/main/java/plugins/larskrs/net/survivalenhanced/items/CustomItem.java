package plugins.larskrs.net.survivalenhanced.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import plugins.larskrs.net.survivalenhanced.hats.HatItem;

import java.util.UUID;

public abstract class CustomItem {


    private String base;
    private String description;
    private CustomItemType type;
    public ItemRarity rarity;
    private String id;

    public CustomItem (
        String base,
        String description,
        CustomItemType type,
        String id
    )
    {
        this.base = base;
        this.description = description;
        this.type = type;
        this.id = id;
    }

    public void SetRarity (ItemRarity rarity) {
        this.rarity = rarity;
    }

    public ItemStack UpdateItem (String data) {
        return new ItemStack(Material.STICK);
    }

    public ItemStack CreateItem (ItemRarity rarity) {
        return new ItemStack(Material.STICK);
    }

    public String GetLocalizedFormat (ItemRarity rarity) {
        return (
                "custom_item" + "," +
                GetID() + "," + // Item ID
                rarity.ordinal() + "," + // Rarity ID
                0 // Format ID
        );
    }

    public CustomItemType GetType() {
        return type;
    }

    public String GetID() {
        return id;
    }
}
