package plugins.larskrs.net.survivalenhanced.hats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.general.Module;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class HatModule extends Module implements Listener {

    private YamlConfiguration config;
    private static List<HatItem> hats;

    public HatModule() {
        super("HatModule");
    }

    public static HatItem GetHat(String identity) {
        for (HatItem h : hats
             ) {
            if (h.getIdentity().equals(identity)) {
                return h;
            }
        }
        return null;
    }

    public static HatItem[] GetHats() {
        return hats.toArray(new HatItem[0]);
    }

    @Override
    public boolean onLoadModule() {

        hats = new ArrayList<>();

        Bukkit.getPluginManager().registerEvents(this, SurvivalEnhanced.getInstance());
        FileManager.getInstance().LoadFile("hats.yml");
        this.config = SurvivalEnhanced.GetFileManager().GetYamlFromString("hats.yml");


        LoadHats ();


        return false;
    }

    private void LoadHats() {
        hats.clear();
        for (String s : config.getConfigurationSection("hats").getKeys(false)
        ) {

            ConfigurationSection section = config.getConfigurationSection("hats." + s);

            HatItem hat = new HatItem(
                    Material.getMaterial(section.getString("type")),
                    s,
                    section.getString("display"),
                    section.getStringList("lore").stream().map((l) -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()),
                    section.getInt("model"),
                    Color.fromRGB(section.getInt("color"))

            );
            hats.add(hat);
        }
    }

    @Override
    public boolean onUnloadModule() {
        return false;
    }


    public float getFishingChance () {
        return (float) config.getDouble("fishing.chance", 5f);
    }


    public static HatItem GetRandomHat () {

        Random r = new Random();
        int index = r.nextInt(1, HatModule.GetHats().length + 1);
        return HatModule.GetHats()[index -1];
    }
    @EventHandler
    public void armourDispenser (BlockDispenseArmorEvent e) {
        if (!e.getItem().hasItemMeta()) return;

        ItemMeta meta = e.getItem().getItemMeta();
        if (!meta.hasLocalizedName()) return;

        if (!meta.getLocalizedName().equals("equippable_hat")) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void blockPlace (BlockPlaceEvent e) {
        if (e.getItemInHand() == null) return;
        if (!e.getItemInHand().hasItemMeta()) return;
        if (!e.getHand().equals(EquipmentSlot.HAND)) return;

        ItemMeta meta = e.getItemInHand().getItemMeta();
        if (!meta.hasLocalizedName()) return;

        if (!meta.getLocalizedName().equals("equippable_hat")) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract (InventoryClickEvent e) {
        if (!(e.getInventory() instanceof HorseInventory)) return;

        ItemStack item = e.getCurrentItem();
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLocalizedName()) return;

        if (!meta.getLocalizedName().equals("equippable_hat")) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {

        List<Action> allowed = Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

        if (!allowed.contains(e.getAction())) return;

        if (e.getItem() == null) return;
        if (!e.getItem().hasItemMeta()) return;
        if (!e.getHand().equals(EquipmentSlot.HAND)) return;

        ItemMeta meta = e.getItem().getItemMeta();
        if (!meta.hasLocalizedName()) return;

        if (!meta.getLocalizedName().equals("equippable_hat")) return;

        ItemStack helmet = e.getPlayer().getInventory().getHelmet();
        ItemStack hatItemStack = e.getItem();

        e.getPlayer().getInventory().removeItem(hatItemStack);
        e.getPlayer().getInventory().setHelmet(e.getItem());
        if (helmet != null) {
            e.getPlayer().getInventory().addItem(helmet);
        }
    }



    @EventHandler
    public void onFish(PlayerFishEvent event)  {
        if (event.getCaught() instanceof Item)   {
            Item stack = (Item) event.getCaught();
            float chance = getFishingChance();
            Random r = new Random();
            float pf = r.nextFloat(1, 100);

            // Messanger.InfoAll(pf + "-" + chance + "-");

            if (pf > chance) return;

            HatItem hat = GetRandomHat();
            ItemRarity rarity = ItemRarity.GetRandomRarity();
            stack.setItemStack(hat.getItem(rarity));

            event.getPlayer().sendMessage(ChatColor.YELLOW + "You found " + rarity.article + " " + rarity.getChatColor() + rarity.display + " " + ChatColor.AQUA + hat.getDisplay() + ChatColor.YELLOW );

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
