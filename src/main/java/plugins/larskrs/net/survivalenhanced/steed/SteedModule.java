package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.general.Module;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.*;

public class SteedModule extends Module {

    List<Steed> steeds;
    public static SteedModule instance;

    public YamlConfiguration steedConfig;
    public HashMap<UUID, Steed> mainSteed;

    public SteedModule() {
        super("SteedModule");
    }

    @Override
    public boolean onLoadModule() {
        instance = this;
        FileManager.getInstance().LoadFile("steed.yml");
        steedConfig = FileManager.getInstance().GetYamlFromString("steed.yml");
        SetDefaultConfigValues();

        if (!steedConfig.getBoolean("enabled")) {
            return true;
        }

        steeds = LoadSteedsFromFile();
        LoadMainSteedsFromFile();

        new SteedCommand(SurvivalEnhanced.getInstance());
        Bukkit.getPluginManager().registerEvents(new SteedListener(), SurvivalEnhanced.getInstance());

        return false;
    }

    @Override
    public boolean onReloadModule() {
        FileManager.getInstance().LoadFile("steed.yml");
        steedConfig = FileManager.getInstance().GetYamlFromString("steed.yml");
        steeds = LoadSteedsFromFile();
        LoadMainSteedsFromFile();
        return false;
    }

    @Override
    public boolean onUnloadModule() {
        return false;
    }

    public static SteedModule getInstance() {
        return instance;
    }

//  |------------------------------|
//  |        Get Steed Methods     |
//  |------------------------------|

    public Steed GetSteed (Entity entity) {
        for (Steed steed : steeds)
            {
            if (steed.entity_id.equals(entity.getUniqueId())) {
                return steed;
            }
        }
        return null;
    }

    public Steed GetSteed (Player owner) {
        for (Steed steed : steeds)
        {
            if (steed.owner_id.equals(owner.getUniqueId())) {
                return steed;
            }
        }
        return null;
    }
    public Steed GetMainSteed (Player owner) {

        return mainSteed.get(owner.getUniqueId());
    }

    private void GetNewMainSteed(Player owner) {

        Steed steed = GetSteed(owner);
        if (steed == null || !steed.isAlive) {
            owner.sendMessage(ChatColor.RED + "You don't have any spare steeds.");
            return;
        }


        owner.sendMessage(ChatColor.YELLOW + "Set " + steed.custom_name + " as your main steed.");
        SetMainSteed(owner, steed);
    }

    public boolean isSteedTaken (Entity entity) {
        return (GetSteed(entity) != null);
    }

//  |------------------------------|
//  |        Serialize Steeds      |
//  |------------------------------|

    public void LoadMainSteedsFromFile () {
        mainSteed = new HashMap<>();

        if (!steedConfig.contains("steeds")) {
            Messanger.ErrorConsole("No main steeds set in steed.yml");
            return;
        }
        for (String s : steedConfig.getStringList("main")) {
            Steed steed = GetSteed(UUID.fromString(s));
            mainSteed.put(steed.owner_id, steed);
        }

    }

    public List<Steed> LoadSteedsFromFile () {

        ArrayList<Steed> steeds = new ArrayList<>();

        if (!steedConfig.contains("steeds")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "No steeds found in steed.yml");
            return steeds;
        }


        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "-- Horses Loaded --");
        for (String s : steedConfig.getConfigurationSection("steeds").getKeys(false))
        {
            ConfigurationSection section = steedConfig.getConfigurationSection("steeds." + s);
            UUID steed_id = UUID.fromString(s);
            UUID owner_id = UUID.fromString(section.getString("owner"));
            UUID entity_id = UUID.fromString(section.getString("entity"));
            EntityType type = EntityType.valueOf(section.getString("type"));

            Steed steed = new Steed(entity_id, owner_id, steed_id, type);
            steed.SetSpeed(      (float)  section.getDouble("speed"));
            steed.SetJump(       (float)  section.getDouble("jump"));
            steed.SetMaxHealth(  (float)  section.getDouble("max_health"));
            steed.SetCustomName(          section.getString("custom_name"));
            steed.SetAlive(               section.getBoolean("alive"));

            if (steed.type.equals(EntityType.HORSE)) {
                steed.SetStyle(Horse.Style.valueOf(section.getString("style")));
                steed.SetHorseColor(Horse.Color.valueOf(section.getString("horse_color")));
            }


            steeds.add(steed);
            Bukkit.getConsoleSender().sendMessage("   " +  ChatColor.GRAY + " - " + section.getString("custom_name") + "   " + (steed.isAlive ? ChatColor.GREEN + "Alive " : ChatColor.RED + "Dead  "));
        }
        return steeds;

    }
    private void SetDefaultConfigValues () {
        if (!steedConfig.contains("settings.max-call-distance")) {
            steedConfig.set("settings.max-call-distance", 50);
        }
        if (!steedConfig.contains("settings.glow-duration")) {
            steedConfig.set("settings.glow-duration", 5);
        }
        if (!steedConfig.contains("enabled")) {
            steedConfig.set("enabled", true);
        }


        FileManager.getInstance().SaveData("steed.yml");


    }

    public void StoreSteed (Steed steed) {

        steedConfig.set("steeds." + steed.uuid.toString() + ".stored", true);
        FileManager.getInstance().SaveData("steed.yml");

    }
    public void UnStoreSteed (Steed steed) {

        steedConfig.set("steeds." + steed.uuid.toString() + ".stored", false);
        FileManager.getInstance().SaveData("steed.yml");

    }
    public void AddSteed(UUID owner_id, UUID entity_id, String horseName) {

        Entity entity =  Bukkit.getEntity(entity_id);

        List<EntityType> entityTypeList = Arrays.asList(EntityType.HORSE, EntityType.MULE, EntityType.DONKEY, EntityType.CAMEL);

        if (!(entityTypeList.contains(entity.getType()))) {
            return; }

        entity.setCustomName(horseName);

        Player player = Bukkit.getPlayer(owner_id);
        if (player == null) {
            Bukkit.getConsoleSender().sendMessage("Not player");
            return; }


        // Crate Steed
        Steed steed = null;
        if (entity.getType().equals(EntityType.HORSE)) {
            Horse horse = (Horse) entity;
            steed = new Steed(horse, owner_id, UUID.randomUUID());
        }
        if (entity.getType().equals(EntityType.DONKEY)) {
            Donkey donkey = (Donkey) entity;
            steed = new Steed(donkey, owner_id, UUID.randomUUID());
        }
        if (entity.getType().equals(EntityType.MULE)) {
            Mule mule = (Mule) entity;
            steed = new Steed(mule, owner_id, UUID.randomUUID());
        }
        if (entity.getType().equals(EntityType.CAMEL)) {
            Camel camel = (Camel) entity;
            steed = new Steed(camel, owner_id, UUID.randomUUID());
        }

//        if (GetSteed(player) != null) {
//            RemoveSteed(GetSteed(player));
//        }
        steeds.add(steed);


        SaveSteed(steed);
    }

    private void SaveSteed(Steed steed) {

        ConfigurationSection section = steedConfig.createSection("steeds." + steed.uuid.toString());
        section.set("owner", steed.owner_id.toString());
        section.set("entity", steed.entity_id.toString());

        section.set("speed",       steed.speed);
        section.set("jump",        steed.jump);
        section.set("max_health",  steed.max_health);
        section.set("custom_name", steed.custom_name);
        section.set("type", steed.type.toString());
        section.set("alive", steed.isAlive);

        if (steed.style != null) {
            section.set("style",   steed.style.toString());
        }
        if (steed.horse_color != null) {
            section.set("horse_color",   steed.horse_color.toString());
        }

        FileManager.getInstance().SaveData("steed.yml");

    }

    public void RemoveSteed (Steed steed) {

        // Limited, only removes it from save.
        steeds.remove(steed);
        steedConfig.set("steeds." + steed.uuid.toString(), null);
        SurvivalEnhanced.GetFileManager().SaveData("steed.yml");

    }

    public void SummonSteed (Steed steed, Location location) {


        Messanger.InfoConsole("Steed Type " + steed.type + "");
        Messanger.InfoConsole("Custom Name" + steed.custom_name);
        Messanger.InfoConsole("Entity Id " + steed.entity_id);
        Messanger.InfoConsole("Alive: " + steed.isAlive);

        if (!steedConfig.getBoolean("steeds." + steed.uuid.toString() + ".stored")) {
            Bukkit.getPlayer(steed.owner_id).sendMessage(ChatColor.RED + "Your steed is already summoned.");
            return;
        }


        Entity entity = location.getWorld().spawnEntity(location, steed.type);
        steed.MigrateEntityId(entity.getUniqueId());

        // Unset steed as stored.
        UnStoreSteed(steed);

    }

    public void HighlightSteed(Steed steed) {


        Player player = Bukkit.getPlayer(steed.owner_id);
        Entity entity = Bukkit.getEntity(steed.entity_id);
        if (entity == null) {
            player.sendMessage(ChatColor.RED + steed.custom_name + " is too far away to see.");
            return;
        }

        float duration = (float) steedConfig.getDouble("settings.glow-duration");

        ((Creature) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Math.round(duration * 20), 3));
        player.sendMessage(ChatColor.YELLOW + steed.custom_name + " is glowing");
    }

    public void CallSteed(Steed steed) {

        Player p = Bukkit.getPlayer(steed.owner_id);
        Entity entity = Bukkit.getEntity(steed.entity_id);

        if (!steed.isAlive) {
            p.sendMessage(ChatColor.RED + "Sorry, " + steed.custom_name + " has left us.");
            return;
        }

        if (entity == null) {
            p.sendMessage(ChatColor.RED + "Your horse is too far away. like really... far.");
            return;
        }
        if (!p.getWorld().equals(entity.getWorld())) {
            p.sendMessage(ChatColor.RED + "Can't call your horse, you and your horse are in different dimensions.");
            return;
        }
        float max_distance = (float) steedConfig.getDouble("settings.max-call-distance");
        if (p.getLocation().distance(entity.getLocation()) > max_distance) {
            p.sendMessage(ChatColor.RED + "Your horse is too far away.");
            return;
        }

        p.sendMessage(ChatColor.YELLOW + "Called " + steed.custom_name + " to you");
        entity.teleport(p);

    }

    public List<Steed> GetSteedList() {
        return steeds;
    }

    public Steed GetSteed(UUID uuid) {
        for (Steed steed : steeds
             ) {
            if (steed.uuid.equals(uuid)) {
                return steed;
            }
        }
        return null;
    }

    public void SetMainSteed(Player p, Steed steed) {
        if (mainSteed.containsKey(p.getUniqueId())) {
            mainSteed.remove(p.getUniqueId());
        }
        mainSteed.put(p.getUniqueId(), steed);
        List<String> stringList = steedConfig.getStringList("main");
        stringList.remove(steed.uuid.toString());
        stringList.add(steed.uuid.toString());

        steedConfig.set("main", stringList);
        FileManager.getInstance().SaveData("steed.yml");
    }
}
