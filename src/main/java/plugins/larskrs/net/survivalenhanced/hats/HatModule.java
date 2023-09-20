package plugins.larskrs.net.survivalenhanced.hats;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.general.Module;
import plugins.larskrs.net.survivalenhanced.items.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class HatModule extends Module implements Listener {

    private YamlConfiguration config;

    public HatModule() {
        super("HatModule");
    }

    public static CustomHatItem GetHat(String identity) {
        for (CustomHatItem h : Arrays.asList(GetHats())
             ) {
            if (h.GetID().equals(identity)) {
                return h;
            }
        }
        return null;
    }

    public static CustomHatItem[] GetHats() {
        return Arrays.stream(CustomItemManager.GetCustomItem(CustomItemType.HAT)).map((CustomItem h) -> (CustomHatItem) h).collect(Collectors.toList()).toArray(new CustomHatItem[]{});
    }

    @Override
    public boolean onLoadModule() {

        Bukkit.getPluginManager().registerEvents(this, SurvivalEnhanced.getInstance());
        FileManager.getInstance().LoadFile("hats.yml");
        this.config = SurvivalEnhanced.GetFileManager().GetYamlFromString("hats.yml");

        LoadHats ();

        return false;
    }

    private void LoadHats() {
        for (String s : config.getConfigurationSection("hats").getKeys(false)
        ) {

            ConfigurationSection section = config.getConfigurationSection("hats." + s);
            String biomeString = section.getString("biomes", "");
            Biome[] biomes = {};
            if (!biomeString.equals("")) {
                biomes = Arrays.stream(biomeString.split(",")).map(Biome::valueOf).toArray(Biome[]::new);
            }
            if (biomes.length == 0) {
                biomes = Biome.values();
            }


            CustomHatItem hat = new CustomHatItem(
                    s,
                    Material.getMaterial(section.getString("type")),
                    section.getString("display"),
                    section.getStringList("lore").stream().map((l) -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()),
                    section.getInt("model"),
                    biomes

            );

            CustomItemManager.RegisterItem(hat);
        }
    }

    public CustomHatItem[] getHatsInBiome (Biome biome) {
        return Arrays.stream(GetHats()).filter((CustomHatItem h) -> Arrays.stream(h.GetBiomes()).collect(Collectors.toList()).contains(biome)).toArray(CustomHatItem[]::new);
    }

    @Override
    public boolean onUnloadModule() {
        return false;
    }


    public float getFishingChance () {
        return (float) config.getDouble("fishing.chance", 5f);
    }


    public static CustomHatItem GetRandomHat (CustomHatItem[] hats) {

        if (hats == null || hats.length == 0) {
            return null;
        }

        Random r = new Random();
        int index = r.nextInt(0, hats.length);
        return hats[index];
    }

    public static CustomHatItem GetRandomHat () {

        Random r = new Random();
        int index = r.nextInt(1, HatModule.GetHats().length + 1);
        return HatModule.GetHats()[index -1];
    }
    @EventHandler
    public void armourDispenser (BlockDispenseArmorEvent e) {
        if (!e.getItem().hasItemMeta()) return;

        ItemMeta meta = e.getItem().getItemMeta();
        if (!meta.hasLocalizedName()) return;

        if (!meta.getLocalizedName().contains("equippable_hat")) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void blockPlace (BlockPlaceEvent e) {
        if (e.getItemInHand() == null) return;
        if (!e.getItemInHand().hasItemMeta()) return;
        if (!e.getHand().equals(EquipmentSlot.HAND)) return;

        ItemMeta meta = e.getItemInHand().getItemMeta();
        if (!meta.hasLocalizedName()) return;

        if (!meta.getLocalizedName().contains("equippable_hat")) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract (InventoryClickEvent e) {
        if (!(e.getInventory() instanceof HorseInventory)) return;

        ItemStack item = e.getCurrentItem();
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLocalizedName()) return;

        if (!meta.getLocalizedName().contains("equippable_hat")) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {

        List<Action> allowed = Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

        if (e.getPlayer().isSneaking()) { return; }

        if (!allowed.contains(e.getAction())) return;

        if (e.getItem() == null) return;
        if (!e.getItem().hasItemMeta()) return;
        if (!e.getHand().equals(EquipmentSlot.HAND)) return;

        ItemMeta meta = e.getItem().getItemMeta();
        if (!meta.hasLocalizedName()) return;

        if (!meta.getLocalizedName().contains("equippable_hat")) return;

        ItemStack helmet = e.getPlayer().getInventory().getHelmet();
        ItemStack hatItemStack = e.getItem();

        e.getPlayer().getInventory().removeItem(hatItemStack);
        e.getPlayer().getInventory().setHelmet(e.getItem());
        if (helmet != null) {
            e.getPlayer().getInventory().addItem(helmet);
        }

        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
    }



    @EventHandler
    public void onFish(PlayerFishEvent event)  {
        if (event.getCaught() instanceof Item)   {
            Item stack = (Item) event.getCaught();
            float chance = getFishingChance();
            Random r = new Random();
            float pf = r.nextFloat(1, 100);

            if (pf > chance) return;

            Biome b = event.getPlayer().getLocation().getBlock().getBiome();
            CustomHatItem[] _hats = getHatsInBiome(b);

            CustomHatItem hat = GetRandomHat(_hats);
            ItemRarity rarity = ItemRarity.GetRandomRarity();
            stack.setItemStack(hat.CreateItem(rarity));

            event.getPlayer().sendMessage(ChatColor.YELLOW + "You found " + rarity.article + " " + rarity.getChatColor() + rarity.display + " " + ChatColor.AQUA + hat.GetDisplay() + ChatColor.YELLOW );

        }
    }

    @Override
    public boolean onReloadModule () {
        FileManager.getInstance().LoadFile("hats.yml");
        config = FileManager.getInstance().GetYamlFromString("hats.yml");
        LoadHats();
        return false;
    }


}
