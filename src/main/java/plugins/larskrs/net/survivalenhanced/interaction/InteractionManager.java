package plugins.larskrs.net.survivalenhanced.interaction;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.interaction.Interaction;

import java.util.HashMap;
import java.util.UUID;

public class InteractionManager {

    private SurvivalEnhanced se;
    public HashMap<UUID, Interaction> interactions;

    public InteractionManager (SurvivalEnhanced se) {
        this.se = se;
        this.interactions = new HashMap<>();
    }

    public void SetInteraction (UUID uuid, Interaction interaction) {
        if (HasInteraction(uuid)) {
            interactions.remove(uuid);
        }
        interactions.put(uuid, interaction);
    }

    public boolean HasInteraction (UUID uuid) {
        return interactions.containsKey(uuid);
    }
    public Interaction GetInteraction (UUID uuid) {
        return interactions.get(uuid);
    }
    public void InteractEntity(Entity entity, UUID uuid) {
        if (!HasInteraction(uuid)) { return; }
        Interaction interaction = GetInteraction (uuid);
        interaction.onInteractEntity(entity);

        FulfillInteraction(interaction);
    }
    public void FulfillInteraction (Interaction interaction) {
        interactions.remove(interaction.holder.getUniqueId());
    }

    public void InteractBlock(Block block, UUID uuid) {
        if (!HasInteraction(uuid)) { return; }
        Interaction interaction = GetInteraction (uuid);
        interaction.onInteractBlock(block);

        FulfillInteraction(interaction);
    }
}
