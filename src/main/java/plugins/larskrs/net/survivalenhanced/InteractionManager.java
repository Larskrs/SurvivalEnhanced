package plugins.larskrs.net.survivalenhanced;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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

        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            p.sendMessage(ChatColor.GREEN + "Selecting Horse...");
            p.sendMessage(ChatColor.YELLOW + "Right Click on the horse you want to select.");
        }
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
}
