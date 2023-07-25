package plugins.larskrs.net.survivalenhanced.skull;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.Material;
import plugins.larskrs.net.survivalenhanced.skull.SkullEnum;
import plugins.larskrs.net.survivalenhanced.steed.Steed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class SkullTool {

    public static ItemStack getSteedSkull(Steed steed) {

        String texture = "";

        if (steed.type.equals(EntityType.HORSE)) {
            texture = SkullEnum.valueOf(steed.horse_color.toString() + "_HORSE").texture;
        } else {
            texture = SkullEnum.valueOf(steed.type.toString()).texture;
        }


        ItemStack head = getSkull(texture);
        return head;

    }

    public static ItemStack getSkull(String url) {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) return head;

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace();
        }

        head.setItemMeta(skullMeta);
        return head;
    }

}
