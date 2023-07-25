package plugins.larskrs.net.survivalenhanced.dungeons;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party {

    private List<UUID> players;
    public List<NPC> npcs;
    private UUID leader;
    private PartyState state;

    public List<UUID> GetMembers () {
        return players;
    }
    public void SetLeader (UUID uuid) {
        this.leader = uuid;
    }


    public Party(Player leader) {
        this.players = new ArrayList<>();
        this.leader = leader.getUniqueId();
        this.state = PartyState.LOBBY;
        this.npcs = new ArrayList<>();
    }

    public void AddMember(UUID uuid) {
        players.add(uuid);
    }

    public void RemoveMember(UUID uuid) {
        players.remove(uuid);
    }
    public void SetState (PartyState state) {
        this.state = state;
    }
    public PartyState GetState () {
        return this.state;
    }
}

