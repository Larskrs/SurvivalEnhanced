package plugins.larskrs.net.survivalenhanced;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class HorseCommand extends Command {

    private SurvivalEnhanced se;

    public HorseCommand(SurvivalEnhanced se) {
        super(
                "horse",
                "enhancedsurvival",
                "This command will let you manage your horses",
                "horse"

        );

        this.se = se;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }

        if (args.length >= 1) {
            if (args[0].equals("add")) { AddHorse(sender, args); }
            if (args[0].equals("glow")) { GlowHorse(sender, args); }
            if (args[0].equals("call")) { CallHorse(sender, args); }
            return;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {


        if (!(sender instanceof Player)) { return null; }
        Player player = (Player) sender;

        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.add("add");

            if (! SurvivalEnhanced.GetHorseManager().HasHorse(player.getUniqueId()) ) {
                return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
            }
            options.add("call");
            options.add("glow");
            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }


        return null;
    }

    private void CallHorse(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can glow their horse.");
            return; }

        Player player = (Player) sender;

        if (!se.horseManager.HasHorse(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Sorry, you need to set a main horse first.");
            return;
        }
        if (SurvivalEnhanced.GetHorseManager().GetHorse(SurvivalEnhanced.GetHorseManager().GetHorseId(player.getUniqueId())) == null) {
            sender.sendMessage(ChatColor.RED + "Sorry, your horse is dead.");
            return;

        }

        se.horseManager.RequestHorseTeleport (player.getUniqueId());


    }

    private void GlowHorse(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can glow their horse.");
            return; }

        Player player = (Player) sender;

        if (!se.horseManager.HasHorse(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Sorry, you need to set a main horse first.");
            return;
        }
        LivingEntity horse = se.horseManager.GetHorse(se.horseManager.GetHorseId(player.getUniqueId()));
        if (horse == null) {
            sender.sendMessage(ChatColor.RED + "Sorry, your horse is dead.");
            return;
        }
        se.horseManager.GlowHorse(horse.getUniqueId());
        if (horse.getCustomName() != null) {
            sender.sendMessage(ChatColor.YELLOW + "Glowing, " + ChatColor.AQUA + horse.getCustomName());
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Glowing horse.");
        }
    }



    private void AddHorse(CommandSender sender, String[] args) {


        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can add horses.");
            return; }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Make sure to give your friend a new name.");
            return;
        }

        String horseName = args[1];

        Player player = (Player) sender;

        SurvivalEnhanced.GetInteractionManager().SetInteraction(player.getUniqueId(), new HorseSelectInteraction(player, horseName));
    }



    private boolean getLookingAt(Player player, LivingEntity livingEntity){
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99D;
    }
    private List<Entity> getEntitys(Player player){
        List<Entity> entitys = new ArrayList<Entity>();
        for(Entity e : player.getNearbyEntities(10, 10, 10)){
            if(e instanceof LivingEntity){
                if(getLookingAt(player, (LivingEntity) e)){
                    entitys.add(e);
                }
            }
        }

        return entitys;
    }








}












