package plugins.larskrs.net.survivalenhanced.bounty;

import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.util.UUID;

public class Bounty {

    private int id;
    private UUID target;
    private UUID issuer;
    private ItemStack reward;
    private BountyType type;
    Timestamp created_at;
    Timestamp deadline_at;

    public Bounty (
            int id,
            UUID target,
            UUID issuer,
            ItemStack reward,
            BountyType type,
            Timestamp created_at,
            Timestamp deadline_at
    ) {
        this.id = id;
        this.target = target;
        this.issuer = issuer;
        this.reward = reward;
        this.type = type;
        this.created_at = created_at;
        this.deadline_at = deadline_at;
    }



}
