package plugins.larskrs.net.survivalenhanced.hats;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.Random;

public enum ItemRarity {
    UNUSUAL (
            Color.fromRGB(10040115),
            "Unusual",
            2F
    ),
    LEGENDARY (
            Color.fromRGB(14188339),
            "Legendary",
            18F
    ),
    EPIC (
            Color.fromRGB(8339378),
            "Epic",
            35F
    ),
    RARE (
            Color.fromRGB(3361970),
            "Rare",
            60F
    ),
    NORMAL (
            Color.fromRGB(8375321),
            "Normal",
            100F
    ),
    ;

    public final Color color;
    public final String display;
    public final float percentage;


    public static ItemRarity GetRandomRarity () {

        int sum = 0;
        for (ItemRarity rarity : values()
             ) {
            sum += rarity.percentage;
        }

        Random r = new Random();
        Float f = r.nextFloat(1, sum);

        //      45
        // [4,10,52,23]

        // First we see if the percentage is above the lowest percentage, then continue if not, if we are at the end we return the value.

        for (int i = 0; i < values().length; i++) {
            ItemRarity rarity = ItemRarity.values()[i];
            if (f > rarity.percentage) {
                continue;
            }
            return rarity;
        }

        // NOTE: I did not want to fucking sort these values, so make sure they are sorted correctly. unusual has the lowest.
        return values()[values().length - 1];
    }

    private ItemRarity (
            Color color,
            String display,
            float percentage
    ) {
        this.color = color;
        this.display = display;
        this.percentage = percentage;
    }
}
