package plugins.larskrs.net.survivalenhanced.stable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import plugins.larskrs.net.survivalenhanced.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

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

    }

    private void TeleportStable(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) { return; }
        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Invalid Usage, /stable create " + ChatColor.YELLOW + "(stable-name)");
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



        return null;
    }
}
