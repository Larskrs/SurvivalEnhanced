package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import plugins.larskrs.net.survivalenhanced.FileManager;

import java.util.UUID;

public class Steed {

    public UUID entity_id;
    public UUID owner_id;
    public UUID uuid;

    public float speed;
    public float jump;
    public float max_health;
    public String custom_name;
    public Horse.Style style;
    public EntityType type;
    public boolean isAlive = true;


    public Entity entity;

    public Steed (UUID entity_id, UUID owner_id, UUID uuid, EntityType type) {
        this.entity_id = entity_id;
        this.owner_id = owner_id;
        this.uuid = uuid;
        this.type = type;
    }

    public Steed (Horse horse, UUID owner_id, UUID uuid) {
        this.entity_id = horse.getUniqueId();
        this.owner_id = owner_id;
        this.uuid = uuid;


        // Assign values
        speed =      (float)    horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        jump =       (float)    horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();
        max_health = (float)    horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        style =                 horse.getStyle();

        custom_name = horse.getCustomName();

        this.entity = horse;
        this.type = EntityType.HORSE;
    }
    public Steed (Donkey donkey, UUID owner_id, UUID uuid) {
        this.entity_id = donkey.getUniqueId();
        this.owner_id = owner_id;
        this.uuid = uuid;


        // Assign values
        speed =      (float)    donkey.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        jump =       (float)    donkey.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();
        max_health = (float)    donkey.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        custom_name = donkey.getCustomName();

        this.entity = donkey;
        this.type = EntityType.DONKEY;
    }
    public Steed (Mule mule, UUID owner_id, UUID uuid) {
        this.entity_id = mule.getUniqueId();
        this.owner_id = owner_id;
        this.uuid = uuid;


        // Assign values
        speed =      (float)    mule.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        jump =       (float)    mule.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();
        max_health = (float)    mule.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        custom_name = mule.getCustomName();

        this.entity = mule;
        this.type = EntityType.DONKEY;
    }

    public void SetSpeed (float speed) {
        this.speed = speed;
    }
    public void SetJump (float jump) {
        this.jump = jump;
    }
    public void SetMaxHealth (float max_health) {
        this.max_health = max_health;
    }
    public void SetCustomName (String custom_name) {
        this.custom_name = custom_name;
    }
    public void SetAlive (boolean alive ) { this.isAlive = alive; }


    public void MigrateEntityId (UUID newEntity) {

        // Change
        this.entity_id = newEntity;

        Entity entity = Bukkit.getEntity(entity_id);
        Creature creature = (Creature) entity;

        this.entity = entity;

        YamlConfiguration config = SteedManager.getInstance().steedConfig;
        // Change entity stats;
        creature.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                .setBaseValue(config.getDouble("steeds." + this.uuid.toString() + ".speed"));
        creature.getAttribute(Attribute.HORSE_JUMP_STRENGTH)
                .setBaseValue(config.getDouble("steeds." + this.uuid.toString() + ".jump"));
        creature.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                .setBaseValue(config.getDouble("steeds." + this.uuid.toString() + ".health"));
        if (creature instanceof Horse) {

            ((Horse) creature).setStyle(Horse.Style.valueOf(
                    config.getString("steeds." + this.uuid.toString()) + ".custom_name"));
        }




    }


    public void SetStyle(Horse.Style style) {
        this.style = style;
    }
    public void KillSteed ( ) {
        this.isAlive = false;
        Player p = Bukkit.getPlayer(owner_id);
        if (p == null) {
            return;
        }
        p.sendMessage(ChatColor.RED + custom_name + " has perished. Your steed is no longer available.");


        SteedManager.getInstance().steedConfig.set("steeds." + uuid.toString() + ".alive", false);
        FileManager.getInstance().SaveData("steed.yml");
    }
}
