package plugins.larskrs.net.survivalenhanced.stable;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;
import plugins.larskrs.net.survivalenhanced.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.gui.DynamicContentGUI;
import plugins.larskrs.net.survivalenhanced.steed.Steed;
import plugins.larskrs.net.survivalenhanced.steed.SteedManager;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;
import plugins.larskrs.net.survivalenhanced.tools.NumberTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StableCommand extends Command {

    private final SurvivalEnhanced se;

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
        if (args[0].equals("list")) { ListStables(sender, args); }

        

    }


    private void ListStables(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) { return; }

        Player p = (Player) sender;
        int page = 1;
        if (args.length == 2) {
            if (NumberTools.isNumeric(args[1])) {
                page = Integer.parseInt(args[1]);
            }
        }

//        DynamicContentGUI stableGUI = new DynamicContentGUI(page, 6, "Stable List");

//        for (Material mat : Material.values()) {
//
//            ItemStack item = new ItemStack(mat);
//
//            if (item.hasItemMeta()) {
//                // Handle item meta if possible
//                ItemMeta meta = item.getItemMeta();
//                meta.setDisplayName(ChatColor.YELLOW  + mat.name());
//                item.setItemMeta(meta);
//                //
//            }
//            stableGUI.RegisterItemStack(item);
//        }

//
//        for (Stable stable : StableManager.getInstance().GetStables()) {
//
//            ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
//
//            Player player = Bukkit.getPlayer(stable.getOwner());
//
//            SkullMeta meta = (SkullMeta) skull.getItemMeta();
//            meta.setOwner(player.getName());
//            meta.setDisplayName(ChatColor.YELLOW + stable.getName());
//            skull.setItemMeta(meta);
//
//            stableGUI.RegisterItemStack(skull);
//        }
//
//        stableGUI.OpenGUI(p, page);


    }

    private void SummonSteed(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) { return; }

        Player p = (Player) sender;

        Steed steed = SteedManager.getInstance().GetSteed(p);

        if (!steed.isAlive) {
            p.sendMessage(ChatColor.RED + steed.custom_name + " is not alive.");
            return;
        }

        Stable stable = StableManager.getInstance().GetClosestStable(p.getLocation());
        Location storingLocation = stable.getLocation();
        double distance = p.getLocation().distance(storingLocation);

        if (distance > StableManager.getInstance().GetStableRadius()) {
            p.sendMessage(ChatColor.RED + "You are too far away from any stables.");
            p.sendMessage(ChatColor.YELLOW + "Closest stable " + stable.getName() + " is at ");
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

        // The location that will have to be close to a stable.
        Stable stable = StableManager.getInstance().GetClosestStable(p.getLocation());
        Location storingLocation = stable.getLocation();
        double distance = p.getLocation().distance(storingLocation);

        if (distance > StableManager.getInstance().GetStableRadius()) {
            p.sendMessage(ChatColor.RED + "You are too far away from any stables.");
            p.sendMessage(ChatColor.YELLOW + "Closest stable " + stable.getName() + " is at ");
            return;
        }

        steed.DespawnSteed();
        StableManager.getInstance().StoreSteed(steed);

    }

    private void TeleportStable(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) { return; }
        Player p = (Player) sender;

        if (!p.hasPermission("survivalenhanced.command.stable.tp")) {
            p.sendMessage(ChatColor.RED + "You don't have permission to teleport to stables.");
            return;
        }

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
                if (player.hasPermission("survivalenhanced.command.stable.tp")) {
                    options.add("tp");
                }


            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("summon")) {
                for (Steed steed : SteedManager.getInstance().GetSteedList()
                     ) {

                    if (!steed.isAlive) { continue; }

                    options.add(steed.custom_name);
                }
                return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
            }
            if (args[0].equalsIgnoreCase("tp")) {
                for (Stable stable : StableManager.getInstance().GetStables()
                ) {
                    options.add(stable.getName());
                }
                return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
            }

            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }


        return null;
    }
}
