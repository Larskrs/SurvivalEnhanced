package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.UUID;

public class Steed {

    public UUID entity_id;
    public UUID owner_id;
    public UUID uuid;

    public float speed;
    public float jump;
    public float max_health;
    public String custom_name;
    public Horse.Style style = null;
    public EntityType type;
    public boolean isAlive = true;
    public boolean isStored = false;
    public Horse.Color horse_color;


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
        this.horse_color = horse.getColor();
        SaveLastLocation(horse.getLocation());
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
        SaveLastLocation(donkey.getLocation());
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
        SaveLastLocation(mule.getLocation());
    }
    public Steed (Camel camel, UUID owner_id, UUID uuid) {
        this.entity_id = camel.getUniqueId();
        this.owner_id = owner_id;
        this.uuid = uuid;


        // Assign values
        speed =      (float)    camel.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        jump =       (float)    camel.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();
        max_health = (float)    camel.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        custom_name = camel.getCustomName();

        this.entity = camel;
        this.type = EntityType.CAMEL;
        SaveLastLocation(camel.getLocation());
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
    public void SetStored (boolean stored) { this.isStored = stored; }

    public void MigrateEntityId (UUID newEntity) {

        // Change

        UpdateSteedEntityID (newEntity);

        Entity entity = Bukkit.getEntity(entity_id);
        Creature creature = (Creature) entity;

        this.entity = entity;

        YamlConfiguration config = SteedModule.getInstance().steedConfig;
        // Change entity stats;
        creature.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                .setBaseValue(config.getDouble("steeds." + this.uuid.toString() + ".speed"));
        creature.getAttribute(Attribute.HORSE_JUMP_STRENGTH)
                .setBaseValue(config.getDouble("steeds." + this.uuid.toString() + ".jump"));
        creature.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                .setBaseValue(config.getDouble("steeds." + this.uuid.toString() + ".max_health"));
        creature.setHealth(creature.getMaxHealth());
        creature.setCustomName(config.getString("steeds." + this.uuid.toString() + ".custom_name"));



        if (creature instanceof Mule) {
            Mule mule = (Mule) creature;

            mule.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            mule.setTamed(true);
        }
        if (creature instanceof Donkey) {
            Donkey donkey = (Donkey) creature;

            donkey.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            donkey.setTamed(true);
        }
        if (creature instanceof Camel) {
            Camel camel = (Camel) creature;

            camel.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            camel.setTamed(true);
        }

        if (creature instanceof Horse) {

            Horse horse = (Horse) creature;
            horse.setTamed(true);

            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

            Horse.Style _style = Horse.Style.valueOf(
                    config.getString("steeds." + this.uuid.toString() + ".style")
            );
            Horse.Color _color = Horse.Color.valueOf(
                    config.getString("steeds." + this.uuid.toString() + ".horse_color")
            );

            Messanger.InfoConsole(_style + " Is the style");

            ((Horse) creature).setStyle(_style);
            ((Horse) creature).setColor(_color);

        }


    }

    private void UpdateSteedEntityID(UUID newEntity) {
        this.entity_id = newEntity;
        SteedModule.getInstance().steedConfig.set("steeds." + uuid.toString() + ".entity", newEntity.toString());
        FileManager.getInstance().SaveData("steed.yml");
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


        SteedModule.getInstance().steedConfig.set("steeds." + uuid.toString() + ".alive", false);
        FileManager.getInstance().SaveData("steed.yml");
    }

    public void DespawnSteed() {

        Messanger.InfoConsole("This is to be despawned ; " + entity_id.toString());

        Entity _ent = Bukkit.getEntity(entity_id);
        Messanger.InfoConsole(_ent.getCustomName() + " - ");
        _ent.remove();

    }

    public void SaveLastLocation(Location location) {

        FileManager.getInstance().SaveLocation("steed.yml", "steeds." + uuid.toString() + ".last_location", location);


    }

    public Location GetLastLocation () {

        return FileManager.getInstance().ReadLocation("steed.yml", "steeds." + uuid.toString() + ".last_location");
    }

    public void SetHorseColor(Horse.Color horse_color) {
        this.horse_color = horse_color;
    }
}
