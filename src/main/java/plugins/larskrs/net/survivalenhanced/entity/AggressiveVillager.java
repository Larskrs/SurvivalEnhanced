package plugins.larskrs.net.survivalenhanced.entity;



import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.attribute.CraftAttributeMap;

import java.lang.reflect.Field;
import java.util.Map;

public class AggressiveVillager extends Villager {
    public AggressiveVillager(Location loc) {
        super(EntityType.VILLAGER, ((CraftWorld) loc.getWorld()).getHandle());

        this.setPos(loc.getX(), loc.getY(), loc.getZ());

        this.setCanPickUpLoot(false); // Can Pick up Loot
        this.setAggressive(true); // Aggressive
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<ServerPlayer>(this, ServerPlayer.class, true));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<Zombie>(this, Zombie.class, true));

        try {
            registerAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        this.getAttributes().getSyncableAttributes().add(new AttributeInstance(Attributes.ATTACK_DAMAGE, (a) -> {a.setBaseValue(1.0);}));
    }


    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, ServerPlayer.class, true, false));
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, true));
    }

    public void registerAttribute(org.bukkit.attribute.Attribute a) throws IllegalAccessException{
        Field attributeField = null;
        try {
            attributeField = AttributeMap.class.getDeclaredField("b");
            attributeField.setAccessible(true);
        }catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
        AttributeMap attributeMap = this.getAttributes();
        net.minecraft.world.entity.ai.attributes.Attribute attribute = CraftAttributeMap.toMinecraft(a);
        Map<Attribute, AttributeInstance> map = (Map<Attribute, AttributeInstance>) attributeField.get(attributeMap);
        AttributeInstance at = new AttributeInstance(attribute, AttributeInstance::getAttribute);
        map.put(attribute, at);

    }
}
