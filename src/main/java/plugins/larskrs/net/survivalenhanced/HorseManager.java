package plugins.larskrs.net.survivalenhanced;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HorseManager {

    private SurvivalEnhanced se;
    private YamlConfiguration horseConfig;
    private HashMap<UUID, UUID> horses;

    public HorseManager (SurvivalEnhanced se) {
        this.se = se;

        // Setup horse config
        se.fileManager.LoadFile("horse.yml");
        horseConfig = se.fileManager.GetYamlFromString("horse.yml");

        SetDefaultConfigValues();

        this.horses = LoadHorseOwners();
    }

    public boolean AddHorse(UUID playerID, UUID horseID, String horseName) {
        Entity entity =  Bukkit.getEntity(horseID);
        if (!(entity instanceof Horse)) {
            Bukkit.getConsoleSender().sendMessage("Not horse");
            return false; }
        Horse horse = (Horse) entity;
        horse.setCustomName(horseName);

        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            Bukkit.getConsoleSender().sendMessage("Not player");
            return false; }

        if (this.horses.containsKey(playerID)) {
            this.horses.remove(playerID, horseID);
        }
        this.horses.put(playerID, horseID);


        SaveHorse(horse, playerID);
        return true;
    }


    private List<String> SerializeHorseMap() {

        List<String> horseList = new ArrayList<>();
        for (UUID player : this.horses.keySet()) {
            horseList.add(player + ":" + GetHorseId(player));
        }
        return horseList;
    }

    public LivingEntity GetHorse (UUID horseID) {
        Entity entity = Bukkit.getEntity(horseID);
        if (entity == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR: horse is null");
            return null;
        }
        if (!(entity instanceof LivingEntity)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR: horse entity is not a living entity");
            return null;
        }

        LivingEntity livingEntity = (LivingEntity) entity;


        return livingEntity;
    }
    public void GlowHorse (UUID horseID) {
        LivingEntity horse = GetHorse(horseID);
        float duration = 3;
        horse.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Math.round(duration * 20), 3));
        World world = horse.getWorld();
        world.playSound(horse.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.VOICE, 1.5f, 1.0f);
    }

    public UUID GetHorseId (UUID playerID) {
        return this.horses.get(playerID);
    }

    public void DisplayHorses (CommandSender sender) {
        for (UUID playerID : this.horses.keySet()) {

            UUID horseID = GetHorseId(playerID);
            sender.sendMessage(ChatColor.GREEN + "Player: " + Bukkit.getOfflinePlayer(playerID).getName() + ChatColor.WHITE + ":" + ChatColor.AQUA + Bukkit.getEntity(horseID).getCustomName());
        }
    }



    public HashMap<UUID, UUID> LoadHorseOwners () {

        HashMap<UUID, UUID> hashMap = new HashMap<>();

        for (String string : horseConfig.getStringList("horses")) {
            String[] array = string.split(":");
            UUID playerID = UUID.fromString(array[0]);
            UUID horseID = UUID.fromString(array[1]);

            hashMap.put(playerID, horseID);
            Player p = Bukkit.getPlayer(playerID);
            if (p != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Added " + p.getName() + "`s horse");
            }
        }

        return hashMap;
    }

    public boolean HasHorse(UUID playerID) {

        if (!this.horses.containsKey(playerID)) { return false; }

        UUID horseID = this.horses.get(playerID);
        Entity horse = Bukkit.getEntity(horseID);

        return horse != null;
    }

    public void RequestHorseTeleport(UUID playerID) {
        Player player = Bukkit.getPlayer(playerID);
        LivingEntity livingEntity = GetHorse(GetHorseId(playerID));

        if (player.getWorld() != livingEntity.getWorld()) {
            player.sendMessage(ChatColor.RED + "Your horse is too far away to call.");
            return;
        }
        double maxDistance = horseConfig.getDouble("max-horse-distance");
        if (player.getLocation().distance(livingEntity.getLocation()) > maxDistance) {
            player.sendMessage(ChatColor.RED + "Your horse is too far away to call.");
            return;
        }


        if (livingEntity.getCustomName() != null) {
            player.sendMessage(ChatColor.AQUA + livingEntity.getCustomName() + ChatColor.YELLOW + " has found you!");
        } else {
            player.sendMessage(ChatColor.YELLOW + "Your horse has found you!");
        }
        livingEntity.teleport(player);
        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_LAND, 1.0f, 1.0f);

    }

    private Player GetHorseOwner (UUID horseID) {
        UUID playerID = null;
        for (UUID pID : this.horses.keySet()) {
            if (GetHorseId(pID) == horseID) {
                playerID = pID;
            }
        }
        if (playerID == null) { return null; }
        return Bukkit.getPlayer(playerID);
    }

    private void SetDefaultConfigValues () {
        if (!horseConfig.contains("max-horse-distance")) {
            horseConfig.set("max-horse-distance", 50);
        }

        se.fileManager.SaveData("horse.yml");


    }
    public void SaveHorse (Horse horse, UUID owner) {
        UUID steedID = UUID.randomUUID();
        ConfigurationSection section = horseConfig.createSection("horses." + steedID.toString());

        float jumpStrength = (float) horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getValue();
        float movementSpeed = (float) horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue();
        float maxHealth = (float) horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        section.set("jump", jumpStrength);
        section.set("speed", movementSpeed);
        section.set("health", maxHealth);

        section.set("owner", owner.toString());
        section.set("custom_name", horse.getCustomName() + "");
        section.set("style", horse.getStyle().toString());
        section.set("entity", horse.getUniqueId().toString());

        SurvivalEnhanced.GetFileManager().SaveData("horse.yml");
    }

    public void SpawnSteed (UUID steedID, Location location) {

        Horse horse = (Horse) location.getWorld().spawnEntity(location, EntityType.HORSE);
        horse.setStyle(Horse.Style.valueOf(horseConfig.getString("horses." + steedID.toString())+".style"));
        horse.setJumpStrength(horseConfig.getDouble("horses." + steedID.toString() + ".jump"));
        horse.setMaxHealth(horseConfig.getDouble("horses." + steedID.toString() + ".health"));
        horse.setCustomName(horseConfig.getString("horses." + steedID.toString() + ".custom_name"));
        horse.setTamed(true); // all horses have to be tamed to be added.

        SetSteedEntityId (steedID, horse.getUniqueId());

    }

    private void SetSteedEntityId(UUID steedID, UUID uniqueId) {

        horseConfig.set("horses." + steedID.toString() + ".entity", uniqueId.toString());
        se.GetFileManager().SaveData("horse.yml");

    }

    public void SetPrimary (UUID steedID, UUID owner) {

        horseConfig.set("primary."+owner.toString(), steedID.toString());

    }

    public UUID GetPrimarySteed (UUID owner) {

        UUID steedID = UUID.fromString(horseConfig.getString("primary." + owner.toString()));
        return steedID;

    }


}
