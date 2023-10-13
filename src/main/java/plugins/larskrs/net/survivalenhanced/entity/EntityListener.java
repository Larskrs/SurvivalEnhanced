package plugins.larskrs.net.survivalenhanced.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

public class EntityListener implements Listener {

    @EventHandler
    public void punchVillager (EntitiesLoadEvent e) {

        ServerLevel wrld = ((CraftWorld) e.getWorld()).getHandle();
        for (Entity entity : e.getEntities()
             ) {
            if (!(entity instanceof Villager)) { continue; }
            AggressiveVillager AV = new AggressiveVillager(entity.getLocation());
            entity.remove();
            wrld.tryAddFreshEntityWithPassengers(AV);
        }


    }

}
