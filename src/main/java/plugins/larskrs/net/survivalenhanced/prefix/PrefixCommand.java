package plugins.larskrs.net.survivalenhanced.prefix;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.steed.SteedManager;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

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

            if (!(sender instanceof Player)) {
                Messanger.ErrorConsole("Invalid Usage, You need to specify, set or clear? /prefix <set/clear>");
                return;
            }
            Player p = (Player) sender;

            PrefixMenu menu = new PrefixMenu(1);
            menu.OpenGUI(p);

            return;
        }

        if (args.length >= 1) {
            String arg = args[0].toLowerCase();
            if (arg.equals("set"))      { SetPrefix(sender, args); }
            if (arg.equals("clear"))     { ClearPrefix(sender, args); }
            return;
        }
    }

    private void ClearPrefix(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {

            // CONSOLE VERSION
            // requires to /prefix clear <player>


            return;
        }

        Player p = (Player) sender;

        p.sendMessage(ChatColor.YELLOW + "Clearing your prefix. ");
        PrefixManager.getInstance().ClearPrefix(p.getUniqueId());

    }

    private void SetPrefix(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {

            // CONSOLE VERSION
            // requires to /prefix set <prefix> <player>


            return;
        }

        Player p = (Player) sender;

        if (args.length <= 1) {

            PrefixMenu menu = new PrefixMenu(1);
            menu.OpenGUI(p);
            return;
        }

        Prefix prefix = PrefixManager.getInstance().GetPrefix(args[1]);

        if (prefix == null) {
            p.sendMessage(ChatColor.RED + "Could not find a prefix with the name of: " + args[1]);
            p.sendMessage(ChatColor.RED + "Invalid Usage, /prefix set <prefix>");
            return;
        }

        PrefixManager.getInstance().SetPrefix(p.getUniqueId(), prefix);
        p.sendMessage(ChatColor.YELLOW + "Set your prefix as " + prefix.GetDisplay());

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
            for (Prefix prefix : PrefixManager.getInstance().GetPrefixes()
                 ) {
                options.add(prefix.GetName());
            }

            return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
        }


        return null;
    }
}
