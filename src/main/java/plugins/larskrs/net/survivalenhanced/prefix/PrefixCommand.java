package plugins.larskrs.net.survivalenhanced.prefix;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.steed.SteedManager;

import java.util.ArrayList;
import java.util.List;

public class PrefixCommand extends Command {
    public PrefixCommand(SurvivalEnhanced survivalEnhanced) {
        super(
                "prefix",
                "enhancedsurvival.command.prefix",
                "This command will let you manage your personal prefix",
                "prefix",
                (new String[]{"pfix", "pf"})

        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            // TODO: Open prefix menu.
            return;
        }

        if (args.length >= 1) {
            if (args[0].equals("add"))      { SetPrefix(sender, args); }
            if (args[0].equals("glow"))     { ClearPrefix(sender, args); }
            return;
        }
    }

    private void ClearPrefix(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {

            // CONSOLE VERSION
            // requires to /prefix clear <player>


            return;
        }

        Player player = (Player) sender;

    }

    private void SetPrefix(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {

            // CONSOLE VERSION
            // requires to /prefix set <prefix> <player>


            return;
        }

        Player player = (Player) sender;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {


        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.add("set");
            options.add("clear");

            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }

        if (args.length == 2) {
            sender.sendMessage("[" + args[1]);
            for (Prefix prefix : PrefixManager.getInstance().GetPrefixes()
                 ) {
                options.add(prefix.GetName());
            }

            return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
        }


        return null;
    }
}
