package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

import java.util.ArrayList;
import java.util.List;

public class SteedCommand extends Command {


    public SteedCommand(SurvivalEnhanced se) {
        super(
                "steed",
                "enhancedsurvival",
                "This command will let you manage your steeds/horses",
                "steed",
                (new String[]{"donkey", "mule", "horse"})

        );
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {


        if (!(sender instanceof Player)) { return null; }
        Player player = (Player) sender;

        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.add("add");

            if (SteedManager.getInstance().GetSteed(player) == null ) {
                return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
            }
            options.add("call");
            options.add("glow");
            options.add("locate");
            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }


        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }

        if (args.length >= 1) {
            if (args[0].equals("add"))      { AddSteed(sender, args); }
            if (args[0].equals("glow"))     { HighlightSteed(sender, args); }
            if (args[0].equals("call"))     { CallSteed(sender, args); }
            if (args[0].equals("locate"))   { LocateSteed(sender, args); }
            return;
        }
    }

    private void LocateSteed(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can add horses.");
            return; }

        Player player = (Player) sender;

        Steed steed = SteedManager.getInstance().GetSteed(player);

        if (steed == (null)) {
            sender.sendMessage(ChatColor.RED + "You don't have a steed.");
            return;
        }

        if (!steed.isAlive) {
            sender.sendMessage(ChatColor.RED + steed.custom_name + " is 7 feet under. Hope that helps to locate em.");
            return;
        }

        Entity entity = Bukkit.getEntity(steed.entity_id);
        if (entity == null) {
            sender.sendMessage(ChatColor.RED + steed.custom_name + " is not loaded by the server.");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + steed.custom_name + " is at: " + ChatColor.WHITE
                        + " ("
               + " X: " + entity.getLocation().getBlockX()
               + " Y: " + entity.getLocation().getBlockY()
               + " Z: " + entity.getLocation().getBlockZ()
                       + " ) "

        );
    }

    private void CallSteed(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can add horses.");
            return; }

        Player player = (Player) sender;

        Steed steed = SteedManager.getInstance().GetSteed(player);

        if (steed == (null)) {
            sender.sendMessage(ChatColor.RED + "You don't have a steed.");
            return;
        }

        SteedManager.getInstance().CallSteed(steed);
    }

    private void HighlightSteed(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can add horses.");
            return; }

        Player player = (Player) sender;

        Steed steed = SteedManager.getInstance().GetSteed(player);

        if (steed == (null)) {
            sender.sendMessage(ChatColor.RED + "You don't have a steed.");
            return;
        }

        SteedManager.getInstance().HighlightSteed(steed);

    }

    private void AddSteed(CommandSender sender, String[] args) {


        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can add horses.");
            return; }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Make sure to give your friend a new name.");
            return;
        }

        String horseName = args[1];

        Player player = (Player) sender;


        SurvivalEnhanced.GetInteractionManager().SetInteraction(player.getUniqueId(), new SteedAddInteraction(player, horseName));

    }


}
