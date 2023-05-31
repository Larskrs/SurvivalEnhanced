package plugins.larskrs.net.survivalenhanced;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
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

    public boolean AddHorse(UUID playerID, UUID horseID) {
        Entity entity =  Bukkit.getEntity(horseID);
        if (!(entity instanceof Horse)) {
            Bukkit.getConsoleSender().sendMessage("Not horse");
            return false; }
        Horse horse = (Horse) entity;

        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            Bukkit.getConsoleSender().sendMessage("Not player");
            return false; }

        if (this.horses.containsKey(playerID)) {
            this.horses.remove(playerID, horseID);
        }
        this.horses.put(playerID, horseID);




        UpdateHorseConfig();
        return true;
    }

    private void UpdateHorseConfig() {

        horseConfig.set("horses", SerializeHorseMap());
        se.fileManager.SaveData("horse.yml");
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
}
