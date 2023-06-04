package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;

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


    public Entity entity;

    public Steed (UUID entity_id, UUID owner_id, UUID uuid) {
        this.entity_id = entity_id;
        this.owner_id = owner_id;
        this.uuid = uuid;
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


    public void MigrateEntityId (UUID newEntity) {

        // Change
        this.entity_id = newEntity;

        Entity entity = Bukkit.getEntity(entity_id);
        Horse horse = (Horse) entity;

        this.entity = entity;

        YamlConfiguration config = SteedManager.getInstance().steedConfig;
        // Change entity stats;
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                .setBaseValue(config.getDouble("steeds." + this.uuid.toString() + ".speed"));
        horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH)
                .setBaseValue(config.getDouble("steeds." + this.uuid.toString() + ".jump"));
        horse.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                .setBaseValue(config.getDouble("steeds." + this.uuid.toString() + ".health"));
        horse.setStyle(Horse.Style.valueOf(
                config.getString("steeds." + this.uuid.toString()) + ".custom_name"));




    }



}
