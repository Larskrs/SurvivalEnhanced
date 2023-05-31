package plugins.larskrs.net.survivalenhanced;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InteractionListener implements Listener {

    private SurvivalEnhanced se;

    public InteractionListener (SurvivalEnhanced se) {
        this.se = se;
    }

    @EventHandler
    public void OnRightClickEntity (PlayerInteractEntityEvent e) {


        // Prevent it from running twice by stopping if the interaction is from the offhand.
        if (e.getHand() == EquipmentSlot.OFF_HAND) { return; }
        if (e.getRightClicked() == null) { return; }
        if (!SurvivalEnhanced.GetInteractionManager().HasInteraction(e.getPlayer().getUniqueId())) { return; }

        SurvivalEnhanced.GetInteractionManager().InteractEntity (e.getRightClicked(), e.getPlayer().getUniqueId());


    }

    @EventHandler
    public void OnRightClickBlock (PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) { return; }
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }

        SurvivalEnhanced.GetInteractionManager().InteractBlock(e.getClickedBlock(), e.getPlayer().getUniqueId());
    }

}
