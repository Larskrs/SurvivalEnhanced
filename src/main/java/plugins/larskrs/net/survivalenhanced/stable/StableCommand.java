package plugins.larskrs.net.survivalenhanced.stable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;
import plugins.larskrs.net.survivalenhanced.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.steed.Steed;
import plugins.larskrs.net.survivalenhanced.steed.SteedManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StableCommand extends Command {

    private SurvivalEnhanced se;

    public StableCommand(SurvivalEnhanced se) {
        super(
                "stable",
                "enhancedsurvival",
                "This command will let you manage your stables",
                "stable"

        );

        this.se = se;
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 0) {
            return;
        }

        if (args[0].equals("create")) { CreateStable(sender, args); }
        if (args[0].equals("tp")) { TeleportStable(sender, args); }
        if (args[0].equals("store")) { StoreSteed(sender, args); }
        if (args[0].equals("summon")) { SummonSteed(sender, args); }

        

    }

    private void SummonSteed(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) { return; }

        Player p = (Player) sender;

        Steed steed = SteedManager.getInstance().GetSteed(p);

        if (!steed.isAlive) {
            p.sendMessage(ChatColor.RED + "Your steed is not alive.");
            return;
        }

        SteedManager.getInstance().SummonSteed(steed, p.getLocation());
    }


    private void StoreSteed(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) { return; }

        Player p = (Player) sender;

        Steed steed = SteedManager.getInstance().GetSteed(p);

        if (!steed.isAlive) {
            p.sendMessage(ChatColor.RED + "Your steed is not alive.");
            return;
        }

        steed.DespawnSteed();
        StableManager.getInstance().StoreSteed(steed);

    }

    private void TeleportStable(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) { return; }
        Player p = (Player) sender;

        if (args.length == 1) {


            Stable closest = StableManager.getInstance().GetClosestStable(p.getLocation());
            if (closest == null) {
                p.sendMessage(ChatColor.RED + "Could not find a stable nearby.");
                return;
            }

            p.teleport(closest.getLocation().toVector().add(new Vector(0.5f, 1f,0.5f)).toLocation(closest.getLocation().getWorld()));
            p.sendMessage(ChatColor.AQUA + "Sent you to the closest stable");

            return;
        }

        String stableName = args[1];

        if (!StableManager.getInstance().StableExists(stableName)) {
            p.sendMessage(ChatColor.RED + "Invalid Usage, the stable " + ChatColor.YELLOW + stableName + ChatColor.RED + " does not exist. Use a different name.");
            return;
        }

        Stable stable = StableManager.getInstance().GetStable(stableName);

        p.teleport((stable.getLocation().toVector().add(new Vector(.5f, 1, .5f))).toLocation(stable.getLocation().getWorld()));
    }

    private void CreateStable(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) { return; }
        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Invalid Usage, /stable create " + ChatColor.YELLOW + "(stable-name)");
            return;
        }

        String stableName = args[1];


        if (StableManager.getInstance().StableExists(stableName)) {
            p.sendMessage(ChatColor.RED + "The stable named, " + ChatColor.YELLOW + stableName + ChatColor.RED + " exists. Use a different name.");
            return;
        }

        StableManager.getInstance().RegisterStable(stableName, p.getUniqueId(), p.getLocation());
        SurvivalEnhanced.GetInteractionManager().SetInteraction(p.getUniqueId(), new SetStableCenterInteraction(p));


    }



    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {




        if (!(sender instanceof Player)) { return null; }
        Player player = (Player) sender;

        List<String> options = new ArrayList<>();

        if (args.length == 1) {
                options.add("create");
                options.add("store");
                options.add("summon");

            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("summon")) {
                for (Steed steed : SteedManager.getInstance().GetSteedList()
                     ) {
                    options.add(steed.custom_name);
                }
                return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
            }

            options.add("store");
            options.add("summon");

            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }


        return null;
    }
}
