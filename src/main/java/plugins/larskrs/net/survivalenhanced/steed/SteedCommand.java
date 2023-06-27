package plugins.larskrs.net.survivalenhanced.steed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixMenu;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.List;

public class SteedCommand extends Command {


    public SteedCommand(SurvivalEnhanced se) {
        super(
                "steed",
                "survivalenhanced.command.steed",
                "This command will let you manage your steeds/horses",
                "steed",
                (new String[]{"donkey", "mule", "horse", "camel"})

        );
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {


        if (!(sender instanceof Player)) { return null; }
        Player player = (Player) sender;

        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.add("add");

            if (SteedModule.getInstance().GetSteed(player) == null ) {
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
            OpenSteedMenu(sender, args);
            return;
        }

        if (args.length >= 1) {
            if (args[0].equals("add"))      { AddSteed(sender, args); return;}
            if (args[0].equals("glow"))     { HighlightSteed(sender, args); return;}
            if (args[0].equals("call"))     { CallSteed(sender, args); return; }
            if (args[0].equals("locate"))   { LocateSteed(sender, args); return;}
            OpenSteedMenu(sender, args);
            return;
        }
    }

    private void OpenSteedMenu(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Messanger.ErrorConsole("Invalid Usage, You need to specify /steed <add/glow/call/locate>");
            return;
        }
        Player p = (Player) sender;

        SteedMenu menu = new SteedManageMenu(1);
        menu.OpenGUI(p);


    }



    private void LocateSteed(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can add horses.");
            return; }

        Player player = (Player) sender;



        if (SteedModule.getInstance().GetSteed(player) == (null)) {
            sender.sendMessage(ChatColor.RED + "You don't have a steed.");
            return;
        }

        Steed steed = SteedModule.getInstance().GetMainSteed(player);

        if (steed == (null)) {
            sender.sendMessage(ChatColor.RED + "You don't have a steed.");
            return;
        }

        if (!steed.isAlive) {
            sender.sendMessage(ChatColor.RED + steed.custom_name + " is 7 feet under. Hope that helps to locate em.");
            return;
        }

        Entity entity = Bukkit.getEntity(steed.entity_id);
        Location location = null;
        if (entity == null) {
            sender.sendMessage(ChatColor.RED + steed.custom_name + " was last seen here: ");
            location = steed.GetLastLocation();
        } else {
            location = entity.getLocation();
        }


        player.sendMessage(ChatColor.YELLOW + steed.custom_name + " is at: " + ChatColor.WHITE
                        + " ("
               + " X: " + location.getBlockX()
               + " Y: " + location.getBlockY()
               + " Z: " + location.getBlockZ()
                       + " ) "

        );
    }

    private void CallSteed(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can add horses.");
            return; }

        Player player = (Player) sender;

        if (SteedModule.getInstance().GetSteed(player) == (null)) {
            sender.sendMessage(ChatColor.RED + "You don't have a steed.");
            return;
        }

        Steed steed = SteedModule.getInstance().GetMainSteed(player);

        if (steed == (null)) {
            sender.sendMessage(ChatColor.RED + "You don't have a steed.");
            return;
        }

        SteedModule.getInstance().CallSteed(steed);
    }

    private void HighlightSteed(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, Only players can add horses.");
            return; }

        Player player = (Player) sender;

        if (SteedModule.getInstance().GetSteed(player) == (null)) {
            sender.sendMessage(ChatColor.RED + "You don't have a steed.");
            return;
        }

        Steed steed = SteedModule.getInstance().GetMainSteed(player);

        if (steed == (null)) {
            sender.sendMessage(ChatColor.RED + "You don't have a steed.");
            return;
        }

        if (!steed.isAlive) {
            sender.sendMessage(ChatColor.RED + steed.custom_name + " is 7 feet under. Hope that helps to locate em.");
            return;
        }

        SteedModule.getInstance().HighlightSteed(steed);

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
