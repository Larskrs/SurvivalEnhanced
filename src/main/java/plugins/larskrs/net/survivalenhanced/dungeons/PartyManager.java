package plugins.larskrs.net.survivalenhanced.dungeons;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.trait.SneakTrait;
import net.citizensnpcs.util.PlayerAnimation;
import net.milkbowl.vault.chat.Chat;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.A;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.general.FileManager;
import plugins.larskrs.net.survivalenhanced.location.LocationChange;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;
import plugins.larskrs.net.survivalenhanced.watchover.WatchoverModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class PartyManager extends BukkitRunnable implements Listener  {

    private static HashMap<UUID, Party> parties;
    public static YamlConfiguration config;
    public static HashMap<UUID, NPC> npcMap;
    private BukkitTask task;

    public static NPCRegistry registry = null;

    public PartyManager() {
        parties = new HashMap<>();
        npcMap = new HashMap<>();
        Messanger.InfoConsole("Party setup.");
        new PartyCommand();

        registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());

        Bukkit.getPluginManager().registerEvents(this, SurvivalEnhanced.getInstance());

        FileManager.getInstance().LoadFile("party.yml");
        config = FileManager.getInstance().GetYamlConfig("party.yml");


        if (task != null) {
            this.task.cancel();
            this.task = null;
        }
        this.task = runTaskTimer(SurvivalEnhanced.getInstance(), 0, 10);
    }

    public static Party GetParty (UUID uuid) {
        return parties.get(uuid);
    }

    public static void RemovePlayer (Player player) {
        Party party = GetParty(player.getUniqueId());

        MessageParty(party, ChatColor.YELLOW + player.getName() + " left the party. ");

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.showPlayer(p);
        }

        player.teleport(WatchoverModule.getLastLocation(player.getUniqueId()).getLocation());

        party.RemoveMember(player.getUniqueId());
        parties.remove(player.getUniqueId());

        HideNPC(player);

        for (UUID uuid : party.GetMembers()) {
            HideNPC(Bukkit.getPlayer(uuid));
            DisplayNPC(Bukkit.getPlayer(uuid));
        }
    }

    private static void HideNPC(Player player) {
        NPC npc = npcMap.get(player.getUniqueId());
        npcMap.remove(player.getUniqueId());
        npc.destroy();
    }

    public static void MigratePartyLeader (Party party) {
        UUID newLeader = party.GetMembers().get(0);
        MigratePartyLeader(party, newLeader);
    }
    public static void MigratePartyLeader (Party party, UUID newLeader) {
        party.SetLeader(newLeader);
        Player player = Bukkit.getPlayer(newLeader);

        MessageParty(party, ChatColor.YELLOW + "Changed party leader to: " + player.getName());
    }

    public static void MessageParty (Party party, String message) {
        for (UUID uuid : party.GetMembers()
             ) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                p.sendMessage(message);
            }
        }
    }

    public static Party CreateParty (Player leader) {
        Party party = new Party(leader);
        AddMember(party, leader);

        MessageParty(party, ChatColor.GREEN + "Created a new party.");

        return party;
    }

    public static void AddMember (Party party, Player player) {


        Location spawnLocation = FileManager.getInstance().ReadLocation("party.yml", "party.spawn");
        player.teleport(spawnLocation);
        WatchoverModule.SaveLastLocation(player, true, LocationChange.SPECIAL);

        party.AddMember(player.getUniqueId());
        AddToParty(player.getUniqueId(), party);

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(p);
        }

        HideOtherNPC (player);
        DisplayNPC(player);

        for (Player pl : party.GetMembers().stream().map(uuid -> Bukkit.getPlayer(uuid)).collect(Collectors.toList())
             ) {
            Location npcLocation = FileManager.getInstance().ReadLocation("party.yml", "party.pedestal." + party.GetMembers().indexOf(player.getUniqueId()));
            pl.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,
                    npcLocation.add(0, 2D, 0), 500);
        }

        MessageParty(party, ChatColor.YELLOW + player.getName() + " joined the party.");
    }

    private static void HideOtherNPC(Player player) {
        Party party = GetParty(player.getUniqueId());

        for (UUID uuid : npcMap.keySet()) {
            if (!party.GetMembers().contains(uuid)) {
                Entity entity = npcMap.get(uuid).getEntity();
                player.hideEntity(SurvivalEnhanced.getInstance(), entity);
            }
        }
    }

    public static void DisplayNPC (Player player) {

        Party party = GetParty(player.getUniqueId());

        NPC npc = createPlayerNPC(player, party.GetMembers().indexOf(player.getUniqueId()));
        npcMap.put(player.getUniqueId(), npc);

        for (UUID uuid : parties.keySet()
             ) {
            if (!party.GetMembers().contains(uuid)) {
                Player p = Bukkit.getPlayer(uuid);
                p.hideEntity(SurvivalEnhanced.getInstance(), npc.getEntity());
            }
        }

    }

    private static NPC createPlayerNPC(Player player, int index) {
        Location location = FileManager.getInstance().ReadLocation("party.yml", "party.pedestal." + index);
        NPC npc = registry.createNPC(EntityType.PLAYER, player.getName());
        npc.getOrAddTrait(SkinTrait.class).setSkinName(player.getName());
        npc.setAlwaysUseNameHologram(false);
        npc.setName(ChatColor.YELLOW + "[ " + player.getName() + " ]");
        npc.spawn(location);


        return npc;
    }
    
    public static void AddToParty(UUID uuid, Party party) {
        parties.put(uuid, party);
    }

    public static boolean HasParty(UUID uuid) {
        return parties.containsKey(uuid);
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (HasParty(p.getUniqueId())) {
                e.getPlayer().hidePlayer(p);
                p.hidePlayer(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onMove (PlayerMoveEvent e) {
        if (!HasParty(e.getPlayer().getUniqueId())) {
            return;
        }
        Party party = GetParty(e.getPlayer().getUniqueId());
        if (party.GetState().equals(PartyState.LOBBY)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSneak (PlayerToggleSneakEvent e) {
        if (!HasParty(e.getPlayer().getUniqueId())) {
            return;
        }
        Party party = GetParty(e.getPlayer().getUniqueId());
        if (party.GetState().equals(PartyState.LOBBY)) {
            e.setCancelled(true);
        }

        NPC npc = npcMap.get(e.getPlayer().getUniqueId());
        npc.getOrAddTrait(SneakTrait.class).setSneaking(e.isSneaking());
    }
    @EventHandler
    public void onTeleport (PlayerTeleportEvent e) {
        if (!HasParty(e.getPlayer().getUniqueId())) {
            return;
        }
        Party party = GetParty(e.getPlayer().getUniqueId());
        if (party.GetState().equals(PartyState.LOBBY)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDamage (EntityDamageEvent e) {
        if (!HasParty(e.getEntity().getUniqueId())) {
            return;
        }
        Party party = GetParty(e.getEntity().getUniqueId());
        if (party.GetState().equals(PartyState.LOBBY)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onFoodChange (FoodLevelChangeEvent e) {
        if (!HasParty(e.getEntity().getUniqueId())) {
            return;
        }
        Party party = GetParty(e.getEntity().getUniqueId());
        if (party.GetState().equals(PartyState.LOBBY)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract (PlayerInteractEvent e) {
        if (!HasParty(e.getPlayer().getUniqueId())) {
            return;
        }
        Party party = GetParty(e.getPlayer().getUniqueId());
        if (party.GetState().equals(PartyState.LOBBY)) {
            e.setCancelled(true);
        }
        NPC npc = npcMap.get(e.getPlayer().getUniqueId());
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            PlayerAnimation.ARM_SWING.play((Player) npc.getEntity());
        }
    }
    @EventHandler
    public void onInteract (PlayerSwapHandItemsEvent e) {
        if (!HasParty(e.getPlayer().getUniqueId())) {
            return;
        }
        Party party = GetParty(e.getPlayer().getUniqueId());
        if (party.GetState().equals(PartyState.LOBBY)) {
            e.setCancelled(true);
        }
        NPC npc = npcMap.get(e.getPlayer().getUniqueId());
        PlayerAnimation.START_USE_OFFHAND_ITEM.play((Player) npc.getEntity());
    }

    @Override
    public void run() {
        for (UUID uuid : npcMap.keySet()
             ) {
            Player player = Bukkit.getPlayer(uuid);
            Equipment equipment = npcMap.get(uuid).getOrAddTrait(Equipment.class);

            equipment.set(Equipment.EquipmentSlot.HELMET, player.getInventory().getHelmet());
            equipment.set(Equipment.EquipmentSlot.CHESTPLATE, player.getInventory().getChestplate());
            equipment.set(Equipment.EquipmentSlot.LEGGINGS, player.getInventory().getLeggings());
            equipment.set(Equipment.EquipmentSlot.BOOTS, player.getInventory().getBoots());
            equipment.set(Equipment.EquipmentSlot.HAND, player.getInventory().getItemInMainHand());
            equipment.set(Equipment.EquipmentSlot.OFF_HAND, player.getInventory().getItemInOffHand());
        }
    }
}
